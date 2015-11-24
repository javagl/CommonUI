package de.javagl.common.ui.test;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.javagl.common.ui.JSpinners;

/**
 * Simple integration test for 
 * {@link JSpinners#setSpinnerDraggingEnabled(javax.swing.JSpinner, boolean)} 
 */
@SuppressWarnings("javadoc")
public class SpinnerDraggingTest
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
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel p = new JPanel(new GridLayout(1,0));

        SpinnerModel spinnerModel0 = 
            new SpinnerNumberModel(0.0, -10000.0, 10000.0, 1.0);
        SpinnerModel spinnerModel1 = 
            new SpinnerNumberModel(0.0, -0.1, 0.1, 0.001);
        SpinnerModel spinnerModel2 = 
            new SpinnerNumberModel(0, -100, 100, 1);

        p.add(createSpinnerTestPanel(spinnerModel0));
        p.add(createSpinnerTestPanel(spinnerModel1));
        p.add(createSpinnerTestPanel(spinnerModel2));
        
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(p, BorderLayout.NORTH);
        
        f.setSize(800,200);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
    private static JPanel createSpinnerTestPanel(SpinnerModel spinnerModel)
    {
        JPanel p = new JPanel(new GridLayout(0,1));
        
        JSpinner spinner = new JSpinner(spinnerModel);
        JSpinners.setSpinnerDraggingEnabled(spinner, true);
        p.add(spinner);
        
        JLabel label1 = new JLabel("0");
        spinner.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                label1.setText(String.valueOf(spinner.getValue()));
            }
        });
        p.add(label1);
        
        return p;
    }
    
}
