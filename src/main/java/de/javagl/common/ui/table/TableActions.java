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

import javax.swing.Action;
import javax.swing.JTable;

import de.javagl.common.ui.LocationBasedAction;
import de.javagl.common.ui.LocationBasedPopupHandler;

/**
 * Methods for creating actions that operate on a table, for example, to
 * change the selection state.<br>
 */
public class TableActions
{
    /**
     * Create the actions for changing the selection state of a table
     * based on the content of the cell that was clicked on. These
     * are {@link LocationBasedAction} instances, and supposed to be
     * shown in a popup menu with a {@link LocationBasedPopupHandler}
     * 
     * @return The actions
     */
    public static List<Action> createContentBasedSelectionActions()
    {
        List<Action> actions = new ArrayList<Action>();
        actions.add(new SetEqualAsSelectionAction());
        actions.add(new AddEqualToSelectionAction());
        actions.add(new RemoveEqualFromSelectionAction());
        return actions;
    }
    
    /**
     * Create the actions for changing the selection state of a table.
     * 
     * @param table The table
     * @return The actions
     */
    public static List<Action> createGenericSelectionActions(JTable table)
    {
        List<Action> actions = new ArrayList<Action>();
        actions.add(new SelectAllAction(table));
        actions.add(new InvertSelectionAction(table));
        actions.add(new ClearSelectionAction(table));
        return actions;
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private TableActions()
    {
        // Private constructor to prevent instantiation
    }

}
