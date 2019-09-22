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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JTable;

import de.javagl.common.ui.list.ListSelectionModels;

/**
 * An action that inverts the selection of a table
 */
class InvertSelectionAction extends AbstractAction
{
    /**
     * Serial UID
     */
    private static final long serialVersionUID = -4924834825079640491L;
    
    /**
     * The table
     */
    private final JTable table;
    
    /**
     * Default constructor
     * 
     * @param table The table
     */
    InvertSelectionAction(JTable table)
    {
        this.table = table;
        
        putValue(NAME, "Invert selection");
        putValue(SHORT_DESCRIPTION, "Invert the current selection");
        putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_I));
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        List<Integer> unselectedRowIndices = 
            ListSelectionModels.computeUnselectedIndices(
                table.getSelectionModel(), table.getRowCount());
        ListSelectionModels.setAsSelection(
            table.getSelectionModel(), unselectedRowIndices);
    }
}