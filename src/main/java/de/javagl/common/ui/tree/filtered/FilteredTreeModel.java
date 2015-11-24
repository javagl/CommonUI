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

package de.javagl.common.ui.tree.filtered;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/*
 * NOTE: This model is a first step towards a general, filtered tree model,
 * but may not yet be used as a completely transparent, general-purpose drop-in
 * replacement for arbitrary tree models:
 * - The nodes that are created in this model should not extend
 *   DefaultMutableTreeNode, but nearly everybody assumes this class,    
 *   in order to store a user object. A future implementation should check   
 *   the type of the delegate node, and create an appropriate implementation   
 *   based on this type.
 * - Modifications of the backing model are not yet properly
 *   reflected in this model.
 * - Many other reasons, probably...     
 */


/**
 * Implementation of a TreeModel that can be filtered with a
 * {@link TreeModelFilter}
 */
public class FilteredTreeModel implements TreeModel 
{
    /**
     * The delegate tree model
     */
    private final TreeModel delegate;

    /**
     * The {@link TreeModelFilter} for this model
     */
    private TreeModelFilter filter = TreeModelFilters.acceptingAll();

    /**
     * The root node of this model
     */
    private TreeNode root;

    /**
     * The mapping from nodes of this model to the corresponding
     * nodes of the delegate model
     */
    private final Map<TreeNode, TreeNode> thisToDelegate;

    /**
     * The mapping from nodes of the delegate model to the corresponding
     * nodes of this model
     */
    private final Map<TreeNode, TreeNode> delegateToThis;

    /**
     * The listeners that are attached to this tree model
     */
    private final List<TreeModelListener> treeModelListeners;

    /**
     * Creates a new filtered tree model for the given delegate
     * 
     * @param delegate The delegate model
     */
    public FilteredTreeModel(TreeModel delegate)
    {
        this.delegate = delegate;
        
        treeModelListeners = new CopyOnWriteArrayList<TreeModelListener>();

        thisToDelegate = new HashMap<TreeNode, TreeNode>();
        delegateToThis = new HashMap<TreeNode, TreeNode>();
        
        init();
    }
    
    /**
     * Initialize this tree model
     */
    private void init()
    {
        TreeNode delegateRoot = null;
        if (delegate != null)
        {
            delegateRoot = (TreeNode) delegate.getRoot();
        }
        thisToDelegate.clear();
        delegateToThis.clear();
        if (delegateRoot == null)
        {
            root = null;
            return;
        }
        root = createNode(delegateRoot);
    }

