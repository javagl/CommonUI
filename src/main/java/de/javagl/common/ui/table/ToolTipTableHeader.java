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

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * A JTable header that shows a tooltip with the column name
 */
public class ToolTipTableHeader extends JTableHeader
{
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 6269830674612891674L;

    /**
     * Default constructor 
     * 
     * @param columnModel The column model
     */
    public ToolTipTableHeader(TableColumnModel columnModel)
    {
        super(columnModel);
    }

    @Override
    public String getToolTipText(MouseEvent e)
    {
        TableColumnModel columnModel = getColumnModel();
        Point p = e.getPoint();
        int index = columnModel.getColumnIndexAtX(p.x);
        if (index == -1)
        {
            return null;
        }
        TableColumn column = columnModel.getColumn(index);
        return String.valueOf(column.getHeaderValue());
    }
}
