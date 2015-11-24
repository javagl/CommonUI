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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 * A TableCellRenderer that shows indicators of the sort order of
 * a table for multiple sorting columns
 */
public final class SortOrderTableHeaderCellRenderer implements TableCellRenderer
{
    /**
     * The delegate cell renderer
     */
    private final TableCellRenderer delegate;
    
    /**
     * Creates a new renderer with the given delegate. The delegate
     * is assumed to return JLabels as the rendering components
     * 
     * @param delegate The delegate
     */
    public SortOrderTableHeaderCellRenderer(TableCellRenderer delegate)
    {
        this.delegate = delegate;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column)
    {
        Component delegateComponent = 
            delegate.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        if (delegateComponent instanceof JLabel)
        {
            JLabel delegateLabel = (JLabel)delegateComponent;
            delegateLabel.setIcon(null);
            SortOrder sortOrder = getSortOrder(table, column);
            if (sortOrder != null)
            {
                int sortPriority = getSortPriority(table, column);
                if (sortPriority >= 0)
                {
                    delegateLabel.setIcon(
                        createIcon(sortOrder, sortPriority));
                }
            }
        }
        return delegateComponent;
    }
    
    /**
     * Create an Icon for the given sort order and priority. The priority
     * is assumed to be in [0,3]
     * 
     * @param sortOrder The sort order
     * @param sortPriority The sort priority
     * @return The icon
     */
    private Icon createIcon(SortOrder sortOrder, int sortPriority)
    {
        final Color color = UIManager.getColor("controlDkShadow");
        final int width = 12;
        final int height = 12;
        final int sizeX = Math.max(2, width - sortPriority * 2);
        final int sizeY = Math.max(2, height - sortPriority * 2);
        final int space = (height - sizeY) / 2;
        final Shape shape = 
            sortOrder == SortOrder.ASCENDING ?
                createArrowShape(sizeX, space, height-space) :
                createArrowShape(sizeX, height-space, space);
        return new Icon()
        {
            @Override
            public void paintIcon(Component c, Graphics gr, int x, int y)
            {
                Graphics2D g = (Graphics2D)gr;
                AffineTransform oldAT = g.getTransform();
                g.translate(x,y);
                g.setColor(color);
                g.fill(shape);
                g.setTransform(oldAT);
            }

            @Override
            public int getIconWidth()
            {
                return width;
            }

            @Override
            public int getIconHeight()
            {
                return height;
            }
        };        
    }
    
    
    /**
     * Creates a triangle shape with the given coordinates
     * 
     * @param w The width
     * @param y0 The first y-coordinate
     * @param y1 The second y-coordinate
     * @return The shape
     */
    private static Shape createArrowShape(int w, int y0, int y1)
    {
        Path2D path = new Path2D.Double();
        path.moveTo(0, y0);
        if ((w & 1) == 0)
        {
            path.lineTo(w>>1, y1);
        }
        else
        {
            int c = w>>1;
            path.lineTo(c, y1);
            path.lineTo(c+1, y1);
        }
        path.lineTo(w, y0);
        path.closePath();
        return path;
    }
    
    /**
     * Returns the sort order of the specified column in the given table,
     * or null if the column is not sorted
     * 
     * @param table The table
     * @param column The column
     * @return The sort order
     */
    private static SortOrder getSortOrder(JTable table, int column)
    {
        List<? extends SortKey> sortKeys = table.getRowSorter().getSortKeys();
        for (int i=0; i<sortKeys.size(); i++)
        {
            SortKey sortKey = sortKeys.get(i);
            if (sortKey.getColumn() == column)
            {
                return sortKey.getSortOrder();                        
            }
        }
        return null;
    }
    
    /**
     * Returns the sort priority of the specified column in the given
     * table, where 0 means the highest priority, and -1 means that
     * the column is not sorted.
     * 
     * @param table The table
     * @param column The column
     * @return The sort priority
     */
    private static int getSortPriority(JTable table, int column)
    {
        List<? extends SortKey> sortKeys = table.getRowSorter().getSortKeys();
        for (int i=0; i<sortKeys.size(); i++)
        {
            SortKey sortKey = sortKeys.get(i);
            if (sortKey.getColumn() == column)
            {
                return i;                        
            }
        }
        return -1;
    }            
    
}