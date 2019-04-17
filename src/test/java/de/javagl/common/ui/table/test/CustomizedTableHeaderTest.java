package de.javagl.common.ui.table.test;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Vector;
import java.util.function.IntFunction;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import de.javagl.common.ui.table.CustomizedTableHeader;

/**
 * A simple test for the CustomizedTableHeader
 */
@SuppressWarnings("javadoc")
public class CustomizedTableHeaderTest
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
        
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);

        IntFunction<JComponent> componentFactory = columnIndex -> 
        {
            JTextField textField = new JTextField(String.valueOf(columnIndex));
            return textField;
        };
        JTableHeader tableHeader = new CustomizedTableHeader(
            table.getColumnModel(), 20, componentFactory, null);
        table.setTableHeader(tableHeader);
        
        f.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        
        tableModel.addColumn("column "+tableModel.getColumnCount());
        tableModel.addColumn("column "+tableModel.getColumnCount());
        tableModel.addColumn("column "+tableModel.getColumnCount());
        tableModel.addRow(new Vector<Object>());
        tableModel.addRow(new Vector<Object>());
        tableModel.addRow(new Vector<Object>());
        
        
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton addColumnButton = new JButton("Add column");
        addColumnButton.addActionListener(e -> 
        {
            tableModel.addColumn("column "+tableModel.getColumnCount());
        });
        buttonPanel.add(addColumnButton);

        JButton removeColumnButton = new JButton("Remove column");
        removeColumnButton.addActionListener(e -> 
        {
            tableModel.setColumnCount(tableModel.getColumnCount()-1);
        });
        buttonPanel.add(removeColumnButton);
        
        JButton addRowButton = new JButton("Add row");
        addRowButton.addActionListener(e -> 
        {
            tableModel.addRow(new Vector<Object>());
        });
        buttonPanel.add(addRowButton);
        
        
        f.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        f.setSize(800, 600);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
