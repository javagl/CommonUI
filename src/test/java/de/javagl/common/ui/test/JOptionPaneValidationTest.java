package de.javagl.common.ui.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.function.Predicate;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import de.javagl.common.ui.JOptionPanes;

/**
 * Simple integration test for the
 * {@link JOptionPanes#showValidatedTextInputDialog} method
 */
@SuppressWarnings("javadoc")
public class JOptionPaneValidationTest
{
    public static void main(String[] args)
    {
        JPanel mainComponent = new JPanel(new BorderLayout());

        mainComponent.add(new JLabel("Enter 'Test' here:"), BorderLayout.NORTH);

        JTextArea textArea = new JTextArea(5, 30);
        mainComponent.add(textArea, BorderLayout.CENTER);

        JLabel messageLabel = new JLabel(" ");
        mainComponent.add(messageLabel, BorderLayout.SOUTH);

        Predicate<String> predicate = input -> 
        {
            if ("Test".equals(input))
            {
                messageLabel.setForeground(Color.BLACK);
                messageLabel.setText("Well done.");
                return true;
            }
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("You have to enter 'Test'!");
            return false;
        };
        
        int result = JOptionPanes.showValidatedTextInputDialog(null, "Test", 
            mainComponent, textArea, predicate);
        
        if (result == JOptionPane.OK_OPTION)
        {
            System.out.println("Pressed OK");
        }
        else
        {
            System.out.println("Pressed Cancel");
        }
    }
}
