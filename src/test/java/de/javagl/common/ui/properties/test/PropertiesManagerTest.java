/*
 * www.javagl.de - Common - UI
 *
 * Copyright (c) 2013-2019 Marco Hutter - http://www.javagl.de
 */
package de.javagl.common.ui.properties.test;

import java.awt.BorderLayout;
import java.util.function.IntConsumer;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import de.javagl.common.ui.properties.PropertiesHandles;
import de.javagl.common.ui.properties.PropertiesManager;

/**
 * A simple test for the {@link PropertiesManager}
 */
@SuppressWarnings("javadoc")
public class PropertiesManagerTest
{
    /**
     * The properties file name
     */
    private static final String PROPERTIES_FILE_NAME =
        "de.javagl.test.properties";

    /**
     * The entry point of this test
     * 
     * @param args Not used
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> 
        {
            new PropertiesManagerTest();
        });
    }
    
    private JFrame frame;
    private JSplitPane splitPane;
    private JFileChooser fileChooser;
    private JColorChooser colorChooser;
    
    public PropertiesManagerTest()
    {
        // Create the properties manager that will save the properties
        // to a file with the given name
        PropertiesManager pm =
            new PropertiesManager(PropertiesHandles.get(PROPERTIES_FILE_NAME));

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Register a listener to the given frame that will save all
        // properties when the frame is closed
        pm.saveOnClose(frame);

        // Set up some "dummy" components for the test...
        frame.getContentPane().setLayout(new BorderLayout());

        JTextField textField = new JTextField("An example");
        frame.getContentPane().add(textField, BorderLayout.NORTH);
        
        splitPane = new JSplitPane();
        splitPane.setLeftComponent(new JLabel("left"));
        colorChooser = new JColorChooser();
        splitPane.setRightComponent(colorChooser);
        frame.getContentPane().add(splitPane);
        
        frame.setBounds(100, 100, 500, 500);
        
        fileChooser = new JFileChooser();
        JButton openFileChooserButton = new JButton("Open file chooser");
        openFileChooserButton.addActionListener(e -> 
        {
            fileChooser.showOpenDialog(frame);
        });
        frame.getContentPane().add(openFileChooserButton, BorderLayout.SOUTH);

        // (End of setting up "dummy" components for the test)

        // Register the properties that should be managed by the 
        // property manager:
        
        // Register a 'Rectangle' property, namely the 'frame.bounds', to
        // be saved from 'getBounds' when the frame is closed, and to
        // be set via 'setBounds' when the state is restored
        pm.registerRectangle("frame.bounds", 
            frame::getBounds, frame::setBounds);

        // Register an 'Integer' property, namely the divide location of the
        // split pane, to be saved from 'getDividerLocation' when the frame 
        // is closed, and to be set via 'setDividerLocation' on the Event 
        // Dispatch Thread when the state is restored
        IntConsumer setDividerLocationOnEdt = i -> 
        {
            SwingUtilities.invokeLater(() -> 
            {
                splitPane.setDividerLocation(i);
            });
        };
        pm.registerInteger("splitPane.dividerLocation",
            splitPane::getDividerLocation, setDividerLocationOnEdt);
        
        // Register a 'String' property, namely the text of the text field 
        pm.registerString("textField.text", 
            textField::getText, textField::setText);
        
        // Register a 'Path' property, namely the path of the file chooser
        pm.registerPath("path", 
            () -> fileChooser.getCurrentDirectory().toPath(), 
            p -> fileChooser.setCurrentDirectory(p.toFile()));
        
        // Register a 'Color' property, namely the color from the chooser
        pm.registerColor("colorChooser.color", 
            colorChooser::getColor,
            colorChooser::setColor);
        
        // Restore the state that was previously saved to the file
        pm.restore();
        
        frame.setVisible(true);
    }
    
}
