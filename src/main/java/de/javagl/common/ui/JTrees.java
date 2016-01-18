/*
 * www.javagl.de - Common - UI
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.common.ui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * Utility methods related to trees
 */
public class JTrees
{
    /**
     * Expand all rows of the given tree. <br>
     * <br>
     * Note that for large trees, {@link #expandAllFixedHeight(JTree)} may
     * be significantly faster.
     * 
     * @param tree The tree
     */
    public static void expandAll(JTree tree)
    {
        int r = 0;
        while (r < tree.getRowCount())
        {
            tree.expandRow(r);
            r++;
        }
    }
    
    /**
     * Expand all rows of the given tree, assuming a fixed height for
     * the rows.
     * 
     * @param tree The tree
     */
    public static void expandAllFixedHeight(JTree tree)
    {
        // Determine a suitable row height for the tree, based on the 
        // size of the component that is used for rendering the root 
        TreeCellRenderer cellRenderer = tree.getCellRenderer();
        Component treeCellRendererComponent = 
            cellRenderer.getTreeCellRendererComponent(
                tree, tree.getModel().getRoot(), false, false, false, 1, false);
        int rowHeight = treeCellRendererComponent.getPreferredSize().height + 2;
        tree.setRowHeight(rowHeight);
        
        // Temporarily remove all listeners that would otherwise
        // be flooded with TreeExpansionEvents
        List<TreeExpansionListener> expansionListeners =
            Arrays.asList(tree.getTreeExpansionListeners());
        for (TreeExpansionListener expansionListener : expansionListeners)
        {
            tree.removeTreeExpansionListener(expansionListener);
        }
        
        // Recursively expand all nodes of the tree
        TreePath rootPath = new TreePath(tree.getModel().getRoot());
        expandAllRecursively(tree, rootPath);
        
        // Restore the listeners that the tree originally had
        for (TreeExpansionListener expansionListener : expansionListeners)
        {
            tree.addTreeExpansionListener(expansionListener);
        }
        
        // Trigger an update for the TreeExpansionListeners
        tree.collapsePath(rootPath);
        tree.expandPath(rootPath);
    }
    
    /**
     * Recursively expand all paths in the given tree, starting with the
     * given path
     *  
     * @param tree The tree
     * @param treePath The current tree path
     */
    private static void expandAllRecursively(JTree tree, TreePath treePath)
    {
        TreeModel model = tree.getModel();
        Object lastPathComponent = treePath.getLastPathComponent();
        int childCount = model.getChildCount(lastPathComponent);
        if (childCount == 0)
        {
            return;
        }
        tree.expandPath(treePath);
        for (int i=0; i<childCount; i++)
        {
            Object child = model.getChild(lastPathComponent, i);
            int grandChildCount = model.getChildCount(child);
            if (grandChildCount > 0)
            {
                class LocalTreePath extends TreePath
                {
                    private static final long serialVersionUID = 0;
                    public LocalTreePath(
                        TreePath parent, Object lastPathComponent)
                    {
                        super(parent, lastPathComponent);
                    }
                }
                TreePath nextTreePath = new LocalTreePath(treePath, child);
                expandAllRecursively(tree, nextTreePath);
            }
        }
    }
    
    /**
     * Count the number of nodes in the given tree model
     * 
     * @param treeModel The tree model
     * @return The number of nodes
     */
    public static int countNodes(TreeModel treeModel)
    {
        return countNodes(treeModel, treeModel.getRoot());
    }
    
    /**
     * Recursively count the number of nodes in the given tree model,
     * starting with the given node
     * 
     * @param treeModel The tree model
     * @param node The node
     * @return The number of nodes
     */
    private static int countNodes(TreeModel treeModel, Object node)
    {
        int sum = 1;
        int n = treeModel.getChildCount(node);
        for (int i=0; i<n; i++)
        {
            sum += countNodes(treeModel, treeModel.getChild(node, i));
        }
        return sum;
    }
    
    
    /**
     * Returns the first node with the given user object in the tree with
     * the given model. This assumes that the user object is stored 
     * in a DefaultMutableTreeNode. 
     * Returns <code>null</code> if no matching node is found.
     * 
     * @param treeModel The tree model
     * @param userObject The user object
     * @return The node with the given user object, or <code>null</code>
     */
    public static DefaultMutableTreeNode findNode(
        TreeModel treeModel, Object userObject)
    {
        return findNode(treeModel, treeModel.getRoot(), userObject);
    }
    
