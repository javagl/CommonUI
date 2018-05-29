package de.javagl.common.ui.test;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import de.javagl.common.ui.JTrees;

/**
 * Simple integration test for the {@link JTrees} class 
 */
@SuppressWarnings("javadoc")
public class JTreesTest
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }
    
    private static void createAndShowGui()
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        f.getContentPane().setLayout(new BorderLayout());
        
        JTree tree = new JTree();
        f.getContentPane().add(tree, BorderLayout.CENTER);
        
        JPanel controlPanel = createControlPanel(tree);
        f.getContentPane().add(controlPanel, BorderLayout.EAST);
        
        f.setSize(500, 800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private static JPanel createControlPanel(JTree tree)
    {
        JPanel controlPanel = new JPanel(new GridLayout(0,1,5,5));
        TreeModel treeModel = tree.getModel();
        
        add(controlPanel, "expandAll",
            e -> JTrees.expandAll(tree));
        
        add(controlPanel, "expandAllFixedHeight",
            e -> JTrees.expandAllFixedHeight(tree));
        
        add(controlPanel, "collapseAll",
            e -> JTrees.collapseAll(tree, true));

        add(controlPanel, "countNodes", e -> 
        {
            System.out.println(JTrees.countNodes(treeModel));
        });
        
        add(controlPanel, "findNode", e -> 
        {
            System.out.println(JTrees.findNode(treeModel, "football"));
        });
        
        add(controlPanel, "getChildren", e -> 
        {
            DefaultMutableTreeNode node = 
                JTrees.findNode(treeModel, "sports");
            System.out.println(JTrees.getChildren(treeModel, node));
        });

        add(controlPanel, "getParent", e -> 
        {
            DefaultMutableTreeNode node = 
                JTrees.findNode(treeModel, "football");
            System.out.println(JTrees.getParent(treeModel, node));
        });
        
        add(controlPanel, "getAllNodes", e -> 
        {
            System.out.println(JTrees.getAllNodes(treeModel));
        });
        
        add(controlPanel, "getAllDescendants", e -> 
        {
            DefaultMutableTreeNode node = 
                JTrees.findNode(treeModel, "sports");
            System.out.println(JTrees.getAllDescendants(treeModel, node));
        });

        add(controlPanel, "getLeafNodes", e -> 
        {
            System.out.println(JTrees.getLeafNodes(treeModel));
        });

        add(controlPanel, "getLeafNodes", e -> 
        {
            DefaultMutableTreeNode node = 
                JTrees.findNode(treeModel, "football");
            System.out.println(JTrees.getLeafNodes(treeModel, node));
        });
        
        add(controlPanel, "createTreePathToRoot", e -> 
        {
            DefaultMutableTreeNode node = 
                JTrees.findNode(treeModel, "football");
            System.out.println(JTrees.createTreePathToRoot(treeModel, node));
        });
        
        add(controlPanel, "computeExpandedPaths", e -> 
        {
            System.out.println(JTrees.computeExpandedPaths(tree));
        });
        
        add(controlPanel, "computeIndexInParent", e -> 
        {
            DefaultMutableTreeNode node = 
                JTrees.findNode(treeModel, "football");
            System.out.println(JTrees.computeIndexInParent(node));
        });
        
        return controlPanel;
    }

    private static void add(JPanel panel, String label, ActionListener listener)
    {
        JButton button = new JButton(label);
        button.addActionListener(listener);
        panel.add(button);
    }
}
