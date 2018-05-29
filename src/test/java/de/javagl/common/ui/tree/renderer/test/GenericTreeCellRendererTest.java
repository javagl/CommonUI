/*
 * www.javagl.de - Common - UI
 *
 * Copyright (c) 2013-2018 Marco Hutter - http://www.javagl.de
 */
package de.javagl.common.ui.tree.renderer.test;

import java.awt.Insets;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

import de.javagl.common.ui.JTrees;
import de.javagl.common.ui.tree.renderer.GenericTreeCellRenderer;

/**
 * Simple integration test for the {@link GenericTreeCellRenderer} class.
 * It uses the {@link JTrees#applyButtonTreeCellRenderer} utility method
 * to generate the renderer.
 */
@SuppressWarnings("javadoc")
public class GenericTreeCellRendererTest
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }
    
    private static void createAndShowGui()
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JTree tree = new JTree();
        attachCellRenderer(tree);
        f.getContentPane().add(tree);
        
        f.setSize(500, 800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
    private static void attachCellRenderer(JTree tree)
    {
        Function<Object, JButton> buttonFactory = nodeObject -> 
        {
            Object userObject = 
                JTrees.getUserObjectFromTreeNode(nodeObject);
            
            if (userObject.equals("sports"))
            {
                return null;
            }
            
            JButton button = new JButton("Button for " + userObject);
            button.setMargin(new Insets(0,0,0,0));
            button.addActionListener(e -> 
            {
                System.out.println("Pressed button for " + userObject);
            });
            return button;
        };
        Function<Object, String> textFactory = nodeObject -> 
        {
            Object userObject = 
                JTrees.getUserObjectFromTreeNode(nodeObject);
            
            if (userObject.equals("sports"))
            {
                return "sports (no button here for demo purposes)";
            }
            
            return String.valueOf(userObject);            
        };
        JTrees.applyButtonTreeCellRenderer(tree, buttonFactory, textFactory);
        
    }
}
