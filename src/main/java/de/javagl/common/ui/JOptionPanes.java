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
package de.javagl.common.ui;

import java.awt.Window;
import java.util.Arrays;
import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

/**
 * Methods related to option panes
 */
public class JOptionPanes
{
    /**
     * Create a new input dialog that performs validation.
     * 
     * @param parent The optional parent
     * @param title The title
     * @param mainComponent The main component that is shown in the 
     * dialog. This must be a component that contains the text component.
     * @param textComponent The text component
     * @param validInputPredicate The predicate that says whether the
     * input is valid
     * @return <code>JOptionPane.OK_OPTION</code> if OK was pressed. Any
     * other value otherwise.
     */
    public static int showValidatedTextInputDialog(Window parent, String title,
        JComponent mainComponent, JTextComponent textComponent,
        Predicate<String> validInputPredicate)
    {
        JButton okButton = new JButton("Ok");
        
        String text = textComponent.getText();
        boolean valid = validInputPredicate.test(text);
        okButton.setEnabled(valid);
        
        JButton cancelButton = new JButton("Cancel");

        Object[] options = new Object[] { okButton, cancelButton };
        JOptionPane optionPane =
            new JOptionPane(mainComponent, JOptionPane.PLAIN_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION, null, options, okButton);

        okButton.addActionListener(e -> optionPane.setValue(okButton));
        cancelButton.addActionListener(e -> optionPane.setValue(cancelButton));

        AncestorListener focussingAncestorListener = new AncestorListener()
        {
            @Override
            public void ancestorAdded(AncestorEvent event)
            {
                textComponent.requestFocus();
            }
            
            @Override
            public void ancestorRemoved(AncestorEvent event)
            {
                // Nothing to do here
            }
            
            @Override
            public void ancestorMoved(AncestorEvent event)
            {
                // Nothing to do here
            }
        };
        textComponent.addAncestorListener(focussingAncestorListener);
        
        DocumentListener documentListener = new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                updateButtonState();
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                updateButtonState();
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                updateButtonState();
            }

            protected void updateButtonState()
            {
                String text = textComponent.getText();
                boolean valid = validInputPredicate.test(text);
                okButton.setEnabled(valid);
            }
        };
        Document document = textComponent.getDocument();
        document.addDocumentListener(documentListener);

        JDialog dialog = optionPane.createDialog(parent, title);
        dialog.pack();
        dialog.setResizable(true);
        dialog.setVisible(true);
        
        document.removeDocumentListener(documentListener);
        textComponent.removeAncestorListener(focussingAncestorListener);
        
        Object selectedValue = optionPane.getValue();
        if (selectedValue == null)
        {
            return JOptionPane.CLOSED_OPTION;
        }
        return Arrays.asList(options).indexOf(selectedValue);
    }

    /**
     * Private constructor to prevent instantiation
     */
    private JOptionPanes()
    {
        // Private constructor to prevent instantiation
    }
}
