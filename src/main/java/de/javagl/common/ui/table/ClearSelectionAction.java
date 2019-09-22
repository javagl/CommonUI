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

import javax.swing.AbstractAction;
import javax.swing.JTable;

/**
 * An action to clear the selection of a table
 */
class ClearSelectionAction extends AbstractAction
{
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 6200885470391710645L;
    
    /**
     * The table
     */
    private final JTable table;
    
    /**
     * Default constructor
     * 
     * @param table The table
     */
    ClearSelectionAction(JTable table)
    {
        this.table = table;
        
        putValue(NAME, "Clear selection");
        putValue(SHORT_DESCRIPTION, "Clear the current selection");
        putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_C));
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        table.getSelectionModel().clearSelection(); 
    }
}