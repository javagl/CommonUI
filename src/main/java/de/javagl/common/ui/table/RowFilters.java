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
package de.javagl.common.ui.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.RowFilter;

/**
 * Methods related to row filters
 */
public class RowFilters
{
    /**
     * Combine the given filters, using an "and", if they are not 
     * <code>null</code>. If they are both <code>null</code>, then
     * the resulting filter will accept all elements.
     * 
     * @param <M> The model type
     * @param <I> The index type
     * 
     * @param filter0 The first filter
     * @param filter1 The second filter
     * @return The result
     */
    public static <M, I> RowFilter<M, I> and(
        RowFilter<M, I> filter0, 
        RowFilter<M, I> filter1)
    {
        if (filter0 != null && filter1 == null)
        {
            return filter0;
        }
        if (filter0 == null && filter1 != null)
        {
            return filter1;
        }
        List<RowFilter<M, I>> filters = new ArrayList<RowFilter<M, I>>();
        if (filter0 != null)
        {
            filters.add(filter0);
        }
        if (filter1 != null)
        {
            filters.add(filter1);
        }
        return RowFilter.andFilter(filters);
    }
    
    /**
     * Combine the given filters, using an "or", if they are not 
     * <code>null</code>. If they are both <code>null</code>, then
     * the resulting filter will accept no elements.
     * 
     * @param <M> The model type
     * @param <I> The index type
     * 
     * @param filter0 The first filter
     * @param filter1 The second filter
     * @return The result
     */
    public static <M, I> RowFilter<M, I> or(
        RowFilter<M, I> filter0, 
        RowFilter<M, I> filter1)
    {
        if (filter0 != null && filter1 == null)
        {
            return filter0;
        }
        if (filter0 == null && filter1 != null)
        {
            return filter1;
        }
        List<RowFilter<M, I>> filters = new ArrayList<RowFilter<M, I>>();
        if (filter0 != null)
        {
            filters.add(filter0);
        }
        if (filter1 != null)
        {
            filters.add(filter1);
        }
        return RowFilter.orFilter(filters);
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private RowFilters()
    {
        // Private constructor to prevent instantiation
    }

}
