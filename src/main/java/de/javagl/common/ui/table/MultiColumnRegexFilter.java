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
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;

import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.text.Document;

/**
 * A class that creates a table filter for multiple columns. It maintains 
 * one text field for each column, and combines the texts from these text 
 * fields to create a row filter. The texts from the text fields are assumed 
 * to contain regular expressions, and the filter conditions are AND-combined 
 * to create the actual filter. 
 */
public class MultiColumnRegexFilter
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(MultiColumnRegexFilter.class.getName());

    /**
     * The instance that will receive the filter
     */
    private final Consumer<? super RowFilter<TableModel, Integer>> 
        filterConsumer;
    
    /**
     * The text fields
     */
    private final List<JTextField> textFields;
    
    /**
     * Whether the regular expressions should be prefixed with 
     * <code>"(?i)"</code> to make them case-INsensitive
     */
    private boolean ignoringCase;
    
    /**
     * Creates a new instance that passes the filter to the given consumer
     * 
     * @param filterConsumer The table row sorter
     */
    public MultiColumnRegexFilter(
        Consumer<? super RowFilter<TableModel, Integer>> filterConsumer)
    {
        this.filterConsumer = Objects.requireNonNull(
            filterConsumer, "The filterConsumer may not be null");
        this.textFields = new ArrayList<JTextField>();
        this.ignoringCase = true;
    }
    
    /**
     * Clear all text fields in this instance
     */
    public void clearAll()
    {
        for (int i = 0; i < textFields.size(); i++)
        {
            JTextField textField = textFields.get(i);
            if (textField == null)
            {
                continue;
            }
            textField.setText("");
        }
    }
    
    /**
     * Set whether the regular expressions in the text fields should be 
     * matched case-insensitively 
     * 
     * @param ignoringCase Whether the case should be ignored
     */
    public void setIgnoringCase(boolean ignoringCase)
    {
        this.ignoringCase = ignoringCase;
        updateFilter();
    }
    
    /**
     * Create the text field for the specified column
     * 
     * @param columnIndex The column index
     * @return The text field
     */
    public JTextField createFilterTextField(int columnIndex)
    {
        while (textFields.size() - 1 < columnIndex)
        {
            textFields.add(null);
        }
        JTextField textField = new JTextField();
        textFields.set(columnIndex, textField);
        
        Document document = textField.getDocument();
        document.addDocumentListener(new DocumentListener()
        {
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                updateFilter();
            }
            
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                updateFilter();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e)
            {
                updateFilter();
            }
        });
        return textField;
    }
    
    /**
     * Remove the text field for the specified column
     * 
     * @param columnIndex The column index
     */
    public void removeFilterTextField(int columnIndex)
    {
        textFields.remove(columnIndex);
    }
    
    /**
     * Update the row filter based on the contents of the text fields
     */
    private void updateFilter()
    {
        List<RowFilter<TableModel, Integer>> regexFilters = 
            new ArrayList<RowFilter<TableModel, Integer>>();
        for (int i = 0; i < textFields.size(); i++)
        {
            JTextField textField = textFields.get(i);
            if (textField == null)
            {
                continue;
            }
            String regex = textField.getText();
            RowFilter<TableModel, Integer> regexFilter = 
                createRegexFilter(regex, i);
            if (regexFilter == null)
            {
                continue;
            }
            regexFilters.add(regexFilter);
        }
        if (regexFilters.isEmpty())
        {
            filterConsumer.accept(null);
        }
        else
        {
            RowFilter<TableModel, Integer> rowFilter = 
                RowFilter.andFilter(regexFilters);
            filterConsumer.accept(rowFilter);
        }
    }
    
    /**
     * Create a row filter for the given regex and column. If the given
     * string is not a valid regex, then a warning will be printed and
     * <code>null</code> will be returned.
     * 
     * @param regex The regex
     * @param columnIndex The column index
     * @return The row filter
     */
    private RowFilter<TableModel, Integer> createRegexFilter(
        String regex, int columnIndex)
    {
        try
        {
            String finalRegex = regex;
            if (ignoringCase)
            {
                finalRegex = "(?i)" + regex;
            }
            RowFilter<TableModel, Integer> rowFilter = 
                RowFilter.regexFilter(finalRegex, columnIndex);
            return rowFilter;
        }
        catch (PatternSyntaxException e)
        {
            logger.warning(e.getMessage());
            return null;
        }
    }
    
    
}