    /**
     * Recursively create the node for the given delegate node and
     * its children.
     * 
     * @param delegateNode The delegate node
     * @return The filtered version of the node
     */
    private TreeNode createNode(final TreeNode delegateNode)
    {
        final List<TreeNode> childList = new ArrayList<TreeNode>();
        Enumeration<?> delegateChildren = delegateNode.children();
        while (delegateChildren.hasMoreElements())
        {
            Object object = delegateChildren.nextElement();
            TreeNode delegateChild = (TreeNode) object;
            TreeNode child = createNode(delegateChild);
            childList.add(child);
        }

        TreeNode node = new DefaultMutableTreeNode()
        {
            /**
             * Serial UID
             */
            private static final long serialVersionUID = -8766897308902463690L;

            @Override
            public boolean isLeaf()
            {
                return delegateNode.isLeaf();
            }
            
            @Override
            public Object getUserObject()
            {
                DefaultMutableTreeNode delegateNode = 
                    (DefaultMutableTreeNode)thisToDelegate.get(this);
                return delegateNode.getUserObject();
            }
            
            @Override
            public void setUserObject(Object userObject)
            {
                DefaultMutableTreeNode delegateNode = 
                    (DefaultMutableTreeNode)thisToDelegate.get(this);
                delegateNode.setUserObject(userObject);
            }

            @Override
            public Object[] getUserObjectPath()
            {
                DefaultMutableTreeNode delegateNode = 
                    (DefaultMutableTreeNode)thisToDelegate.get(this);
                Object delegateResult[] = delegateNode.getUserObjectPath();
                
                Object result[] = new Object[delegateResult.length];
                for (int i=0; i<delegateResult.length; i++)
                {
                    result[i] = delegateToThis.get(delegateResult[i]);
                }
                return super.getUserObjectPath();
            }
            
            @Override
            public TreeNode getParent()
            {
                return delegateToThis.get(delegateNode.getParent());
            }

            @Override
            public int getIndex(TreeNode node)
            {
                return getFiltered(childList).indexOf(node);
            }

            @Override
            public int getChildCount()
            {
                return getFiltered(childList).size();
            }

            @Override
            public TreeNode getChildAt(int childIndex)
            {
                return getFiltered(childList).get(childIndex);
            }

            @Override
            public boolean getAllowsChildren()
            {
                return delegateNode.getAllowsChildren();
            }

            @Override
            @SuppressWarnings({ "rawtypes", "unchecked" })
            public Enumeration children()
            {
                return new Vector(getFiltered(childList)).elements();
            }

            @Override
            public String toString()
            {
                return delegateNode.toString();
            }

            @Override
            public boolean equals(Object object)
            {
                if (object == this)
                {
                    return true;
                }
                return delegateNode.equals(object);
            }

            @Override
            public int hashCode()
            {
                return delegateNode.hashCode();
            }

        };
        delegateToThis.put(delegateNode, node);
        thisToDelegate.put(node, delegateNode);
        return node;
    }

    /**
     * Set the {@link TreeModelFilter} for this model. If the given
     * filter is <code>null</code>, then all nodes of the delegate
     * model will be shown. 
     * 
     * @param filter The filter to use
     */
    public void setFilter(TreeModelFilter filter)
    {
        this.filter = filter;
        if (this.filter == null)
        {
            this.filter = TreeModelFilters.acceptingAll();
        }
        fireTreeStructureChanged(this, new TreeNode[] { root }, null, null);
    }
    
    /**
     * Returns the result of filtering the given list of nodes 
     * with the current {@link TreeModelFilter}
     * 
     * @param list The input list
     * @return The filtered list
     */
    private List<TreeNode> getFiltered(List<TreeNode> list)
    {
        List<TreeNode> result = new ArrayList<TreeNode>();
        for (TreeNode node : list)
        {
            if (filter.acceptNode(this, node))
            {
                result.add(node);
            }
        }
        return result;
    }

    /**
     * Fires a treeStructureChanged event
     *  
     * @param source The source
     * @param path The tree paths
     * @param childIndices The child indices
     * @param children The children
     */
    protected void fireTreeStructureChanged(Object source, Object[] path,
        int[] childIndices, Object[] children)
    {
        for (TreeModelListener listener : treeModelListeners)
        {
            listener.treeStructureChanged(
                new TreeModelEvent(source, path, childIndices, children));
        }
    }    

    //=== Implementation of the TreeModel interface ===========================
    
    @Override
    public Object getRoot()
    {
        return root;
    }

    @Override
    public int getIndexOfChild(Object parent, Object child)
    {
        if (parent == null || child == null)
        {
            return -1;
        }
        return ((TreeNode) parent).getIndex((TreeNode) child);
    }

    @Override
    public Object getChild(Object parent, int index)
    {
        return ((TreeNode) parent).getChildAt(index);
    }

    @Override
    public int getChildCount(Object parent)
    {
        return ((TreeNode) parent).getChildCount();
    }

    @Override
    public boolean isLeaf(Object node)
    {
        return ((TreeNode) node).isLeaf();
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue)
    {
        MutableTreeNode aNode = (MutableTreeNode) path.getLastPathComponent();
        aNode.setUserObject(newValue);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l)
    {
        treeModelListeners.add(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l)
    {
        treeModelListeners.remove(l);
    }

}