/*
 * www.javagl.de - Common - UI
 *
 * Copyright (c) 2013-2019 Marco Hutter - http://www.javagl.de
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
package de.javagl.common.ui.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.ListSelectionModel;

/**
 * Methods related to <code>ListSelectionModel</code> instances 
 */
public class ListSelectionModels
{
    /**
     * Set the given indices as the selection in the given list selection model
     * 
     * @param selectionModel The list selection model
     * @param selectedIndices The selected indices
     */
    public static void setAsSelection(
        ListSelectionModel selectionModel, 
        Collection<? extends Integer> selectedIndices)
    {
        selectionModel.setValueIsAdjusting(true);
        selectionModel.clearSelection();
        for (Integer i : selectedIndices)
        {
            selectionModel.addSelectionInterval(i, i);
        }
        selectionModel.setValueIsAdjusting(false);
    }
    
    /**
     * Add the given indices to the selection in the given list selection model
     * 
     * @param selectionModel The list selection model
     * @param selectedIndices The selected indices
     */
    public static void addToSelection(
        ListSelectionModel selectionModel, 
        Collection<? extends Integer> selectedIndices)
    {
        selectionModel.setValueIsAdjusting(true);
        for (Integer i : selectedIndices)
        {
            selectionModel.addSelectionInterval(i, i);
        }
        selectionModel.setValueIsAdjusting(false);
    }

    /**
     * Remove the given indices from the selection in the given list 
     * selection model
     * 
     * @param selectionModel The list selection model
     * @param unselectedIndices The unselected indices
     */
    public static void removeFromSelection(
        ListSelectionModel selectionModel, 
        Collection<? extends Integer> unselectedIndices)
    {
        selectionModel.setValueIsAdjusting(true);
        for (Integer i : unselectedIndices)
        {
            selectionModel.removeSelectionInterval(i, i);
        }
        selectionModel.setValueIsAdjusting(false);
    }

    /**
     * Compute the list of all indices that are unselected in the given 
     * selection model
     * 
     * @param selectionModel The list selection model
     * @param size The size (number of indices) to assume
     * @return The unselected view row indices
     */
    public static List<Integer> computeUnselectedIndices(
        ListSelectionModel selectionModel, int size)
    {
        List<Integer> unselectedIndices = new ArrayList<Integer>();
        for (int i = 0; i < size; i++)
        {
            if (!selectionModel.isSelectedIndex(i))
            {
                unselectedIndices.add(i);
            }
        }
        return unselectedIndices;
    }
    
    /**
     * Compute the list of all indices that are selected in the given 
     * selection model
     * 
     * @param selectionModel The list selection model
     * @return The unselected view row indices
     */
    public static List<Integer> computeSelectedIndices(
        ListSelectionModel selectionModel)
    {
        List<Integer> selectedIndices = new ArrayList<Integer>();
        int min = selectionModel.getMinSelectionIndex();
        if (min == -1)
        {
            return selectedIndices;
        }
        int max = selectionModel.getMaxSelectionIndex();
        for (int i=min; i<=max; i++)
        {
            if (selectionModel.isSelectedIndex(i))
            {
                selectedIndices.add(i);
            }
        }
        return selectedIndices;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private ListSelectionModels()
    {
        // Private constructor to prevent instantiation
    }
}
