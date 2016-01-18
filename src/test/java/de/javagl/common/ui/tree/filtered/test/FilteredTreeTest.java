package de.javagl.common.ui.tree.filtered.test;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import de.javagl.common.ui.JTrees;
import de.javagl.common.ui.tree.filtered.FilteredTree;
import de.javagl.common.ui.tree.filtered.TreeModelFilters;

/**
 * Simple integration test for the {@link FilteredTree}
 */
@SuppressWarnings("javadoc")
public class FilteredTreeTest
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(
            () -> createAndShowGUI());
    }
    
    private static void createAndShowGUI()
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JTextField textField = new JTextField();
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(textField, BorderLayout.NORTH);

        TreeModel originalTreeModel = createTestTreeModel();
        int originalNumberOfNodes = JTrees.countNodes(originalTreeModel);
        FilteredTree filteredTree = 
            FilteredTree.create(originalTreeModel);
        JTree tree = filteredTree.getTree();
        
        f.getContentPane().add(new JScrollPane(tree), BorderLayout.CENTER);
        
        JLabel infoLabel = 
            new JLabel("Number of nodes: "+originalNumberOfNodes);
        f.getContentPane().add(infoLabel, BorderLayout.SOUTH);

        textField.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                update();
            }
            
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                update();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e)
            {
                update();
            }
            
            private void update()
            {
                String s = textField.getText();
                if (s == null || s.trim().length() == 0)
                {
                    filteredTree.setFilter(null);
                    infoLabel.setText(
                        "Number of nodes: "+originalNumberOfNodes);
                }
                else
                {
                    filteredTree.setFilter(
                        TreeModelFilters.containsStringIgnoreCase(s));
                    
                    TreeModel filteredModel = filteredTree.getFilteredModel();
                    int filteredNumberOfNodes = 
                        JTrees.countNodes(filteredModel);
                    infoLabel.setText(
                        "Number of nodes: "+originalNumberOfNodes+", "+
                        "in filtered tree: "+filteredNumberOfNodes);
                }
            }
        });
        
        f.setSize(500,500);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
    private static TreeModel createTestTreeModel() 
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("JTree");
        addNodes(root, 0, 5, 8, 10);
        return new DefaultTreeModel(root);
    }
    
    private static void addNodes(DefaultMutableTreeNode node, 
        int depth, int maxDepth, int count, int leafCount)
    {
        if (depth == maxDepth)
        {
            return;
        }
        for (int i=0; i<leafCount; i++)
        {
            DefaultMutableTreeNode leaf = 
                new DefaultMutableTreeNode("depth_"+depth+"_leaf_"+i);
            node.add(leaf);
        }
        if (depth < maxDepth - 1)
        {
            for (int i=0; i<count; i++)
            {
                DefaultMutableTreeNode child = 
                    new DefaultMutableTreeNode("depth_"+depth+"_node_"+i);
                node.add(child);
                addNodes(child, depth+1, maxDepth, count, leafCount);
            }
        }
        
    }
    
    
}


