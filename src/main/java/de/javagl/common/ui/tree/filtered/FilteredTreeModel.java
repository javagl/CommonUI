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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

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
    private FilteredTreeNode root;

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
     * Returns the delegate node for the given filtered node
     * 
     * @param filteredTreeNode The filtered node
     * @return The delegate node
     */
    DefaultMutableTreeNode getDelegateNode(FilteredTreeNode filteredTreeNode)
    {
        return (DefaultMutableTreeNode)thisToDelegate.get(filteredTreeNode);
    }
    
    /**
     * Returns the filtered node for the given delegate node
     * 
     * @param treeNode The delegate node
     * @return The filtered node
     */
    FilteredTreeNode getFilteredNode(Object treeNode)
    {
        return (FilteredTreeNode)delegateToThis.get(treeNode);
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
    private FilteredTreeNode createNode(final TreeNode delegateNode)
    {
        FilteredTreeNode node = new FilteredTreeNode(this, delegateNode);
        delegateToThis.put(delegateNode, node);
        thisToDelegate.put(node, delegateNode);

        @SuppressWarnings("unchecked")
        Enumeration<? extends TreeNode> delegateChildren = 
        delegateNode.children();
        while (delegateChildren.hasMoreElements())
        {
            TreeNode delegateChild = delegateChildren.nextElement();
            createNode(delegateChild);
        }
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
        if (root != null)
        {
            root.notifyFilterChanged();
        }
        fireTreeStructureChanged(this, new TreeNode[] { root }, null, null);
    }
    
    
    /**
     * Returns the stream of nodes that results from filtering the given
     * stream with the current {@link TreeModelFilter} and mapping them
     * to the filtered nodes
     * 
     * @param delegateNodes The delegate nodes
     * @return The stream of filtered nodes 
     */
    Stream<TreeNode> getFiltered(Stream<TreeNode> delegateNodes)
    {
        return delegateNodes
            .filter(delegateNode -> filter.acceptNode(this, delegateNode))
            .map(delegateNode -> delegateToThis.get(delegateNode))
            .filter(filteredNode -> filteredNode != null);
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