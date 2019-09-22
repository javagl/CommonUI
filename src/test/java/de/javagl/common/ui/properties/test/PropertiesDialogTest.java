/*
 * www.javagl.de - Common - UI
 *
 * Copyright (c) 2013-2019 Marco Hutter - http://www.javagl.de
 */
package de.javagl.common.ui.properties.test;

import java.util.Map;

import javax.swing.SwingUtilities;

import de.javagl.common.ui.properties.PropertiesDialog;

/**
 * Simple test for the {@link PropertiesDialog}
 */
@SuppressWarnings("javadoc")
public class PropertiesDialogTest
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }
    
    private static void createAndShowGui()
    {
        PropertiesDialog propertiesDialog = 
            PropertiesDialog.create(null, "Test")
            .addProperty("property0", 
                "The first property. May be empty:")
            .addProperty("property1", 
                "The second. This may not be empty:", 
                "default value", s -> !s.isEmpty())
            .addProperty("property2", 
                "The third property. This must be 3 letters long:", null,
                s -> s.length() == 3)
            .addProperty("property3", 
                "The fourth property:", null,
                s -> s.length() == 4, s -> "This must be 4 letters long")
            .addProperty("property4", 
                "The fifth property:", "1234",
                s -> isInteger(s), s -> "This must be an integer")
            .build();
        
        Map<String, String> propertyValues = propertiesDialog.showDialog();
        System.out.println("Got " + propertyValues);
    }
    
    private static boolean isInteger(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

}
