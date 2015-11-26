package de.javagl.common.ui.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import de.javagl.common.ui.LocationBasedAction;
import de.javagl.common.ui.LocationBasedPopupHandler;

/**
 * Simple integration test for the {@link LocationBasedAction}
 * and the {@link LocationBasedPopupHandler} 
 */
@SuppressWarnings("javadoc")
public class LocationBasedActionTest
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
        
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(new ExampleAction());
        table.addMouseListener(new LocationBasedPopupHandler(popupMenu));
        
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(new JLabel(
            "<html>" +
            "Opening a popup menu on one table entry will offer an " +
            "action that depends on the clicked table cell." + 
            "<br>" +
            "</html>"
        ), BorderLayout.NORTH);
        f.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    /**
     * A {@link LocationBasedAction} for this test
     */
    private static class ExampleAction extends LocationBasedAction
    {
        @Override
        protected void prepareShow(Component component, int x, int y)
        {
            JTable table = (JTable)component;
            Point p = new Point(x,y);
            int row = table.rowAtPoint(p);
            int col = table.columnAtPoint(p);
            if (row != -1 && col != -1)
            {
                String columnName = table.getColumnName(col);
                Object value = table.getValueAt(row, col);
                putValue(NAME, 
                    "Action for column " + columnName + ", value " + value);
            }
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("Executed: "+getValue(NAME));
        }
    }

}