    /**
     * Returns the node with the given user object in the tree that is
     * rooted at the given node in the given model. This assumes that 
     * the user object is stored in a DefaultMutableTreeNode. 
     * Returns <code>null</code> if no matching node is found.
     * 
     * @param treeModel The tree model
     * @param node The root node
     * @param userObject The user object
     * @return The node with the given user object, or <code>null</code>
     */
    private static DefaultMutableTreeNode findNode(
        TreeModel treeModel, Object node, Object userObject)
    {
        if (node instanceof DefaultMutableTreeNode)
        {
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)node;
            Object object = treeNode.getUserObject();
            if ((object == null && userObject == null) ||
                (object != null && object.equals(userObject)))
            {
                return treeNode;
            }
        }
        int n = treeModel.getChildCount(node);
        for (int i=0; i<n; i++)
        {
            Object child = treeModel.getChild(node, i);
            DefaultMutableTreeNode result = 
                findNode(treeModel, child, userObject);
            if (result != null)
            {
                return result;
            }
        }
        return null;
    }
    
    /**
     * Returns the children of the given node in the given tree model
     * 
     * @param treeModel The tree model
     * @param node The node
     * @return The children
     */
    private static List<Object> getChildren(TreeModel treeModel, Object node)
    {
        List<Object> children = new ArrayList<Object>();
        int n = treeModel.getChildCount(node);
        for (int i=0; i<n; i++)
        {
            Object child = treeModel.getChild(node, i);
            children.add(child);
        }
        return children;
    }
    
    /**
     * Returns the parent of the given node in the given tree model.
     * This parent may be <code>null</code>, if the given node is
     * the root node (or not contained in the tree model at all).
     * 
     * @param treeModel The tree model
     * @param node The node
     * @return The parent
     */
    public static Object getParent(TreeModel treeModel, Object node)
    {
        return getParent(treeModel, node, treeModel.getRoot());
    }

    /**
     * Returns the parent of the given node in the given tree model.
     * This parent may be <code>null</code>, if the given node is
     * the root node (or not contained in the tree model at all).
     * 
     * @param treeModel The tree model
     * @param node The node
     * @param potentialParent The potential parent
     * @return The parent
     */
    private static Object getParent(
        TreeModel treeModel, Object node, Object potentialParent)
    {
        List<Object> children = getChildren(treeModel, potentialParent);
        for (Object child : children)
        {
            if (child == node)
            {
                return potentialParent;
            }
            Object parent = getParent(treeModel, node, child);
            if (parent != null)
            {
                return parent;
            }
        }
        return null;
    }

    /**
     * Return all nodes of the given tree model
     * 
     * @param treeModel The tree model
     * @return The nodes
     */
    public static List<Object> getAllNodes(TreeModel treeModel)
    {
        List<Object> result = new ArrayList<Object>();
        getAllDescendants(treeModel, treeModel.getRoot(), result);
        result.add(0, treeModel.getRoot());
        return result;
    }
    
    /**
     * Return all descendants of the given node in the given tree model
     * (not including the given node!)
     * 
     * @param treeModel The tree model
     * @param node The node
     * @return The descendants
     */
    public static List<Object> getAllDescendants(
        TreeModel treeModel, Object node)
    {
        List<Object> result = new ArrayList<Object>();
        getAllDescendants(treeModel, node, result);
        result.remove(node);
        return result;
    }
    
    /**
     * Compute all descendants of the given node in the given tree model
     * 
     * @param treeModel The tree model
     * @param node The node 
     * @param result The descendants
     */
    private static void getAllDescendants(
        TreeModel treeModel, Object node, List<Object> result)
    {
        if (node == null)
        {
            return;
        }
        result.add(node);
        List<Object> children = getChildren(treeModel, node);
        for (Object child : children)
        {
            getAllDescendants(treeModel, child, result);
        }
    }
    
    /**
     * Returns the tree path from the given node to the root in the
     * given tree model
     * 
     * @param treeModel The tree model
     * @param node The node
     * @return The tree path
     */
    public static TreePath createTreePathToRoot(
        TreeModel treeModel, Object node)
    {
        List<Object> nodes = new ArrayList<Object>();
        nodes.add(node);
        Object current = node;
        while (true)
        {
            Object parent = getParent(treeModel, current);
            if (parent == null)
            {
                break;
            }
            nodes.add(0, parent);
            current = parent;
        }
        TreePath treePath = new TreePath(nodes.toArray());
        return treePath;
    }
    
    
    /**
     * Translates one TreePath to a new TreeModel. This methods assumes 
     * DefaultMutableTreeNodes, and identifies the path based on the
     * user objects.
     * 
     * @param newTreeModel The new tree model
     * @param oldPath The old tree path
     * @return The new tree path, or <code>null</code> if there is no
     * corresponding path in the new tree model
     */
    public static TreePath translatePath(
        TreeModel newTreeModel, TreePath oldPath)
    {
        Object newRoot = newTreeModel.getRoot();
        List<Object> newPath = new ArrayList<Object>();
        newPath.add(newRoot);
        Object newPreviousElement = newRoot;
        for (int i=1; i<oldPath.getPathCount(); i++)
        {
            Object oldElement = oldPath.getPathComponent(i);
            DefaultMutableTreeNode oldElementNode = 
                (DefaultMutableTreeNode)oldElement;
            Object oldUserObject = oldElementNode.getUserObject();
            
            Object newElement = getChildWith(newPreviousElement, oldUserObject);
            if (newElement == null)
            {
                return null;
            }
            newPath.add(newElement);
            newPreviousElement = newElement;
        }
        return new TreePath(newPath.toArray());
    }
    
    
    /**
     * Returns the child with the given user object in the given tree
     * node. Assumes DefaultMutableTreeNodes.
     * 
     * @param node The node
     * @param userObject The user object
     * @return The child with the given user object, or <code>null</code>
     * if no such child can be found.
     */
    private static Object getChildWith(Object node, Object userObject)
    {
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)node;
        for (int j=0; j<treeNode.getChildCount(); j++)
        {
            TreeNode child = treeNode.getChildAt(j);
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)child;
            Object childUserObject = childNode.getUserObject();
            if (Objects.equals(userObject, childUserObject))
            {
                return child;
            }
        }
        return null;
    }
        
    
    /**
     * Private constructor to prevent instantiation
     */
    private JTrees()
    {
        // Private constructor to prevent instantiation
    }


    
