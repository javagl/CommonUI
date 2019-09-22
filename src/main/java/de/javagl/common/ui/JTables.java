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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * Utility methods related to tables
 */
public class JTables
{
    /**
     * Adjust the preferred widths of the columns of the given table
     * depending on the contents of the cells and headers.
     * 
     * @param table The table to adjust
     * @param maxWidth The maximum width a column may have
     */
    public static void adjustColumnWidths(JTable table, int maxWidth)
    {
        final int safety = 20;
        for (int c = 0; c < table.getColumnCount(); c++)
        {
            TableColumn column = table.getColumnModel().getColumn(c);

            TableCellRenderer headerRenderer = column.getHeaderRenderer();
            if (headerRenderer == null) {
                headerRenderer = table.getTableHeader().getDefaultRenderer();
            }
            Component headerComponent = 
                headerRenderer.getTableCellRendererComponent(
                    table, column.getHeaderValue(), false, false, 0, 0);
            int width = headerComponent.getPreferredSize().width;

            for (int r = 0; r < table.getRowCount(); r++) 
            {
                TableCellRenderer cellRenderer = table.getCellRenderer(r, c);
                Component cellComponent = 
                    cellRenderer.getTableCellRendererComponent(
                        table, table.getValueAt(r, c), 
                        false, false, r, c);
                Dimension d = cellComponent.getPreferredSize();
                //System.out.println(
                //    "Preferred is "+d.width+" for "+cellComponent);
                width = Math.max(width, d.width);
            }
            column.setPreferredWidth(Math.min(maxWidth, width + safety));
        }
    }    
    
    /**
     * Scroll the given table so that the specified row is visible.
     * 
     * @param table The table
     * @param row The row
     */
    public static void scrollToRow(JTable table, int row)
    {
        Rectangle visibleRect = table.getVisibleRect();
        Rectangle cellRect = table.getCellRect(row, 0, true);
        Rectangle r = new Rectangle(
            visibleRect.x, cellRect.y, 
            visibleRect.width, cellRect.height);
        table.scrollRectToVisible(r);
    }
    
    /**
     * Convert all of the given model row indices to view row indices
     * for the given table
     * 
     * @param table The table
     * @param modelRows The model row indices
     * @return The view row indices
     */
    public static Set<Integer> convertRowIndicesToView(
        JTable table, Iterable<? extends Integer> modelRows)
    {
        Set<Integer> viewRows = new LinkedHashSet<Integer>();
        for (Integer modelRow : modelRows)
        {
            int viewRow = table.convertRowIndexToView(modelRow);
            viewRows.add(viewRow);
        }
        return viewRows;
    }

    /**
     * Convert all of the given view row indices to model row indices
     * for the given table
     * 
     * @param table The table
     * @param viewRows The view row indices
     * @return The model row indices
     */
    public static Set<Integer> convertRowIndicesToModel(
        JTable table, Iterable<? extends Integer> viewRows)
    {
        Set<Integer> modelRows = new LinkedHashSet<Integer>();
        for (Integer viewRow : viewRows)
        {
            int modelRow = table.convertRowIndexToModel(viewRow);
            modelRows.add(modelRow);
        }
        return modelRows;
    }

    /**
     * Compute the indices of all rows of the given table where the
     * value in the given column is equal to the value in the given
     * row 
     * 
     * @param table The table
     * @param row The reference row
     * @param col The column
     * @return The row indices
     */
    public static List<Integer> computeRowsWithEqualValue(
        JTable table, int row, int col)
    {
        Object referenceValue = table.getValueAt(row, col);
        List<Integer> selectedRows = new ArrayList<Integer>();
        for (int r = 0; r < table.getRowCount(); r++)
        {
            Object value = table.getValueAt(r, col); 
            if (Objects.equals(value, referenceValue))
            {
                selectedRows.add(r);
            }
        }
        return selectedRows;
    }
    
    /**
     * Set a "small" font for the given table and its header
     * 
     * @param t The table
     */
    public static void setSmallFont(JTable t)
    {
        setFont(t, 9.0);
    }
    
    /**
     * Set a font for the given table and its header that is derived from
     * its current font, but has the given size
     * 
     * @param t The table
     * @param size The size
     */
    public static void setFont(JTable t, double size)
    {
        setDerivedFont(t, (float)size);
    }
    
    /**
     * Set a derived font with the given size for the given
     * table and its header
     * 
     * @param t The table
     * @param size The font size
     */
    private static void setDerivedFont(JTable t, float size)
    {
        t.setFont(t.getFont().deriveFont(size));
        t.getTableHeader().setFont(
            t.getTableHeader().getFont().deriveFont(size));
    }
    

    /**
     * Private constructor to prevent instantiation
     */
    private JTables()
    {
        // Private constructor to prevent instantiation
    }
    
}
