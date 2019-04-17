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

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * A TableCellRenderer that combines two others.<br>
 * <br>
 * Note: Some details (particularly, the layout that is used for the 
 * combination) are intentionally not specified here.
 */
public class CompoundTableCellRenderer implements TableCellRenderer
{
    /**
     * The delegate cell renderer
     */
    private final TableCellRenderer delegate0;
    
    /**
     * The delegate cell renderer
     */
    private final TableCellRenderer delegate1;
    
    /**
     * The container for the delegate components
     */
    private final JPanel container;
    
    /**
     * Creates a new renderer with the given delegates.
     * 
     * @param delegate0 The delegate0
     * @param delegate1 The delegate1
     */
    public CompoundTableCellRenderer(
        TableCellRenderer delegate0,
        TableCellRenderer delegate1)
    {
        this.delegate0 = Objects.requireNonNull(
            delegate0, "The delegate0 may not be null");
        this.delegate1 = Objects.requireNonNull(
            delegate1, "The delegate1 may not be null");
        this.container = new JPanel(new BorderLayout());
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column)
    {
        Component delegateComponent0 = 
            delegate0.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        Component delegateComponent1 = 
            delegate1.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        container.removeAll();
        container.add(delegateComponent0, BorderLayout.NORTH);
        container.add(delegateComponent1, BorderLayout.CENTER);
        return container;
    }
    
}