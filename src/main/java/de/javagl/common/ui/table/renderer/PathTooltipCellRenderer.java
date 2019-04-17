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
import java.nio.file.Path;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * A table cell renderer for <code>Path</code> objects. It shows the file
 * name in the cell, and sets the tooltip of the cells to show the full
 * path string.<br>
 * <br>  
 * It shows the string representation of the object for other types.
 */
public class PathTooltipCellRenderer extends DefaultTableCellRenderer
{
    /**
     * Serial UID 
     */
    private static final long serialVersionUID = 2490353270068441971L;

    @Override
    public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column)
    {
        super.getTableCellRendererComponent(table, value, isSelected,
            hasFocus, row, column);
        if (value != null)
        {
            if (value instanceof Path)
            {
                Path path = (Path)value;
                String fileName = path.getFileName().toString();
                setText(fileName);
                String pathString = path.toString();
                setToolTipText(pathString);
            }
            else
            {
                String string = String.valueOf(value);
                setToolTipText(string);
            }
        }
        else
        {
            setToolTipText(null);
        }
        return this;
    }
}
