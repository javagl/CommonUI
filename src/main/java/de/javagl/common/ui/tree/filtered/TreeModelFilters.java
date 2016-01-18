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
 * Utility methods for creating {@link TreeModelFilter} instances
 */
public class TreeModelFilters
{
    /**
     * Returns a {@link TreeModelFilter} that is accepting all nodes.
     * 
     * @return The new {@link TreeModelFilter}
     */
    public static TreeModelFilter acceptingAll()
    {
        return new TreeModelFilter()
        {
            @Override
            public boolean acceptNode(TreeModel treeModel, TreeNode node)
            {
                return true;
            }
            
            @Override
            public String toString()
            {
                return "TreeModelFilter[acceptingAll]";
            }
        };
    }

    
    /**
     * Returns a {@link TreeModelFilter} that is accepting all leaf nodes
     * whose string representation contains the given string (ignoring 
     * upper/lower case), and all ancestors of these nodes 
     * 
     * @param string The string that must be contained in the node string
     * @return The new {@link TreeModelFilter}
     */
    public static TreeModelFilter containsLeafContainingStringIgnoreCase(
        final String string)
    {
        return new TreeModelFilter()
        {
            @Override
            public boolean acceptNode(TreeModel treeModel, TreeNode node)
            {
                if (node.isLeaf())
                {
                    if (String.valueOf(node).toLowerCase().contains(
                        string.toLowerCase())) 
                    {
                        return true;
                    }
                }
                for (int i=0; i<node.getChildCount(); i++)
                {
                    if (acceptNode(treeModel, node.getChildAt(i)))
                    {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String toString()
            {
                return "TreeModelFilter[" +
                    "containsLeafContainingStringIgnoreCase("+string+")]";
            }
        };        
    }
    
    /**
     * Returns a {@link TreeModelFilter} that is accepting all nodes
     * whose string representation contains the given string (ignoring 
     * upper/lower case), and all ancestors of these nodes 
     * 
     * @param string The string that must be contained in the node string
     * @return The new {@link TreeModelFilter}
     */
    public static TreeModelFilter containsStringIgnoreCase(
        final String string)
    {
        return new TreeModelFilter()
        {
            @Override
            public boolean acceptNode(TreeModel treeModel, TreeNode node)
            {
                if (String.valueOf(node).toLowerCase().contains(
                    string.toLowerCase())) 
                {
                    return true;
                }
                for (int i=0; i<node.getChildCount(); i++)
                {
                    if (acceptNode(treeModel, node.getChildAt(i)))
                    {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String toString()
            {
                return "TreeModelFilter[" +
                    "containsLeafContainingStringIgnoreCase("+string+")]";
            }
        };        
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private TreeModelFilters()
    {
        // Private constructor to prevent instantiation
    }
 
    
//    /**
//     * Returns a {@link TreeModelFilter} that is accepting all nodes
//     * whose string representation contains the given string, or 
//     * which contain descendants whose string representation contains
//     * the given string.
//     * 
//     * @param string The string that must be contained in the node string
//     * @return The new {@link TreeModelFilter}
//     */
//    public static TreeModelFilter containsStringRecursive(
//        final String string)
//    {
//        return new TreeModelFilter()
//        {
//            @Override
//            public boolean acceptNode(TreeModel treeModel, TreeNode node)
//            {
//                if (String.valueOf(node).contains(string)) 
//                {
//                    return true;
//                }
//                for (int i=0; i<node.getChildCount(); i++)
//                {
//                    if (acceptNode(treeModel, node.getChildAt(i)))
//                    {
//                        return true;
//                    }
//                }
//                return false;
//            }
//            
//            @Override
//            public String toString()
//            {
//                return "TreeModelFilter[containsStringRecursive("+string+")]";
//            }
//        };        
//    }
    
    
}