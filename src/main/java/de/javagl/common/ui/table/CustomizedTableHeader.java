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
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import de.javagl.common.ui.table.renderer.CompoundTableCellRenderer;

/**
 * A table header that allows adding custom components over the usual table
 * header, and maintains the appropriate size for these components
 */
public class CustomizedTableHeader extends JTableHeader
{
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 1980995454624976265L;


    /**
     * The height that should be reserved for the custom components
     */
    private final int customComponentHeight;

    /**
     * The cell renderer that will insert the space with the
     * {@link #customComponentHeight} over the actual table header
     */
    private final TableCellRenderer spaceRenderer;

    /**
     * The listener that will be attached to the table columns, and trigger
     * an update of the custom component bounds whenever the size of a 
     * column changes
     */
    private final PropertyChangeListener widthListener;

    /**
     * The listener that will keep track of the table column model and
     * insert or remove the custom components for columns that are 
     * added or removed
     */
    private final TableColumnModelListener columnModelListener;

    /**
     * The factory for the custom components that will receive a column
     * index and create the custom component
     */
    private final IntFunction<JComponent> componentFactory;
    
    /**
     * The remover that will be called when a column is removed, and will
     * receive the column index
     */
    private final IntConsumer componentRemover;
    
    /**
     * The list containing the custom components, one for each column
     */
    private final List<JComponent> customComponents;
    
    /**
     * The list of columns in the current column model
     */
    private final List<TableColumn> tableColumns;

    /**
     * Creates a new instance
     * 
     * @param tableColumnModel The table column model
     * @param customComponentHeight The height that should be reserved for
     * the custom components
     * @param componentFactory The factory that will create the custom
     * components, given a column index
     * @param componentRemover The optional callback the will receive the 
     * indices of columns that are removed 
     */
    public CustomizedTableHeader(TableColumnModel tableColumnModel,
        int customComponentHeight, 
        IntFunction<JComponent> componentFactory,
        IntConsumer componentRemover)
    {
        this.customComponentHeight = customComponentHeight;
        this.componentFactory = Objects.requireNonNull(
            componentFactory, "The componentFactory may not be null");
        this.componentRemover = componentRemover; 
        this.tableColumns = new ArrayList<TableColumn>();
        this.customComponents = new ArrayList<JComponent>();
        
        // Create the listener that will keep track of moved, added and
        // removed columns
        this.columnModelListener = new TableColumnModelListener()
        {
            @Override
            public void columnSelectionChanged(ListSelectionEvent e)
            {
                // Nothing to do here
            }

            @Override
            public void columnRemoved(TableColumnModelEvent e)
            {
                removeColumn(e.getFromIndex());
            }

            @Override
            public void columnMoved(TableColumnModelEvent e)
            {
                int fromIndex = e.getFromIndex();
                int toIndex = e.getToIndex();
                if (fromIndex != toIndex)
                {
                    Collections.swap(tableColumns, fromIndex, toIndex);
                    Collections.swap(customComponents, fromIndex, toIndex);
                }
                updateCustomComponentBounds();
            }

            @Override
            public void columnMarginChanged(ChangeEvent e)
            {
                // Nothing to do here
            }

            @Override
            public void columnAdded(TableColumnModelEvent e)
            {
                addColumn(e.getToIndex());
            }

        };

        // The listener that will trigger a layout update when a column
        // width changes
        this.widthListener = new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if ("width".equals(evt.getPropertyName()))
                {
                    updateCustomComponentBounds();
                }
            }
        };

        // The renderer that will render the space over the actual table
        // header, where the custom components will be placed
        this.spaceRenderer = new TableCellRenderer()
        {
            private final Component component = 
                Box.createVerticalStrut(customComponentHeight);
            @Override
            public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column)
            {
                return component;
            }
        };

        setColumnModel(tableColumnModel);
        setDefaultRenderer(createDefaultRenderer());
    }
    
    /**
     * {@inheritDoc}
     * <br>
     * <br>
     * <b>Note:</b> This method is overridden in the CustomizedTableHeader.
     * It will internally combine the given renderer with the one that
     * reserves the space for the custom components. This means that 
     * calling {@link #getDefaultRenderer()} will return a different
     * renderer than the one that was passed to this call.
     */
    @Override
    public void setDefaultRenderer(TableCellRenderer defaultRenderer)
    {
        if (spaceRenderer != null)
        {
            CompoundTableCellRenderer renderer =
                new CompoundTableCellRenderer(spaceRenderer, defaultRenderer);
            super.setDefaultRenderer(renderer);
        }
        else
        {
            super.setDefaultRenderer(defaultRenderer);
        }
    }

    /**
     * Remove the column and the custom component at the given index
     * 
     * @param index The index
     */
    private void removeColumn(int index)
    {
        TableColumn column = tableColumns.get(index);
        column.removePropertyChangeListener(widthListener);
        tableColumns.remove(index);
        JComponent component = customComponents.remove(index);
        remove(component);
        if (componentRemover != null)
        {
            componentRemover.accept(index);
        }
    }

    /**
     * Add the column and the custom component at the given index
     * 
     * @param index The index
     */
    private void addColumn(int index)
    {
        TableColumnModel columnModel = getColumnModel();
        TableColumn column = columnModel.getColumn(index);
        while (tableColumns.size() - 1 < index)
        {
            tableColumns.add(null);
        }
        tableColumns.set(index, column);
        column.addPropertyChangeListener(widthListener);
        
        JComponent component = componentFactory.apply(index);
        while (customComponents.size() - 1 < index)
        {
            customComponents.add(null);
        }
        customComponents.set(index, component);
        add(component);
    }
    
    /**
     * Update the bounds of all custom components, based on the current
     * column widths
     */
    private void updateCustomComponentBounds()
    {
        if (customComponents == null)
        {
            return;
        }
        if (table == null)
        {
            return;
        }
        for (int i = 0; i < customComponents.size(); i++)
        {
            JComponent component = customComponents.get(i);
            Rectangle rect = getHeaderRect(i);
            rect.height = customComponentHeight;
            component.setBounds(rect);
        }
        revalidate();
    }

    @Override
    public void setColumnModel(TableColumnModel newColumnModel)
    {
        TableColumnModel oldColumnModel = getColumnModel();
        if (oldColumnModel != null)
        {
            oldColumnModel.removeColumnModelListener(columnModelListener);
            int n = oldColumnModel.getColumnCount();
            for (int i = 0; i < n; i++)
            {
                removeColumn(0);
            }
        }
        super.setColumnModel(newColumnModel);

        if (newColumnModel != null)
        {
            newColumnModel.addColumnModelListener(columnModelListener);
            int n = newColumnModel.getColumnCount();
            for (int i = 0; i < n; i++)
            {
                addColumn(i);
            }
        }
        updateCustomComponentBounds();
    }
    
}




