/*
 * www.javagl.de - Common - UI
 *
 * Copyright (c) 2013-2018 Marco Hutter - http://www.javagl.de
 */
package de.javagl.common.ui.layout.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Locale;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;

import de.javagl.common.ui.layout.AspectLayout;

/**
 * Simple integration test for the {@link AspectLayout} class 
 */
@SuppressWarnings("javadoc")
public class AspectLayoutTest
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }

    private static void createAndShowGui()
    {
        javax.swing.JFrame f = new javax.swing.JFrame();
        f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        
        JPanel container = new JPanel(new AspectLayout(1.0));
        container.add(createTestPanel(Color.GREEN));
        
        JPanel controlPanel = createControlPanel(container);
        
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(container, BorderLayout.CENTER);
        f.getContentPane().add(controlPanel, BorderLayout.SOUTH);
        
        f.setSize(800,600);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
    private static JPanel createControlPanel(JPanel container)
    {
        JPanel controlPanel = new JPanel();
        
        JSpinner aspectSpinner = new JSpinner(
            new SpinnerNumberModel(1.0, 0.25, 4.0, 0.25));
        JSpinner alignmentSpinner = new JSpinner(
            new SpinnerNumberModel(0.5, 0.0, 1.0, 0.1));
        JSpinner.DefaultEditor editor = 
            (JSpinner.DefaultEditor)alignmentSpinner.getEditor();
        editor.getTextField().setColumns(4);
        
        ChangeListener changeListener = e -> 
        {
            Object aspectObject = aspectSpinner.getValue();
            Number aspectNumber = (Number) aspectObject;
            double aspect = aspectNumber.doubleValue();
            Object alignmentObject = alignmentSpinner.getValue();
            Number alignmentNumber = (Number) alignmentObject;
            double alignment = alignmentNumber.doubleValue();
            container.setLayout(new AspectLayout(aspect, alignment));
            container.validate();
            container.repaint();
        };
        aspectSpinner.addChangeListener(changeListener);
        alignmentSpinner.addChangeListener(changeListener);
        
        Random random = new Random(0);
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> 
        {
            int colorR = 128 + random.nextInt(128);
            int colorG = 128 + random.nextInt(128);
            int colorB = 128 + random.nextInt(128);
            JPanel panel = createTestPanel(new Color(colorR, colorG, colorB));
            container.add(panel);
            container.validate();
            container.repaint();
        });

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> 
        {
            int n = container.getComponentCount();
            if (n > 0)
            {
                container.remove(n - 1);
            }
            container.validate();
            container.repaint();
        });
        
        controlPanel.add(new JLabel("Aspect:"));
        controlPanel.add(aspectSpinner);
        controlPanel.add(new JLabel("Alignment:"));
        controlPanel.add(alignmentSpinner);
        controlPanel.add(addButton);
        controlPanel.add(removeButton);
        
        return controlPanel;
    }

    private static JPanel createTestPanel(Color color)
    {
        JPanel p = new JPanel()
        {
            /**
             * Serial UID
             */
            private static final long serialVersionUID = 5813408923982201316L;

            @Override
            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                double aspect = (double)getWidth()/getHeight();
                String infoString = String.format(Locale.ENGLISH,
                    "w: %d, h: %d, aspect: %.3f", 
                    getWidth(), getHeight(), aspect);
                g.drawString(infoString, 10, 20);
            }
        };
        p.setBackground(color);
        return p;
    }
}
