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
package de.javagl.common.ui.properties;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A generic dialog that allows entering several property values as strings
 */
public class PropertiesDialog extends JDialog
{
    /**
     * Creates a new {@link PropertiesDialogBuilder} that creates a 
     * {@link PropertiesDialog} with the given parent and title
     *  
     * @param parentComponent The parent component
     * @param title The title
     * @return The {@link PropertiesDialogBuilder}
     */
    public static PropertiesDialogBuilder create(
        Component parentComponent, String title)
    {
        return new PropertiesDialogBuilder(parentComponent, title);
    }
    
    /**
     * A class for building a {@link PropertiesDialog}
     */
    public static class PropertiesDialogBuilder
    {
        /**
         * The parent component of the dialog
         */
        private final Component parentComponent;
        
        /**
         * The title of the dialog
         */
        private final String title;
        
        /**
         * The property names
         */
        private final List<String> propertyNames;
        
        /**
         * The property descriptions that will be shown in labels
         */
        private final List<String> propertyDescriptions;
        
        /**
         * The default values for the properties that will initially be
         * shown in the text fields
         */
        private final List<String> propertyDefaultValues;
        
        /**
         * The validators that will receive the property values from the
         * text fields, and return whether the property value is valid,
         * causing the "OK" button to be disabled if not
         */
        private final List<Predicate<? super String>> propertyValidators;
        
        /**
         * The functions that will receive the property values from the
         * text fields if and only if the validation failed, and return 
         * a message indicating the error
         */
        private final List<Function<? super String, String>> propertyMessages;
        
        /**
         * Default constructor
         * 
         * @param parentComponent The parent component
         * @param title The title for the dialog
         */
        PropertiesDialogBuilder(Component parentComponent, String title)
        {
            this.parentComponent = parentComponent;
            this.title = title;
            this.propertyNames = new ArrayList<String>();
            this.propertyDescriptions = new ArrayList<String>();
            this.propertyDefaultValues = new ArrayList<String>();
            this.propertyValidators = 
                new ArrayList<Predicate<? super String>>();
            this.propertyMessages = 
                new ArrayList<Function<? super String, String>>();
        }
        
        /**
         * Add the specified property to the dialog that is being built
         * 
         * @param name The name
         * @param description The description
         * @param defaultValue The default value
         * @return This builder
         */
        public PropertiesDialogBuilder addProperty(
            String name, String description, String defaultValue)
        {
            return addProperty(name, description, defaultValue, null, null);
        }
        
        /**
         * Add the specified property to the dialog that is being built
         * 
         * @param name The name
         * @param description The description
         * @return This builder
         */
        public PropertiesDialogBuilder addProperty(
            String name, String description)
        {
            return addProperty(name, description, null, null, null);
        }

        /**
         * Add the specified property to the dialog that is being built
         * 
         * @param name The name
         * @param description The description
         * @param defaultValue The default value
         * @param validator The validator
         * @return This builder
         */
        public PropertiesDialogBuilder addProperty(
            String name, String description, String defaultValue, 
            Predicate<? super String> validator)
        {
            return addProperty(
                name, description, defaultValue, validator, null);
        }
        
        /**
         * Add the specified property to the dialog that is being built
         * 
         * @param name The name
         * @param description The description
         * @param defaultValue The default value
         * @param validator The validator
         * @param message The validation failure message
         * @return This builder
         */
        public PropertiesDialogBuilder addProperty(
            String name, String description, String defaultValue, 
            Predicate<? super String> validator,
            Function<? super String, String> message)
        {
            propertyNames.add(name);
            propertyDescriptions.add(description);
            propertyDefaultValues.add(defaultValue);
            propertyValidators.add(validator);
            propertyMessages.add(message);
            return this;
        }
        
        /**
         * Build the {@link PropertiesDialog}
         * 
         * @return The {@link PropertiesDialog}
         */
        public PropertiesDialog build()
        {
            return new PropertiesDialog(parentComponent, title, 
                propertyNames, propertyDescriptions, 
                propertyDefaultValues, propertyValidators,
                propertyMessages);
        }
    }
    
    /**
     * Serial UID
     */
    private static final long serialVersionUID = -8805281807988147166L;
    
    /**
     * A popup for validation messages
     */
    private Popup popup;
    
    /**
     * Whether the "Cancel" button was pressed
     */
    private boolean cancelled = false;
    
    /**
     * The property names
     */
    private final List<String> propertyNames;
    
    /**
     * The property validators
     */
    private final List<Predicate<? super String>> propertyValidators;
    
    /**
     * The property validators failure messages
     */
    private final List<Function<? super String, String>> propertyMessages;
    
    /**
     * The text fields
     */
    private final List<JTextField> textFields;
    
    /**
     * The OK button
     */
    private final JButton okButton;
    
