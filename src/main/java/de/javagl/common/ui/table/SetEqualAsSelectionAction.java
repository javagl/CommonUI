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

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JTable;

import de.javagl.common.ui.JTables;
import de.javagl.common.ui.LocationBasedAction;
import de.javagl.common.ui.list.ListSelectionModels;

/**
 * A {@link LocationBasedAction} that sets all rows of a table
 * that contain the same value as a given row in a certain column
 * as the selection
 */
class SetEqualAsSelectionAction extends LocationBasedAction
{
    /**
     * Serial UID
     */
    private static final long serialVersionUID = -8677896136926635626L;

    /**
     * The table
     */
    private JTable table;
    
    /**
     * The row
     */
    private int row;
    
    /**
     * The column
     */
    private int col;
    
    /**
     * Default constructor
     */
    SetEqualAsSelectionAction()
    {
        putValue(NAME, "SetEqualAsSelectionAction");
        putValue(SHORT_DESCRIPTION, "Select all rows where the value "
            + "is equal to the clicked value");
        putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_S));
    }
    
    @Override
    protected void prepareShow(Component component, int x, int y)
    {
        table = (JTable)component;
        Point p = new Point(x,y);
        row = table.rowAtPoint(p);
        col = table.columnAtPoint(p);
        if (row != -1 && col != -1)
        {
            String columnName = table.getColumnName(col);
            Object value = table.getValueAt(row, col);
            putValue(NAME, "Set as selection all where " 
                + columnName + " is \"" + value + "\"");
        }
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        List<Integer> selectedRows = 
            JTables.computeRowsWithEqualValue(table, row, col);
        ListSelectionModels.setAsSelection(
            table.getSelectionModel(), selectedRows);
    }
}