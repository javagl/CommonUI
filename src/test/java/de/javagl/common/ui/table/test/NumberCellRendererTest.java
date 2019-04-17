package de.javagl.common.ui.table.test;

import java.awt.Color;
import java.util.function.DoubleFunction;
import java.util.function.Function;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import de.javagl.common.ui.table.renderer.NumberBackgroundColorTableCellRenderer;

/**
 * Simple integration test for the 
 * {@link NumberBackgroundColorTableCellRenderer} class
 */
@SuppressWarnings("javadoc")
public class NumberCellRendererTest
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
        
        TableColumn column = table.getColumnModel().getColumn(2);
        double min = 1.23;
        double max = 1.90;
        DoubleFunction<Color> colorFunction = v -> 
        {
            int i = (int)Math.max(0, Math.min(255, v * 255));
            return new Color(i, i, i);
        };
        Function<Number, String> numberFormatter = Object::toString;
        column.setCellRenderer(new NumberBackgroundColorTableCellRenderer(
            min, max, colorFunction, numberFormatter));
        
        f.getContentPane().add(new JScrollPane(table));
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
