package de.javagl.common.ui.tree.checkbox.test;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import de.javagl.common.ui.tree.checkbox.CheckBoxTree;
import de.javagl.common.ui.tree.checkbox.CheckBoxTree.State;
import de.javagl.common.ui.tree.checkbox.CheckBoxTree.StateListener;

/**
 * Simple integration test of the {@link CheckBoxTree}
 */
@SuppressWarnings("javadoc")
public class CheckBoxTreeTest
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
        JFrame f = new JFrame("Test");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setLayout(new BorderLayout());

        CheckBoxTree tree = new CheckBoxTree(getDefaultTreeModel());
        f.getContentPane().add(new JScrollPane(tree));

        tree.addStateListener(new StateListener()
        {
            @Override
            public void
                stateChanged(Object node, State oldState, State newState)
            {
                System.out.println(
                    "State of " + node + " changed from " + 
                        oldState + " to " + newState);
            }
        });

        
        JPanel panel = new JPanel(new FlowLayout());
        JButton selectButton = new JButton("Select child 1");
        selectButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Object root = tree.getModel().getRoot();
                Object child = tree.getModel().getChild(root, 1);
                tree.setSelectionState(child, State.SELECTED);
            }
        });
        panel.add(selectButton);

        JButton unselectButton = new JButton("Unselect child 1");
        unselectButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Object root = tree.getModel().getRoot();
                Object child = tree.getModel().getChild(root, 1);
                tree.setSelectionState(child, State.UNSELECTED);
            }
        });
        panel.add(unselectButton);
        
        f.getContentPane().add(panel, BorderLayout.SOUTH);
        
        f.setSize(600, 600);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    protected static TreeModel getDefaultTreeModel()
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("JTree");
        DefaultMutableTreeNode parent;

        parent = new DefaultMutableTreeNode("colors");
        root.add(parent);
        parent.add(new DefaultMutableTreeNode("blue"));
        parent.add(new DefaultMutableTreeNode("violet"));
        parent.add(new DefaultMutableTreeNode("red"));
        parent.add(new DefaultMutableTreeNode("yellow"));

        parent = new DefaultMutableTreeNode("sports");
        root.add(parent);
        parent.add(new DefaultMutableTreeNode("basketball"));
        parent.add(new DefaultMutableTreeNode("soccer"));
        parent.add(new DefaultMutableTreeNode("football"));
        parent.add(new DefaultMutableTreeNode("hockey"));

        parent = new DefaultMutableTreeNode("food");
        root.add(parent);
        parent.add(new DefaultMutableTreeNode("hot dogs"));
        parent.add(new DefaultMutableTreeNode("pizza"));
        parent.add(new DefaultMutableTreeNode("ravioli"));
        parent.add(new DefaultMutableTreeNode("bananas"));
        return new DefaultTreeModel(root);
    }

}
