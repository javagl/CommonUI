/*
 * www.javagl.de - Common - UI
 *
 * Copyright (c) 2013-2016 Marco Hutter - http://www.javagl.de
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
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * A tree node that models another tree node in a {@link FilteredTreeModel}
 */
class FilteredTreeNode extends DefaultMutableTreeNode
{
    /**
     * Serial UID
     */
    private static final long serialVersionUID = -8766897308902463690L;

    /**
     * The {@link FilteredTreeModel} to which this node belongs
     */
    private final FilteredTreeModel filteredTreeModel;
    
    /**
     * The delegate node that is represented by this node
     */
    private final TreeNode delegateNode;
    
    /**
     * The FilteredTreeNodes that correspond to the children of the
     * delegate node that passed the filter
     */
    private Vector<TreeNode> filteredChildren;

    /**
     * Default constructor
     * 
     * @param filteredTreeModel The {@link FilteredTreeModel} owning this node
     * @param delegateNode The delegate node
     */
    FilteredTreeNode(
        FilteredTreeModel filteredTreeModel, TreeNode delegateNode)
    {
        this.filteredTreeModel = filteredTreeModel;
        this.delegateNode = delegateNode;
        this.filteredChildren = null;
    }

    @Override
    public boolean isLeaf()
    {
        return delegateNode.isLeaf();
    }

    @Override
    public Object getUserObject()
    {
        DefaultMutableTreeNode delegateNode = 
            filteredTreeModel.getDelegateNode(this);
        return delegateNode.getUserObject();
    }

    @Override
    public void setUserObject(Object userObject)
    {
        DefaultMutableTreeNode delegateNode = 
            filteredTreeModel.getDelegateNode(this);
        delegateNode.setUserObject(userObject);
    }

    @Override
    public Object[] getUserObjectPath()
    {
        DefaultMutableTreeNode delegateNode = 
            filteredTreeModel.getDelegateNode(this);
        Object delegateResult[] = delegateNode.getUserObjectPath();
        
        Object result[] = new Object[delegateResult.length];
        for (int i=0; i<delegateResult.length; i++)
        {
            result[i] = filteredTreeModel.getFilteredNode(delegateResult[i]);
        }
        return super.getUserObjectPath();
    }

    @Override
    public TreeNode getParent()
    {
        return filteredTreeModel.getFilteredNode(delegateNode.getParent());
    }

    /**
     * Returns the children of this node. These are the {@link FilteredTreeNode}
     * instances that correspond to the children of the delegate node.
     * 
     * @return The filtered children
     */
    private Vector<TreeNode> getFilteredChildren()
    {
        if (filteredChildren == null)
        {
            @SuppressWarnings("unchecked")
            Enumeration<? extends TreeNode> enumeration = 
                delegateNode.children();
            Stream<TreeNode> stream = enumerationAsStream(enumeration);
            filteredChildren = 
                filteredTreeModel.getFiltered(stream)
                    .collect(Collectors.toCollection(Vector::new));
        }
        return filteredChildren;
    }
    
    /**
     * Notify this node and all its children that the filter criterion
     * changes, and the {@link #filteredChildren} have to be recomputed
     */
    void notifyFilterChanged()
    {
        for (TreeNode child : getFilteredChildren())
        {
            ((FilteredTreeNode)child).notifyFilterChanged();
        }
        filteredChildren = null;
    }
    

    @Override
    public int getIndex(TreeNode node)
    {
        return getFilteredChildren().indexOf(node);
    }

    @Override
    public int getChildCount()
    {
        return getFilteredChildren().size();
    }

    @Override
    public TreeNode getChildAt(int childIndex)
    {
        return getFilteredChildren().get(childIndex);
    }

    @Override
    public boolean getAllowsChildren()
    {
        return delegateNode.getAllowsChildren();
    }

    @Override
    @SuppressWarnings({ "rawtypes" })
    public Enumeration children()
    {
        return getFilteredChildren().elements();
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

    
    /**
     * Returns a new stream that has the same contents as the given enumeration
     * 
     * @param e The enumeration
     * @return The stream
     */
    private static <T> Stream<T> enumerationAsStream(Enumeration<? extends T> e)
    {
        Iterator<T> iterator = new Iterator<T>()
        {
            @Override
            public T next()
            {
                return e.nextElement();
            }

            @Override
            public boolean hasNext()
            {
                return e.hasMoreElements();
            }
        };
        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(
                iterator, Spliterator.ORDERED), false);
    }
    
}