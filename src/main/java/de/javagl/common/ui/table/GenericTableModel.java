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
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

/**
 * Simple implementation of a generic table model that operates on objects
 * using getters and setters.<br>
 * <br>
 * <b>Note:</b> This class is not type-safe in any way. The user is responsible
 * for making sure that the getters and setters are of the appropriate type. 
 * If a getter or setter is called and the type does not match, a warning 
 * is printed and the call will be ignored.
 */
public class GenericTableModel extends AbstractTableModel
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GenericTableModel.class.getName());

    /**
     * Serial UID
     */
    private static final long serialVersionUID = -597222256747479585L;

    /**
     * A class encapsulating the data for one column
     */
    private static class Column
    {
        /**
         * The name of the column
         */
        String name;
        
        /**
         * The type of the column
         */
        Class<?> type;
        
        /**
         * The getter for the value
         */
        Function<?, ?> getter;
        
        /**
         * The optional setter for the value
         */
        BiConsumer<?, ?> setter;
    }

    /**
     * The columns of this model
     */
    private final List<Column> columns;

    /**
     * The elements of this model
     */
    private final List<Object> elements;
    
    /**
     * Default constructor
     */
    public GenericTableModel()
    {
        this.columns = new ArrayList<Column>();
        this.elements = new ArrayList<Object>();
    }
    
    /**
     * Add the specified column to this model
     * 
     * @param <T> The row type
     * @param <V> The value type
     * 
     * @param name The column name 
     * @param type The column type
     * @param getter The getter for the column value
     */
    public <T, V> void addColumn(String name, Class<? extends V> type, 
        Function<T, V> getter)
    {
        addColumn(name, type, getter, null);
    }

    /**
     * Add the specified column to this model
     * 
     * @param <T> The row type
     * @param <V> The value type
     * 
     * @param name The column name 
     * @param type The column type
     * @param getter The getter for the column value
     * @param setter The optional setter for the column value
     */
    public <T, V> void addColumn(String name, Class<? extends V> type, 
        Function<T, V> getter, BiConsumer<T, V> setter)
    {
        Column column = new Column();
        column.name = Objects.requireNonNull(name, "The name may not be null");
        column.type = Objects.requireNonNull(type, "The type may not be null");
        column.getter = Objects.requireNonNull(
            getter, "The getter may not be null");
        column.setter = setter;
        columns.add(column);
        fireTableStructureChanged();
    }
   
    /**
     * Remove the column at the given index
     * 
     * @param index The index
     */
    public void removeColumn(int index)
    {
        columns.remove(index);
        fireTableStructureChanged();
    }

    @Override
    public int getRowCount()
    {
        return elements.size();
    }

    @Override
    public int getColumnCount()
    {
        return columns.size();
    }

    @Override
    public String getColumnName(int c)
    {
        return columns.get(c).name;
    }

    @Override
    public Class<?> getColumnClass(int c)
    {
        return columns.get(c).type;
    }
    
    @Override
    public boolean isCellEditable(int r, int c)
    {
        Column column = columns.get(c);
        return column.setter != null;
    }
    
    /**
     * Add the given element as one row of the table
     * 
     * @param element The element
     */
    public void addRow(Object element)
    {
        insertRow(getRowCount(), element);
    }

    /**
     * Add the given element as one row of the table
     * 
     * @param index The row index
     * @param element The element
     */
    public void insertRow(int index, Object element)
    {
        elements.add(index, element);
        fireTableRowsInserted(index, index);
    }
    
    /**
     * Remove the specified row from this model
     * 
     * @param index The row index
     */
    public void removeRow(int index) 
    {
        elements.remove(index);
        fireTableRowsDeleted(index, index);
    }
    
    
    @Override
    public void setValueAt(Object value, int r, int c)
    {
        Column column = columns.get(c);
        Object element = elements.get(r);
        BiConsumer<?, ?> setter = column.setter;
        try
        {
            @SuppressWarnings("unchecked")
            BiConsumer<Object, Object> typedSetter = 
                (BiConsumer<Object, Object>) setter;
            typedSetter.accept(element, value);
        }
        catch (ClassCastException e)
        {
            logger.warning(e.getMessage());
        }
    }

    @Override
    public Object getValueAt(int r, int c)
    {
        Column column = columns.get(c);
        Object element = elements.get(r);
        Function<?, ?> getter = column.getter;
        try
        {
            @SuppressWarnings("unchecked")
            Function<Object, ?> typedGetter = (Function<Object, ?>) getter;
            Object result = typedGetter.apply(element);
            return result;
        }
        catch (ClassCastException e)
        {
            logger.warning(e.getMessage());
            return null;
        }
    }

}
