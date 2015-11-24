package de.javagl.common.ui.test;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import de.javagl.common.ui.CloseableTab;
import de.javagl.common.ui.CloseableTab.CloseCallback;

/**
 * Simple integration test for the {@link CloseableTab} 
 */
@SuppressWarnings("javadoc")
public class CloseableTabTest
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

        JTabbedPane tabbedPane = new JTabbedPane();
        CloseableTab.addTo(tabbedPane, "Tab0", new JLabel("Tab0"), true);
        CloseableTab.addTo(tabbedPane, "Tab1", new JLabel("Tab1"), false);
        CloseableTab.addTo(tabbedPane, "Tab2", new JLabel("Tab2"), true);
        CloseableTab.addTo(tabbedPane, "Tab3", new JLabel("Tab2"), 
            new CloseCallback()
            {
                @Override
                public boolean mayClose(Component componentInTab)
                {
                    System.out.println("Checking whether tab may be closed...");
                    return false;
                }
            });

        f.getContentPane().add(tabbedPane);
        f.setSize(400,200);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
}
