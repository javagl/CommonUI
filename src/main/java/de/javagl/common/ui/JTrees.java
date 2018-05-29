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

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import de.javagl.common.ui.tree.renderer.GenericTreeCellRenderer;

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
     * Collapse all rows of the given tree
     * 
     * @param tree The tree
     * @param omitRoot Whether the root node should not be collapsed
     */
    public static void collapseAll(JTree tree, boolean omitRoot)
    {
        int rows = tree.getRowCount();
        int limit = (omitRoot ? 1 : 0);
        for (int i = rows - 1; i >= limit; i--)
        {
            tree.collapseRow(i);
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
    public static List<Object> getChildren(TreeModel treeModel, Object node)
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
     * Returns a list containing all leaf nodes from the given tree model.
     * These are the nodes that have 0 children.
     * 
     * @param treeModel The tree model
     * @return The leaf nodes
     */
    public static List<Object> getLeafNodes(TreeModel treeModel)
    {
        return getLeafNodes(treeModel, treeModel.getRoot());
    }
    
    /**
     * Returns a list containing all leaf nodes from the given tree model
     * that are descendants of the given node. These are the nodes that 
     * have 0 children.
     * 
     * @param treeModel The tree model
     * @param node The node to start the search from
     * @return The leaf nodes
     */
    public static List<Object> getLeafNodes(TreeModel treeModel, Object node)
    {
        List<Object> leafNodes = new ArrayList<Object>();
        getLeafNodes(treeModel, node, leafNodes);
        return leafNodes;
    }
    
    /**
     * Recursively collect all leaf nodes in the given tree model that are
     * descendants of the given node.
     * 
     * @param treeModel The tree model
     * @param node The node to start the search from
     * @param leafNodes The leaf nodes
     */
    private static void getLeafNodes(
        TreeModel treeModel, Object node, Collection<Object> leafNodes)
    {
        if (node == null)
        {
            return;
        }
        int childCount = treeModel.getChildCount(node);
        if (childCount == 0)
        {
            leafNodes.add(node);
        }
        else
        {
            for (int i = 0; i < childCount; i++)
            {
                Object child = treeModel.getChild(node, i);
                getLeafNodes(treeModel, child, leafNodes);
            }
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
     * Compute the list of all tree paths in the given tree that are currently
     * expanded
     * 
     * @param tree The tree
     * @return The expanded paths
     */
    public static List<TreePath> computeExpandedPaths(JTree tree)
    {
        List<TreePath> treePaths = new ArrayList<TreePath>();
        int rows = tree.getRowCount();
        for (int i = 0; i < rows; i++)
        {
            TreePath treePath = tree.getPathForRow(i);
            treePaths.add(treePath);
        }
        return treePaths;
    }
    
    
    /**
     * Translates one TreePath to a new TreeModel. This methods assumes 
     * DefaultMutableTreeNodes.
     * 
     * @param newTreeModel The new tree model
     * @param oldPath The old tree path
     * @return The new tree path, or <code>null</code> if there is no
     * corresponding path in the new tree model
     */
    public static TreePath translatePath(
        TreeModel newTreeModel, TreePath oldPath)
    {
        return translatePath(newTreeModel, oldPath, Objects::equals);
    }
    
    /**
     * Translates one TreePath to a new TreeModel. This methods assumes 
     * DefaultMutableTreeNodes, and identifies the path based on the
     * equality of user objects using the given equality predicate.
     * 
     * @param newTreeModel The new tree model
     * @param oldPath The old tree path
     * @param equality The equality predicate 
     * @return The new tree path, or <code>null</code> if there is no
     * corresponding path in the new tree model
     */
    public static TreePath translatePath(
        TreeModel newTreeModel, TreePath oldPath, 
        BiPredicate<Object, Object> equality)
    {
        Object newRoot = newTreeModel.getRoot();
        List<Object> newPath = new ArrayList<Object>();
        newPath.add(newRoot);
        Object newPreviousElement = newRoot;
        for (int i=1; i<oldPath.getPathCount(); i++)
        {
            Object oldElement = oldPath.getPathComponent(i);
            Object oldUserObject = getUserObjectFromTreeNode(oldElement);
            Object newElement = 
                getChildWith(newPreviousElement, oldUserObject, equality);
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
     * Returns the child of the given tree node that has a user object that
     * is equal to the given one, based on the given equality predicate.
     * Assumes DefaultMutableTreeNodes.
     * 
     * @param node The node
     * @param userObject The user object
     * @param equality The equality predicate 
     * @return The child with the given user object, or <code>null</code>
     * if no such child can be found.
     */
    private static Object getChildWith(Object node, Object userObject, 
        BiPredicate<Object, Object> equality)
    {
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)node;
        for (int j=0; j<treeNode.getChildCount(); j++)
        {
            TreeNode child = treeNode.getChildAt(j);
            Object childUserObject = getUserObjectFromTreeNode(child);
            if (equality.test(userObject, childUserObject))
            {
                return child;
            }
        }
        return null;
    }
    
    /**
     * Returns the user object from the last path component of the given tree 
     * path. If the given path is <code>null</code>, then <code>null</code>
     * is returned. If the last path component is not a DefaultMutableTreeNode,
     * then <code>null</code> is returned.
     * 
     * @param treePath The tree path
     * @return The user object
     */
    public static Object getUserObjectFromTreePath(TreePath treePath)
    {
        if (treePath == null)
        {
            return null;
        }
        Object lastPathComponent = treePath.getLastPathComponent();
        return getUserObjectFromTreeNode(lastPathComponent);
    }
    
    /**
     * Returns the user object from the given tree node. If the given node 
     * object is <code>null</code> or not a DefaultMutableTreeNode,
     * then <code>null</code> is returned.
     * 
     * @param nodeObject The node object
     * @return The user object
     */
    public static Object getUserObjectFromTreeNode(Object nodeObject)
    {
        if (nodeObject == null)
        {
            return null;
        }
        if (nodeObject instanceof DefaultMutableTreeNode)
        {
            DefaultMutableTreeNode node = 
                (DefaultMutableTreeNode)nodeObject;
            Object userObject = node.getUserObject();
            return userObject;
        }
        return null;
    }
    
    /**
     * Computes the index that the given node has in its parent node. 
     * Returns -1 if the given node does not have a parent, or the
     * node is not a DefaultMutableTreeNode.
     * 
     * @param nodeObject The node
     * @return The index of the node in its parent
     */
    public static int computeIndexInParent(Object nodeObject)
    {
        if (nodeObject instanceof DefaultMutableTreeNode)
        {
            DefaultMutableTreeNode node = 
                (DefaultMutableTreeNode)nodeObject;
            TreeNode parent = node.getParent();
            if (parent == null)
            {
                return -1;
            }
            int childCount = parent.getChildCount();
            for (int i=0; i<childCount; i++)
            {
                TreeNode child = parent.getChildAt(i);
                if (child == nodeObject)
                {
                    return i;
                }
            }
        }
        return -1;
    }
        
    
    /**
     * Apply a cell renderer to the given tree that will create cells that
     * consist of a button and a label, based on 
     * a {@link GenericTreeCellRenderer}.<br>
     * <br>
     * An editor will be installed for the tree, making the buttons 
     * clickable.<br>
     * <br>
     * Some details about the exact layout of the cells are intentionally
     * not specified. 
     * 
     * @param tree The tree
     * @param buttonFactory The factory that will receive the tree node,
     * and return the JButton (to which listeners may already have been
     * attached). If this function returns <code>null</code>, then no
     * button will be inserted.
     * @param textFactory The factory that will receive the tree node,
     * and return the text that should be displayed as the node label.
     */
    public static void applyButtonTreeCellRenderer(JTree tree, 
        Function<Object, JButton> buttonFactory,
        Function<Object, String> textFactory)
    {
        TreeCellRenderer treeCellrenderer = new GenericTreeCellRenderer()
        {
            @Override
            protected void prepare(Object nodeObject, JPanel container)
            {
                container.setLayout(new BorderLayout(3, 0));
                
                JLabel textLabel = new JLabel();
                String text = textFactory.apply(nodeObject); 
                textLabel.setText(text);
                container.add(textLabel, BorderLayout.CENTER);
                
                JButton button = buttonFactory.apply(nodeObject);
                if (button != null)
                {
                    container.add(button, BorderLayout.WEST);
                }
            }
        };
        tree.setCellRenderer(treeCellrenderer);
        tree.setEditable(true);
        DefaultCellEditor editor = new DefaultCellEditor(new JTextField())
        {
            /**
             * Serial UID 
             */
            private static final long serialVersionUID = 1L;
            
            @Override
            public Component getTreeCellEditorComponent(JTree tree, Object value,
                    boolean selected, boolean expanded, boolean leaf, int row) 
            {
                return treeCellrenderer.getTreeCellRendererComponent(
                    tree, value, selected, expanded, leaf, row, true);
            }
            @Override
            public boolean isCellEditable(EventObject event) 
            {
                return true;
            }
        };
        tree.setCellEditor(editor);
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private JTrees()
    {
        // Private constructor to prevent instantiation
    }

}