    /**
     * Creates a new instance 
     * 
     * @param parentComponent The parent component
     * @param title The title
     * @param propertyNames The property names
     * @param propertyDescriptions The property descriptions
     * @param propertyDefaultValues The property default values
     * @param propertyValidators The property validators
     * @param propertyMessages The property validator failure messages
     */
    PropertiesDialog(Component parentComponent, String title,
        List<String> propertyNames,
        List<String> propertyDescriptions,
        List<String> propertyDefaultValues,
        List<Predicate<? super String>> propertyValidators,
        List<Function<? super String, String>> propertyMessages)
    {
        super(parentComponent == null ? 
            null : SwingUtilities.getWindowAncestor(parentComponent), 
            title, ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        this.propertyNames = propertyNames;
        this.propertyValidators = propertyValidators;
        this.propertyMessages = propertyMessages;

        getContentPane().setLayout(new BorderLayout());
        
        // Create the panel containing the labels with the property 
        // descriptions and the text fields for entering the values
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        textFields = new ArrayList<JTextField>();
        for (int i=0; i<propertyNames.size(); i++)
        {
            String propertyDescription = propertyDescriptions.get(i);
            String propertyValue = propertyDefaultValues.get(i);
            
            JLabel label = new JLabel(propertyDescription);
            panel.add(label);
            
            JTextField textField = new JTextField(propertyValue);
            textFields.add(textField);
            
            JPanel textFieldContainer = new JPanel(new BorderLayout());
            textFieldContainer.add(textField, BorderLayout.NORTH);
            panel.add(textFieldContainer);
        }
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JPanel p = new JPanel(new BorderLayout());
        p.add(panel, BorderLayout.NORTH);
        getContentPane().add(p, BorderLayout.CENTER);
        
        // Create the buttons for canceling or confirming the input
        JButton cancelButton = new JButton("Cancel");
        okButton = new JButton("OK");
        okButton.setEnabled(false);
        cancelButton.addActionListener(e -> 
        {
            cancelled = true;
            setVisible(false);
            dispose();
        });
        okButton.addActionListener(e -> 
        {
            cancelled = false;
            setVisible(false);   
            dispose();
        });
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(okButton);
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
        
        // Attach listeners to all text fields that will enable or disable
        // the "OK" button, depending on the validation of the properties
        for (JTextField textField : textFields)
        {
            textField.getDocument().addDocumentListener(new DocumentListener()
            {
                @Override
                public void removeUpdate(DocumentEvent e)
                {
                    updateButtonState();
                }
                
                @Override
                public void insertUpdate(DocumentEvent e)
                {
                    updateButtonState();
                }
                
                @Override
                public void changedUpdate(DocumentEvent e)
                {
                    updateButtonState();
                }
            });
        }
        updateButtonState();
    }

    /**
     * Update the state of the "OK" button depending on the values of the
     * properties in the text fields and the validators
     */
    private void updateButtonState()
    {
        hideValidationMessagePopup();
        boolean allValid = true;
        boolean firstInvalid = true;
        for (int i=0; i<propertyNames.size(); i++)
        {
            Predicate<? super String> propertyValidator = 
                propertyValidators.get(i);
            if (propertyValidator != null)
            {
                JTextField textField = textFields.get(i);
                String value = textField.getText();
                boolean isValid = propertyValidator.test(value);
                allValid &= isValid;
                
                if (!isValid)
                {
                    Function<? super String, String> message = 
                        propertyMessages.get(i);
                    if (message != null) 
                    {
                        String messageResult = message.apply(value);
                        if (messageResult != null)
                        {
                            if (firstInvalid)
                            {
                                showValidationMessagePopup(
                                    textField, messageResult);
                                firstInvalid = false;
                            }
                        }
                    }
                }
            }
        }
        okButton.setEnabled(allValid);
    }
    
    /**
     * Show a popup, close to the given component, containing the given text
     * 
     * @param component The component
     * @param textOrHtml The text
     */
    private void showValidationMessagePopup(
        JComponent component, String textOrHtml)
    {
        hideValidationMessagePopup();
        PopupFactory popupFactory = PopupFactory.getSharedInstance();
        JLabel label = new JLabel(textOrHtml);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        Point p = new Point(component.getWidth(), 0);
        SwingUtilities.convertPointToScreen(p, component);
        popup = popupFactory.getPopup(component, label, p.x, p.y);
        popup.show();
    }
    
    /**
     * Hide the validation popup, if it is visible
     */
    private void hideValidationMessagePopup()
    {
        if (popup != null)
        {
            popup.hide();
            popup = null;
        }
        
    }
    
    /**
     * Show this dialog and return the entered values. If the dialog is 
     * cancelled, then <code>null</code> is returned. Otherwise, the
     * return value is a map from property names to their values.
     * 
     * @return The property values
     */
    public Map<String, String> showDialog()
    {
        cancelled = false;
        
        pack();
        setLocationRelativeTo(getOwner());
        setVisible(true);
        
        if (cancelled)
        {
            return null;
        }
        Map<String, String> propertyValues = 
            new LinkedHashMap<String, String>();
        
        for (int i=0; i<propertyNames.size(); i++)
        {
            String propertyName = propertyNames.get(i);
            JTextField textField = textFields.get(i);
            String propertyValue = textField.getText();
            propertyValues.put(propertyName, propertyValue);
        }
        return propertyValues; 
    }
}

