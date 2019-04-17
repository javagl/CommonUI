package de.javagl.common.ui.table.test;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import de.javagl.common.ui.table.TristateTableRowSorter;
import de.javagl.common.ui.table.renderer.SortOrderTableHeaderCellRenderer;

/**
 * Simple integration test for the {@link SortOrderTableHeaderCellRenderer}
 * and the {@link TristateTableRowSorter} 
 */
@SuppressWarnings("javadoc")
public class TableSortingTest
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
        
        Object data[][] = {
            { "a0", "b0", "c0" },
            { "a0", "b0", "c1" },
            { "a0", "b0", "c2" },
            { "a0", "b1", "c0" },
            { "a0", "b1", "c1" },
            { "a0", "b1", "c2" },
            { "a0", "b2", "c0" },
            { "a0", "b2", "c1" },
            { "a0", "b2", "c2" },
        };
        Object[] columnNames = { "A", "B", "C" };
        TableModel tableModel = new DefaultTableModel(data, columnNames);
        
        JTable table = new JTable(tableModel);
        
        TableRowSorter<TableModel> sorter = 
            new TristateTableRowSorter<TableModel>(tableModel);
        table.setRowSorter(sorter);

        table.getTableHeader().setDefaultRenderer(
            new SortOrderTableHeaderCellRenderer(
                table.getTableHeader().getDefaultRenderer()));
        
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(new JLabel(
            "<html>" +
            "Clicking the header of one column will change the sort order " +
            "toggling between 'ascending', 'descending' and 'unsorted' " + 
            "(with the original order).<br>" +
            "<br>" +
            "The arrows in the header indicate the sorting criterion " + 
            "columns, for up to 4 columns.<br>" +
            "<br>" +
            "</html>"
        ), BorderLayout.NORTH);
        f.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
