package de.javagl.common.ui.text.test;


import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import de.javagl.common.ui.text.SearchableTextComponent;

/**
 * Simple integration test for the {@link SearchableTextComponent} class
 */
@SuppressWarnings("javadoc")
public class SearchableTextComponentTest
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }

    private static void createAndShowGui()
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextArea textArea = new JTextArea(10, 30);
        textArea.setText(
            "Hit CTRL+F to open the search tool." + "\n" 
            + "Hit F3 to find the next appearance." + "\n"
            + "Here is some more text and text and text" + "\n"
            + "that can be used for the test and test and test...");

        SearchableTextComponent searchableTextComponent = 
            new SearchableTextComponent(textArea);

        f.getContentPane().add(new JScrollPane(searchableTextComponent), 
            BorderLayout.CENTER);
        
        f.setSize(800, 600);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
