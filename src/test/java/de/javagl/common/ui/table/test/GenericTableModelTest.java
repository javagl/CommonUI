package de.javagl.common.ui.table.test;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

import de.javagl.common.ui.table.GenericTableModel;

/**
 * Simple integration test for the {@link GenericTableModel} class
 */
@SuppressWarnings("javadoc")
public class GenericTableModelTest
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }

    private static void createAndShowGui()
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TableModel tableModel = createTestTableModel();
        JTable table = new JTable(tableModel);
        
        f.getContentPane().add(new JScrollPane(table));
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
    static TableModel createTestTableModel()
    {
        GenericTableModel tableModel = new GenericTableModel();
        tableModel.addColumn("Name", String.class, 
            Person::getName, Person::setName);
        tableModel.addColumn("Age", Integer.class, 
            Person::getAge, Person::setAge);
        tableModel.addColumn("Height", Float.class, 
            Person::getHeight, Person::setHeight);
        
        tableModel.addRow(new Person("Abc", 12, 1.23f));
        tableModel.addRow(new Person("Cde", 23, 1.45f));
        tableModel.addRow(new Person("Efg", 34, 1.56f));
        tableModel.addRow(new Person("Ghi", 45, 1.78f));
        tableModel.addRow(new Person("Ijk", 56, 1.90f));
        
        return tableModel;
    }
    
}
