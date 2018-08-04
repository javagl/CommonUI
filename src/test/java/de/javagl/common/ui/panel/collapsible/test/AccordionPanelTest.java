package de.javagl.common.ui.panel.collapsible.test;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

import de.javagl.common.ui.JSplitPanes;
import de.javagl.common.ui.panel.collapsible.AccordionPanel;

/**
 * Simple integration test for the {@link AccordionPanel} class
 */
@SuppressWarnings("javadoc")
public class AccordionPanelTest
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }
    
    private static void createAndShowGui()
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        AccordionPanel a0 = new AccordionPanel();
        a0.addToAccordion("A tree", createExamplePanelA());
        a0.addToAccordion("Some buttons", createExamplePanelB(), true);
        a0.addToAccordion("A text area", createExamplePanelC());
        
        AccordionPanel a1 = new AccordionPanel();
        a1.addToAccordion("Some buttons", createExamplePanelB(), true);
        a1.addToAccordion("A text area", createExamplePanelC());
        a1.addToAccordion("A tree", createExamplePanelA());
        
        splitPane.setLeftComponent(a0);
        splitPane.setRightComponent(a1);
        
        JSplitPanes.setDividerLocation(splitPane, 0.5);
        
        f.getContentPane().add(splitPane);
        f.setSize(1000, 800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
    private static JPanel createExamplePanelA()
    {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel("Example"));
        p.add(new JScrollPane(new JTree()));
        return p;
    }
    
    private static JPanel createExamplePanelB()
    {
        JPanel p = new JPanel(new GridLayout(0,2));
        p.add(new JButton("These"));
        p.add(new JButton("buttons"));
        p.add(new JButton("do"));
        p.add(new JButton("not"));
        p.add(new JButton("do"));
        p.add(new JButton("anything"));
        return p;
    }
    
    private static JPanel createExamplePanelC()
    {
        JPanel p = new JPanel(new GridLayout(1,1));
        JTextArea textArea = new JTextArea(10, 50);
        for (int i=0; i<50; i++)
        {
            textArea.append("All work an no play makes Marco a dull boy\n");
        }
        p.add(new JScrollPane(textArea));
        return p;
    }
    
}
