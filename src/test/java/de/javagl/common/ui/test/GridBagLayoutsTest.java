package de.javagl.common.ui.test;

import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.javagl.common.ui.GridBagLayouts;

/**
 * Simple integration test for the {@link GridBagLayouts} class
 */
@SuppressWarnings("javadoc")
public class GridBagLayoutsTest
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }
    
    private static void createAndShowGui()
    {
        JFrame f = new JFrame();
        
        JPanel panel = new JPanel(new GridBagLayout());
        int row = 0;
        GridBagLayouts.addRow(panel, row++, 1, 
            createLabel("R0----------C0"), 
            createLabel("R0C1"), 
            createLabel("R0C2"));
        GridBagLayouts.addRow(panel, row++, 1, 
            createLabel("R1C0"), 
            createLabel("R1C1"), 
            createLabel("R1----------C2"));
        GridBagLayouts.addRow(panel, row++, 1, 
            createLabel("R2----------C0"), 
            createLabel("R2C1"), 
            createLabel("R2----------C2"));
        
        f.getContentPane().add(panel);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
    private static JLabel createLabel(String text)
    {
        JLabel label = new JLabel(text);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return label;
    }
}
