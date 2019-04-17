package de.javagl.common.ui.text.test;


import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import de.javagl.common.ui.text.UndoRedoHandler;

/**
 * Simple integration test for the {@link UndoRedoHandler} class
 */
@SuppressWarnings("javadoc")
public class UndoRedoHandlerTest
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
        UndoRedoHandler handler = UndoRedoHandler.attach(textArea);
        f.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
        
        JButton detachButton = new JButton("Detach");
        detachButton.addActionListener(e -> handler.detach());
        f.getContentPane().add(detachButton, BorderLayout.SOUTH);
        
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
