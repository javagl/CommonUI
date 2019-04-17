package de.javagl.common.ui.table.test;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import de.javagl.common.ui.table.CustomizedTableHeader;
import de.javagl.common.ui.table.GenericTableModel;
import de.javagl.common.ui.table.MultiColumnRegexFilter;

/**
 * Integration test for the {@link MultiColumnRegexFilter} and 
 * {@link CustomizedTableHeader} classes
 */
@SuppressWarnings("javadoc")
public class TableFilterHeaderTest
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }

    private static void createAndShowGui()
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TableModel tableModel = GenericTableModelTest.createTestTableModel();

        JTable table = new JTable(tableModel);
        
        // Create the TableRowSorter that will receive the RowFilter
        TableRowSorter<TableModel> tableRowSorter = 
            new TableRowSorter<TableModel>(tableModel);
        table.setRowSorter(tableRowSorter);

        // Create the MultiColumnRegexFilter that manages the text
        // fields that are displayed in the table header, and 
        // generates a RowFilter from the regular expressions 
        // that are entered in the text fields
        MultiColumnRegexFilter multiColumnRegexFilter = 
            new MultiColumnRegexFilter(
                filter -> tableRowSorter.setRowFilter(filter));
        
        // Create the table header that shows the text fields from
        // the MultiColumnRegexFilter
        JTableHeader tableHeader = new CustomizedTableHeader(
            table.getColumnModel(), 20,
            multiColumnRegexFilter::createFilterTextField,
            multiColumnRegexFilter::removeFilterTextField);
        table.setTableHeader(tableHeader);
        
        f.getContentPane().add(new JScrollPane(table));
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }


}
