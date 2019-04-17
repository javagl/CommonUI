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
package de.javagl.common.ui.table.renderer;

import java.awt.Color;
import java.awt.Component;
import java.util.Objects;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Implementation of a table cell renderer that sets the background color
 * of the cell renderer component from a delegate renderer based on a
 * color function for the value in the cell.<br>
 * <br>
 * Selected cells will be indicated by a border. 
 */
public class BackgroundColorTableCellRenderer implements TableCellRenderer
{
    /**
     * The delegate renderer
     */
    private final TableCellRenderer delegate;
    
    /**
     * The color function
     */
    private final Function<Object, ? extends Color> colorFunction;
    
    /**
     * Creates a new instance.
     * 
     * @param delegate The delegate renderer
     * @param colorFunction The color function
     */
    BackgroundColorTableCellRenderer(
        TableCellRenderer delegate,
        Function<Object, ? extends Color> colorFunction)
    {
        this.delegate = Objects.requireNonNull(
            delegate, "The delegate may not be null");
        this.colorFunction = Objects.requireNonNull(
            colorFunction, "The colorFunction may not be null");
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column)
    {
        Component component = delegate.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column);
        Color color = colorFunction.apply(value);
        if (color != null)
        {
            component.setBackground(color);
            Color foregroundColor = 
                computeContrastingColor(color.getRGB());
            component.setForeground(foregroundColor);
        }
        else
        {
            component.setBackground(table.getBackground());
            component.setForeground(table.getForeground());
        }
        if (isSelected)
        {
            JComponent c = (JComponent)component;
            c.setBorder(BorderFactory.createLineBorder(
                table.getSelectionBackground(), 2));
        }
        else
        {
            JComponent c = (JComponent)component;
            c.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        }
        return component;
    }
    
    /**
     * Compute a color (either black or white) that as a large contrast
     * to the given ARGB color. Some details of this method are intentionally
     * not specified.
     * 
     * @param argb The ARGB color
     * @return The contrasting color
     */
    private static Color computeContrastingColor(int argb)
    {
        if (computeLuminance(argb) > 0.179)
        {
            return Color.BLACK;
        }
        return Color.WHITE;
    }
    
    /**
     * Returns the luminance of the given ARGB color
     * 
     * @param argb The ARGB color
     * @return The luminance
     */
    private static double computeLuminance(int argb)
    {
        int r = (argb >> 16) & 0xFF;
        int g = (argb >>  8) & 0xFF;
        int b = (argb      ) & 0xFF;
        double nr = Math.pow((r / 255.0), 2.2);
        double ng = Math.pow((g / 255.0), 2.2);
        double nb = Math.pow((b / 255.0), 2.2);
        double y = 0.2126 * nr + 0.7151 * ng + 0.0721 * nb;
        return y;
    }
    
}
