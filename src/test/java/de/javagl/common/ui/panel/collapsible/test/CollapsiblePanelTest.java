package de.javagl.common.ui.panel.collapsible.test;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

import de.javagl.common.ui.JScrollPanes;
import de.javagl.common.ui.panel.collapsible.CollapsiblePanel;

/**
 * Simple integration test of the {@link CollapsiblePanel}
 */
@SuppressWarnings("javadoc")
public class CollapsiblePanelTest
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
        
        
        final CollapsiblePanel p0 = new CollapsiblePanel("Panel 0", true);
        CollapsiblePanel p1 = new CollapsiblePanel("Panel 1", true);
        CollapsiblePanel p2 = new CollapsiblePanel("Panel 2");

        fill(p0);
        fill(p1);
        fill(p2);
        
        
        JButton b = new JButton("Add");
        b.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                p0.add(new JButton("Test"));
                p0.revalidate();
            }
        });
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        
        panel.add(p0, c);
        panel.add(p1, c);
        panel.add(p2, c);
        panel.add(b, c);
        JComponent container = 
            JScrollPanes.createVerticalScrollPane(panel);
        
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(container, BorderLayout.NORTH);
        
        f.setSize(300,600);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
    
    
    
    private static void fill(JPanel p)
    {
        JPanel pp = new JPanel(new BorderLayout());
        pp.setLayout(new BorderLayout());
        pp.add(new JLabel("Label"), BorderLayout.NORTH);
        pp.add(new JTree(), BorderLayout.CENTER);
        pp.add(new JButton("Button"), BorderLayout.SOUTH);
        p.setLayout(new GridLayout(0,1));
        p.add(pp);
    }
}