//    /**
//     * Returns the path from the given node to the root in the
//     * given tree model
//     * 
//     * @param model The tree model
//     * @param node The node
//     * @return The path
//     */
//    static TreeNode[] getPathToRoot(TreeModel model, TreeNode node) 
//    {
//        return getPathToRoot(model, node, 0);
//    }
//
//    /**
//     * Returns the path from the given node to the root in the
//     * given tree model
//     * 
//     * @param model The tree model
//     * @param node The node
//     * @param depth The depth
//     * @return The path
//     */
//    private static TreeNode[] getPathToRoot(
//        TreeModel model, TreeNode node, int depth)
//    {
//        TreeNode resultNodes[];
//        if(node == null) 
//        {
//            if(depth == 0)
//            {
//                return null;
//            }
//            resultNodes = new TreeNode[depth];
//        }
//        else 
//        {
//            depth++;
//            if(node == model.getRoot())
//            {
//                resultNodes = new TreeNode[depth];
//            }
//            else
//            {
//                resultNodes = getPathToRoot(model, node.getParent(), depth);
//            }    
//            resultNodes[resultNodes.length - depth] = node;
//        }
//        return resultNodes;
//    }
//    
//    /**
//     * Returns the node with the given user object in the tree that is
//     * rooted at the given node. Assumes that the user object is stored
//     * in a DefaultMutableTreeNode, and the tree consists of 
//     * MutableTreeNodes. Returns <code>null</code> if no matching node
//     * is found.
//     * 
//     * @param node The root node
//     * @param userObject The user object
//     * @return The node with the given user object, or <code>null</code>
//     */
//    static MutableTreeNode findNode(
//        Object node, Object userObject)
//    {
//        if (node instanceof DefaultMutableTreeNode)
//        {
//            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)node;
//            if (treeNode.getUserObject().equals(userObject))
//            {
//                return treeNode;
//            }
//        }
//        if (node instanceof MutableTreeNode)
//        {
//            MutableTreeNode treeNode = (MutableTreeNode)node;
//            for (int i=0; i<treeNode.getChildCount(); i++)
//            {
//                TreeNode child = treeNode.getChildAt(i);
//                MutableTreeNode result = findNode(child, userObject);
//                if (result != null)
//                {
//                    return result;
//                }
//            }
//        }
//        return null;
//    }
//    
    
    
//    /**
//     * Returns all leaf nodes in the given tree model
//     * 
//     * @param treeModel The tree model
//     * @return All leaf nodes
//     */
//    private static List<Object> getLeafNodes(TreeModel treeModel)
//    {
//        List<Object> allNodes = 
//            getAllDescendants(treeModel, treeModel.getRoot());
//        List<Object> leafNodes = new ArrayList<Object>();
//        for (Object node : allNodes)
//        {
//            int n = treeModel.getChildCount(node);
//            if (n == 0)
//            {
//                leafNodes.add(node);
//            }
//        }
//        return leafNodes;
//    }
    
}
