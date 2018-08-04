package de.javagl.common.ui.closeable.test;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.javagl.common.ui.closeable.CloseCallback;
import de.javagl.common.ui.closeable.CloseablePanel;

/**
 * Simple integration test for the {@link CloseablePanel} 
 */
@SuppressWarnings("javadoc")
public class CloseablePanelTest
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI()
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel container = new JPanel(new GridLayout(0,1));
        
        CloseablePanel panel0 = new CloseablePanel(
            "Title 0", new JLabel("Panel 0"));
        panel0.setBackground(Color.RED);
        container.add(panel0);
        
        CloseablePanel panel1 = new CloseablePanel(
            new JLabel("Panel 1, without title"));
        panel1.setBackground(Color.GREEN);
        container.add(panel1);
        
        CloseCallback closeCallback = new CloseCallback()
        {
            @Override
            public boolean mayClose(Component componentInTab)
            {
                System.out.println("Checking whether panel may be closed...");
                return false;
            }
        };
        CloseablePanel panel2 = new CloseablePanel("Title 2", 
            new JLabel("Panel 2, may not be closed"), closeCallback);
        panel2.setBackground(Color.YELLOW);
        container.add(panel2);
        
        f.getContentPane().add(container);
        f.setSize(400,200);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
}
