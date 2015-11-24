package de.javagl.common.ui.utils.desktop.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.javagl.common.ui.utils.desktop.JDesktopPaneLayout;

/**
 * Integration test of the {@link JDesktopPaneLayout} class
 */
@SuppressWarnings("javadoc")
public class JDesktopPaneLayoutTest
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
        f.getContentPane().setLayout(new BorderLayout());
        
        final JDesktopPane desktopPane = new JDesktopPane();
        f.getContentPane().add(desktopPane, BorderLayout.CENTER);
        
        final JInternalFrame f0 = 
            new JInternalFrame("F0", true, true, true, true);
        final JInternalFrame f1 = 
            new JInternalFrame("F1", true, true, true, true);
        final JInternalFrame f2 = 
            new JInternalFrame("F2", true, true, true, true);
        final JInternalFrame f3 = 
            new JInternalFrame("F3", true, true, true, true);
        final JInternalFrame f4 = 
            new JInternalFrame("F4", true, true, true, true);

        f0.setVisible(true);
        f1.setVisible(true);
        f2.setVisible(true);
        f3.setVisible(true);
        f4.setVisible(true);

        f0.setBounds(10,10,150,150);
        f1.setBounds(20,20,150,150);
        f2.setBounds(30,30,150,150);
        f3.setBounds(40,40,150,150);
        f4.setBounds(50,50,150,150);
        
        f0.setPreferredSize(new Dimension(200,200));
        f1.setPreferredSize(new Dimension(200,200));
        f2.setPreferredSize(new Dimension(200,200));
        f3.setPreferredSize(new Dimension(200,200));
        f4.setPreferredSize(new Dimension(200,200));
        
        desktopPane.add(f0);
        desktopPane.add(f1);
        desktopPane.add(f2);
        desktopPane.add(f3);
        desktopPane.add(f4);
        
        JPanel buttonPanel = new JPanel();
        f.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        JButton gridButton = new JButton("Grid");
        buttonPanel.add(gridButton);
        gridButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JDesktopPaneLayout d = new JDesktopPaneLayout(desktopPane);
                d.setLayout(new GridLayout(0,2,4,4));
                d.add(f0);
                d.add(f1);
                d.add(f2);
                d.add(f3);
                d.add(f4);
                d.validate();
            }
        });

        JButton borderButton = new JButton("Border");
        buttonPanel.add(borderButton);
        borderButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JDesktopPaneLayout d = new JDesktopPaneLayout(desktopPane);
                d.setLayout(new BorderLayout());
                d.add(f0, BorderLayout.CENTER);
                d.add(f1, BorderLayout.EAST);
                d.add(f2, BorderLayout.WEST);
                d.add(f3, BorderLayout.SOUTH);
                d.add(f4, BorderLayout.NORTH);
                d.validate();
            }
        });
        
        JButton flowButton = new JButton("Flow");
        buttonPanel.add(flowButton);
        flowButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JDesktopPaneLayout d = new JDesktopPaneLayout(desktopPane);
                d.setLayout(new FlowLayout());
                d.add(f0);
                d.add(f1);
                d.add(f2);
                d.add(f3);
                d.add(f4);
                d.validate();
            }
        });
        
        JButton gridBagButton = new JButton("GridBag");
        buttonPanel.add(gridBagButton);
        gridBagButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JDesktopPaneLayout d = new JDesktopPaneLayout(desktopPane);
                d.setLayout(new GridBagLayout());
                
                GridBagConstraints c = new GridBagConstraints();
                c.weightx = 1.0;
                c.weighty = 1.0;
                c.fill = GridBagConstraints.BOTH;

                c.gridx = 0;
                c.gridy = 0;
                c.weightx = 0.0;
                c.gridwidth = 3;
                d.add(f0, c);

                c.gridx = 0;
                c.gridy = 1;
                c.weightx = 0.6;
                c.gridwidth = 2;
                d.add(f1, c);

                c.gridx = 2;
                c.weightx = 0.3;
                c.gridwidth = 1;
                d.add(f2, c);

                c.gridy = 2;
                c.gridx = 0;
                c.weightx = 0.3;
                c.gridwidth = 1;
                d.add(f3, c);

                c.gridx = 1;
                c.weightx = 0.6;
                c.gridwidth = 2;
                d.add(f4, c);
                d.validate();
            }
        });
        
        f.setSize(600,600);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
