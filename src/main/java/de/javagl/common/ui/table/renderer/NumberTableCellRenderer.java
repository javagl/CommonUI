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

import java.awt.Component;
import java.util.Objects;
import java.util.function.Function;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Implementation of a <code>TableCellRenderer</code> that shows numbers
 * using a given formatter
 */
public class NumberTableCellRenderer extends DefaultTableCellRenderer
{
    /**
     * Serial UID 
     */
    private static final long serialVersionUID = -474975707473940777L;
    
    /**
     * The number formatter
     */
    private final Function<? super Number, String> numberFormatter;

    /**
     * Creates a new instance using the given formatter for numbers
     * 
     * @param numberFormatter The number formatter
     */
    NumberTableCellRenderer(Function<? super Number, String> numberFormatter)
    {
        this.numberFormatter = Objects.requireNonNull(
            numberFormatter, "The numberFormatter may not be null");
        setHorizontalAlignment(JLabel.RIGHT);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column)
    {
        super.getTableCellRendererComponent(table, value, isSelected,
            hasFocus, row, column);
        if (value == null)
        {
            setText("");
        }
        else if (value instanceof Number)
        {
            Number number = (Number)value;
            String s = numberFormatter.apply(number);
            setText(s);
        }
        else
        {
            setText(String.valueOf(value));
        }
        return this;
    }
}
