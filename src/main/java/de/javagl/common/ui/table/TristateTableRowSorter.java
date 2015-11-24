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
package de.javagl.common.ui.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SortOrder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * Implementation of a TableRowSorter that does not only toggle between
 * ASCENDING and DESCENDING, but between ASCENDING, DESCENDING and 
 * "unsorted"
 *
 * @param <M> The model type
 */
public final class TristateTableRowSorter<M extends TableModel> 
    extends TableRowSorter<M>
{
    /**
     * Create a new sorter for the given model
     * 
     * @param tableModel The model
     */
    public TristateTableRowSorter(M tableModel)
    {
        super(tableModel);
    }

    @Override
    public void toggleSortOrder(int column)
    {
        if (isSortable(column))
        {
            List<SortKey> keys = new ArrayList<SortKey>(getSortKeys());
            int sortIndex = -1;
            for (sortIndex = keys.size() - 1; sortIndex >= 0; sortIndex--)
            {
                if (keys.get(sortIndex).getColumn() == column)
                {
                    break;
                }
            }
            if (sortIndex == -1)
            {
                keys.add(0, new SortKey(column, SortOrder.ASCENDING));
            }
            else if (sortIndex == 0)
            {
                SortKey key = keys.get(0);
                if (key.getSortOrder() == SortOrder.DESCENDING)
                {
                    keys.remove(0);
                }
                else
                {
                    keys.set(0, toggle(keys.get(0)));
                }
            }
            else
            {
                keys.remove(sortIndex);
                keys.add(0, new SortKey(column, SortOrder.ASCENDING));
            }
            if (keys.size() > getMaxSortKeys())
            {
                keys = keys.subList(0, getMaxSortKeys());
            }
            setSortKeys(keys);
        }
    }

    /**
     * Toggle the given key between ASCENDING and DESCENDING
     * 
     * @param key The key
     * @return The new key
     */
    private SortKey toggle(SortKey key)
    {
        if (key.getSortOrder() == SortOrder.ASCENDING)
        {
            return new SortKey(key.getColumn(), SortOrder.DESCENDING);
        }
        return new SortKey(key.getColumn(), SortOrder.ASCENDING);
    }

}