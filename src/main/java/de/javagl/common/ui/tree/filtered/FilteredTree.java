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
import java.util.HashSet;
import java.util.Set;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import de.javagl.common.ui.JTrees;

/**
 * Utility class for maintaining a filtered JTree and its model.
 * This class is not necessary for the actual functionality of
 * filtering the tree, but only maintains the expansion state 
 * of the tree paths when the filter for the tree is enabled or
 * disabled. 
 */
public class FilteredTree
{
    /**
     * Creates a new filtered tree for the given (unfiltered!) model.
     * 
     * @param treeModel The tree model
     * @return The new filtered tree
     */
    public static FilteredTree create(TreeModel treeModel)
    {
        return new FilteredTree(treeModel);
    }
    
    /**
     * The actual JTree
     */
    private final JTree tree;
    
    /**
     * The {@link FilteredTreeModel} for the JTree
     */
    private FilteredTreeModel treeModel;
    
    /**
     * The set of tree paths that are expanded in the unfiltered state
     */
    private final Set<TreePath> expandedPaths = new HashSet<TreePath>();

    /**
     * The TreeExpansionListener that maintains the expanded paths.
     * Then the tree is NOT filtered, then this listener will be
     * attached to the tree and store the expansion state of all
     * paths. The stored paths will be used to restore the expansion
     * state when filtering is switched off.  
     */
    private final TreeExpansionListener expandedPathsListener = 
        new TreeExpansionListener()
    {
        @Override
        public void treeExpanded(TreeExpansionEvent event)
        {
            expandedPaths.add(event.getPath());
        }
        
        @Override
        public void treeCollapsed(TreeExpansionEvent event)
        {
            expandedPaths.remove(event.getPath());
        }
    };

    /**
     * Creates a new filtered tree for the given (unfiltered!) model.
     * 
     * @param delegateTreeModel The tree model
     */
    private FilteredTree(TreeModel delegateTreeModel)
    {
        this.treeModel = new FilteredTreeModel(delegateTreeModel);
        this.tree = new JTree(treeModel);
        tree.addTreeExpansionListener(expandedPathsListener);
        
    }
    
    /**
     * Returns the tree
     * 
     * @return The tree
     */
    public JTree getTree()
    {
        return tree;
    }
    
    /**
     * Returns the (filtered) model of the tree
     * 
     * @return The model of the tree
     */
    public TreeModel getFilteredModel()
    {
        return treeModel;
    }
    
    /**
     * Set the (unfiltered) model for the tree
     * 
     * @param model The model for the tree
     */
    public void setInputModel(TreeModel model)
    {
        Enumeration<TreePath> expandedDescendants = 
            this.tree.getExpandedDescendants(new TreePath(treeModel.getRoot()));
        
        this.treeModel = new FilteredTreeModel(model);
        this.tree.setModel(treeModel);
        
        if (expandedDescendants != null)
        {
            while (expandedDescendants.hasMoreElements())
            {
                TreePath expanded = expandedDescendants.nextElement();
                TreePath translatedPath = 
                    JTrees.translatePath(treeModel, expanded);
                if (translatedPath != null)
                {
                    this.tree.expandPath(translatedPath);
                }
            }
        }
    }
    
    
    
    
    
    /**
     * Set the filter of the model for the tree. All nodes of the
     * filtered tree will be expanded. If the given filter is 
     * <code>null</code>, then all nodes will be displayed and
     * the last expansion state of the unfiltered tree will be
     * restored.
     *  
     * @param filter The {@link TreeModelFilter}
     */
    public void setFilter(TreeModelFilter filter)
    {
        if (filter == null)
        {
            treeModel.setFilter(filter);
            for (TreePath path : expandedPaths)
            {
                tree.expandPath(path);
            }
        }
        else
        {
            tree.removeTreeExpansionListener(expandedPathsListener);
            treeModel.setFilter(filter);
            int row = 0;
            while (row < tree.getRowCount())
            {
                tree.expandRow(row);
                row++;
            }
            tree.addTreeExpansionListener(expandedPathsListener);
        }
    }
}