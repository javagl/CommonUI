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

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

/**
 * Interface for a filter of a tree model. Instances of classes implementing
 * this interface may be passed to the 
 * {@link FilteredTreeModel#setFilter(TreeModelFilter)} method, to cause
 * the tree model to be filtered accordingly. 
 */
public interface TreeModelFilter
{
    /**
     * Returns whether the given node should be contained in
     * the filtered tree model.
     * 
     * @param treeModel The filtered tree model
     * @param node The node which is to be checked
     * @return Whether the given node should be contained in the
     * given filtered model
     */
    boolean acceptNode(TreeModel treeModel, TreeNode node);
}