/*
 * www.javagl.de - Common - UI
 *
 * Copyright (c) 2013-2019 Marco Hutter - http://www.javagl.de
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.common.ui.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Class to handle {@link PropertiesHandle} instances
 */
public class PropertiesHandles
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(PropertiesHandles.class.getName());

    /**
     * The current handle instances
     */
    private static final Map<String, PropertiesHandle> INSTANCES =
         new LinkedHashMap<String, PropertiesHandle>();
    
    /**
     * Returns the {@link PropertiesHandle} for the given properties file
     * name, creating it if it does not exist yet
     * 
     * @param propertiesFileName The properties file name
     * @return The {@link PropertiesHandle}
     */
    public static synchronized PropertiesHandle get(String propertiesFileName)
    {
        return INSTANCES.computeIfAbsent(propertiesFileName, 
            n -> new DefaultPropertiesHandle(propertiesFileName));
    }

    /**
     * Read the properties file with the given name. If the specified
     * file does not exist, then a new properties object is returned.
     * If the file cannot be read, then a warning is printed and a 
     * new properties object is returned.
     * 
     * @param fileName The file name
     * @return The properties
     */
    static Properties readPropertiesUnchecked(String fileName)
    {
        File file = new File(fileName);
        if (!file.exists())
        {
            return new Properties();
        }
        try (InputStream in = new FileInputStream(file))
        {
            Properties properties = new Properties();
            properties.load(in);
            return properties;
        }
        catch (IOException e)
        {
            logger.warning("Could not read properties: " + e);
        }
        return new Properties();
    }

    /**
     * Write the given properties to the specified file. If the given
     * properties object is <code>null</code>, a warning will be printed
     * and nothing will be done. If the properties file can not be
     * written, a warning will be printed.
     * 
     * @param properties The properties
     * @param fileName The file name
     */
    static void writePropertiesUnchecked(
        Properties properties, String fileName)
    {
        if (properties == null)
        {
            logger.warning("The given properties object is null");
            return;
        }
        try (OutputStream out = new FileOutputStream(fileName))
        {
            properties.store(out, "Properties");
        }
        catch (IOException e)
        {
            logger.warning("Could not write properties: " + e);
        }
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private PropertiesHandles()
    {
        // Private constructor to prevent instantiation
    }
}
