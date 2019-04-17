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
package de.javagl.common.ui.text;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicArrowButton;

/**
 * The panel shown at the top of the {@link SearchableTextComponent}
 */
class SearchPanel extends JPanel
{
    /**
     * Serial UID 
     */
    private static final long serialVersionUID = -875498007982382336L;

    /**
     * The main text field for the query
     */
    private final JTextField textField;
    
    /**
     * The check box for selecting case sensitivity
     */
    private final JCheckBox caseSensitiveCheckBox;
    
    /**
     * A label for messages
     */
    private final JLabel messageLabel;
    
    /**
     * Creates a new instance
     * 
     * @param owner The owner
     */
    SearchPanel(SearchableTextComponent owner)
    {
        super(new BorderLayout());
        Objects.requireNonNull(owner, "The owner may not be null");

        JPanel mainPanel = new JPanel(new GridLayout(2,1));

        // The top panel, containing the text field and case check box
        JPanel topPanel = new JPanel(new BorderLayout()); 
        
        textField = new JTextField();
        topPanel.add(textField, BorderLayout.CENTER);
        
        caseSensitiveCheckBox = new JCheckBox("Case sensitive");
        caseSensitiveCheckBox.addActionListener(
            e -> owner.updateSearchResults());
        topPanel.add(caseSensitiveCheckBox, BorderLayout.EAST);
        
        mainPanel.add(topPanel);
        
        // The bottom panel containing the message and find buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        messageLabel = new JLabel(" ");
        bottomPanel.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = 
            new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JButton prevButton = new BasicArrowButton(BasicArrowButton.NORTH);
        prevButton.addActionListener(e -> owner.doFindPrevious());
        buttonPanel.add(prevButton);

        JButton nextButton = new BasicArrowButton(BasicArrowButton.SOUTH);
        nextButton.addActionListener(e -> owner.doFindNext());
        buttonPanel.add(nextButton);
        
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        
        mainPanel.add(bottomPanel);
        
        add(mainPanel, BorderLayout.CENTER);
        
        
        // The action to hide this panel (Escape)
        KeyStroke hideKeyStroke = 
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
            hideKeyStroke, "SearchPanel.doHide");
        getActionMap().put("SearchPanel.doHide",
            Actions.create(() -> owner.setSearchPanelVisible(false)));
        
        // The action to find the next appearance
        KeyStroke findNextKeyStroke = 
            KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);
        textField.getInputMap(JComponent.WHEN_FOCUSED).put(
            findNextKeyStroke, "SearchPanel.doFindNext");
        textField.getActionMap().put("SearchPanel.doFindNext", 
            Actions.create(() -> owner.doFindNext()));
        
        // The action to find the previous appearance
        KeyStroke findPreviousKeyStroke =
            KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.SHIFT_DOWN_MASK);
        textField.getInputMap(JComponent.WHEN_FOCUSED).put(
            findPreviousKeyStroke, "SearchPanel.doFindPrevious");
        textField.getActionMap().put("SearchPanel.doFindPrevious", 
            Actions.create(() -> owner.doFindPrevious()));
        
        // The listener to update the search results
        textField.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                owner.updateSearchResults();
            }
            
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                owner.updateSearchResults();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e)
            {
                owner.updateSearchResults();
            }
        });
    }
    
    /**
     * Returns whether the search is case sensitive
     * 
     * @return The state
     */
    boolean isCaseSensitive()
    {
        return caseSensitiveCheckBox.isSelected();
    }
    
    /**
     * Returns the currently entered search query
     * 
     * @return The query
     */
    String getQuery()
    {
        return textField.getText();
    }

    /**
     * Set the current search query
     * 
     * @param query The query 
     */
    void setQuery(String query)
    {
        textField.setText(query);
    }
    
    /**
     * Let the text field request the focus
     */
    void requestFocusForTextField()
    {
        textField.requestFocus();
    }
    
    /**
     * Set the message that is currently displayed
     * 
     * @param message The message
     */
    void setMessage(String message)
    {
        messageLabel.setText(message);
    }
    
}

