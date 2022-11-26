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

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Default implementation of a {@link PropertiesHandle}
 */
class DefaultPropertiesHandle implements PropertiesHandle
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(DefaultPropertiesHandle.class.getName());

    /**
     * A constant for the suffix of the x-coordinate of a point
     */
    private static final String POINT_X = "x";

    /**
     * A constant for the suffix of the y-coordinate of a point
     */
    private static final String POINT_Y = "y";
    
    /**
     * A constant for the suffix of the x-coordinate of a rectangle
     */
    private static final String RECTANGLE_X = "x";

    /**
     * A constant for the suffix of the y-coordinate of a rectangle
     */
    private static final String RECTANGLE_Y = "y";

    /**
     * A constant for the suffix of the width of a rectangle
     */
    private static final String RECTANGLE_W = "width";

    /**
     * A constant for the suffix of the height of a rectangle
     */
    private static final String RECTANGLE_H = "height";

    /**
     * A constant for the suffix of the r-component of a color
     */
    private static final String COLOR_R = "r";

    /**
     * A constant for the suffix of the g-component of a color
     */
    private static final String COLOR_G = "g";

    /**
     * A constant for the suffix of the b-component of a color
     */
    private static final String COLOR_B = "b";

    /**
     * A constant for the suffix of the b-component of a color
     */
    private static final String COLOR_A = "a";

    /**
     * The name of the properties file
     */
    private final String propertiesFileName;
    
    /**
     * The properties
     */
    private final Properties properties;
    
    /**
     * Default constructor 
     * 
     * @param propertiesFileName The properties file name
     */
    DefaultPropertiesHandle(String propertiesFileName)
    {
        this.propertiesFileName = Objects.requireNonNull(
            propertiesFileName, "The propertiesFileName may not be null");
        this.properties = 
            PropertiesHandles.readPropertiesUnchecked(propertiesFileName);
    }
    
    /**
     * Write the current properties
     */
    private void writePropertiesUnchecked()
    {
        PropertiesHandles.writePropertiesUnchecked(
            properties, propertiesFileName);
    }
    
    
    @Override
    public void writeString(String name, String value)
    {
        if (value == null)
        {
            return;
        }
        properties.put(name, value);
        writePropertiesUnchecked();
    }
    
    @Override
    public void saveString(
        String name, Supplier<? extends String> supplier)
    {
        writeString(name, supplier.get());
    }
    
    @Override
    public String readString(String name)
    {
        String s = properties.getProperty(name);
        return s;
    }
    
    @Override
    public void restoreString(
        String name, Consumer<? super String> consumer)
    {
        String value = readString(name);
        if (value != null)
        {
            consumer.accept(value);            
        }
    }
    
    
    
    @Override
    public void writeInteger(
        String name, Integer value)
    {
        if (value == null)
        {
            return;
        }
        properties.put(name, String.valueOf(value));
        writePropertiesUnchecked();
    }

    @Override
    public void saveInteger(
        String name, IntSupplier supplier)
    {
        writeInteger(name, supplier.getAsInt());
    }

    @Override
    public Integer readInteger(
        String name)
    {
        String s = properties.getProperty(name);
        if (s == null)
        {
            return null;
        }
        try
        {
            int value = Integer.parseInt(s);
            return value;
        }
        catch (NumberFormatException e)
        {
            logger.warning(e.toString());
            return null;
        }
    }
    
    @Override
    public void restoreInteger(
        String name, IntConsumer consumer)
    {
        Integer value = readInteger(name);
        if (value != null)
        {
            consumer.accept(value);
        }
    }
    
    @Override
    public void writeDouble(
        String name, Double value)
    {
        if (value == null)
        {
            return;
        }
        properties.put(name, String.valueOf(value));
        writePropertiesUnchecked();
    }
    
    @Override
    public void saveDouble(
        String name, DoubleSupplier supplier)
    {
        writeDouble(name, supplier.getAsDouble());
    }

    @Override
    public Double readDouble(
        String name)
    {
        String s = properties.getProperty(name);
        if (s == null)
        {
            return null;
        }
        try
        {
            double value = Double.parseDouble(s);
            return value;
        }
        catch (NumberFormatException e)
        {
            logger.warning(e.toString());
            return null;
        }
    }
    
    @Override
    public void restoreDouble(
        String name, DoubleConsumer consumer)
    {
        Double d = readDouble(name);
        if (d != null)
        {
            consumer.accept(d);
        }
    }
    
    
    
    
    
    @Override
    public void writePath(String name, Path value)
    {
        if (value == null)
        {
            return;
        }
        String s = value.normalize().toString();
        properties.put(name, s);
        writePropertiesUnchecked();
    }
    
    @Override
    public void savePath(
        String name, Supplier<? extends Path> supplier)
    {
        writePath(name, supplier.get());
    }
    
    @Override
    public Path readPath(String name)
    {
        String s = properties.getProperty(name);
        if (s == null)
        {
            return null;
        }
        try
        {
            Path value = Paths.get(s);
            return value;
        }
        catch (InvalidPathException e)
        {
            logger.warning(e.toString());
            return null;
        }
    }
    
    @Override
    public void restorePath(
        String name, Consumer<? super Path> consumer)
    {
        Path value = readPath(name);
        if (value != null)
        {
            consumer.accept(value);            
        }
    }


    
    
    @Override
    public void writePoint(
        String prefix, Point value)
    {
        if (value == null)
        {
            return;
        }
        properties.put(prefix + "." + POINT_X, String.valueOf(value.x));
        properties.put(prefix + "." + POINT_Y, String.valueOf(value.y));
        writePropertiesUnchecked();
    }
    
    @Override
    public void savePoint(
        String prefix, Supplier<? extends Point> supplier)
    {
        writePoint(prefix, supplier.get());
    }

    @Override
    public Point readPoint(
        String prefix)
    {
        String sx = properties.getProperty(prefix + "." + POINT_X);
        String sy = properties.getProperty(prefix + "." + POINT_Y);
        if (Arrays.asList(sx, sy).contains(null))
        {
            return null;
        }
        try
        {
            int x = Integer.parseInt(sx);
            int y = Integer.parseInt(sy);
            Point value = new Point(x, y);
            return value;
        }
        catch (NumberFormatException e)
        {
            logger.warning(e.toString());
            return null;
        }
    }
    
    @Override
    public void restorePoint(
        String prefix, Consumer<? super Point> consumer)
    {
        Point value = readPoint(prefix);
        if (value != null)
        {
            consumer.accept(value);
        }
    }
    
    
    
    @Override
    public void writeRectangle(
        String prefix, Rectangle value)
    {
        if (value == null)
        {
            return;
        }
        properties.put(prefix + "." + RECTANGLE_X, String.valueOf(value.x));
        properties.put(prefix + "." + RECTANGLE_Y, String.valueOf(value.y));
        properties.put(prefix + "." + RECTANGLE_W, String.valueOf(value.width));
        properties.put(prefix + "." + RECTANGLE_H, String.valueOf(value.height));
        writePropertiesUnchecked();
    }
    
    @Override
    public void saveRectangle(
        String prefix, Supplier<? extends Rectangle> supplier)
    {
        writeRectangle(prefix, supplier.get());
    }

    @Override
    public Rectangle readRectangle(
        String prefix)
    {
        String sx = properties.getProperty(prefix + "." + RECTANGLE_X);
        String sy = properties.getProperty(prefix + "." + RECTANGLE_Y);
        String sw = properties.getProperty(prefix + "." + RECTANGLE_W);
        String sh = properties.getProperty(prefix + "." + RECTANGLE_H);
        if (Arrays.asList(sx, sy, sw, sh).contains(null))
        {
            return null;
        }
        try
        {
            int x = Integer.parseInt(sx);
            int y = Integer.parseInt(sy);
            int w = Integer.parseInt(sw);
            int h = Integer.parseInt(sh);
            Rectangle value = new Rectangle(x, y, w, h);
            return value;
        }
        catch (NumberFormatException e)
        {
            logger.warning(e.toString());
            return null;
        }
    }
    
    @Override
    public void restoreRectangle(
        String prefix, Consumer<? super Rectangle> consumer)
    {
        Rectangle value = readRectangle(prefix);
        if (value != null)
        {
            consumer.accept(value);
        }
    }
    
    
    
    
    
    @Override
    public void writeColor(
        String prefix, Color value)
    {
        if (value == null)
        {
            return;
        }
        properties.put(prefix + "." + COLOR_R, String.valueOf(value.getRed()));
        properties.put(prefix + "." + COLOR_G, String.valueOf(value.getGreen()));
        properties.put(prefix + "." + COLOR_B, String.valueOf(value.getBlue()));
        properties.put(prefix + "." + COLOR_A, String.valueOf(value.getAlpha()));
        writePropertiesUnchecked();
    }
    
    @Override
    public void saveColor(
        String prefix, Supplier<? extends Color> supplier)
    {
        writeColor(prefix, supplier.get());
    }

    @Override
    public Color readColor(
        String prefix)
    {
        String sr = properties.getProperty(prefix + "." + COLOR_R);
        String sg = properties.getProperty(prefix + "." + COLOR_G);
        String sb = properties.getProperty(prefix + "." + COLOR_B);
        String sa = properties.getProperty(prefix + "." + COLOR_A);
        if (Arrays.asList(sr, sg, sb, sa).contains(null))
        {
            return null;
        }
        try
        {
            int r = Integer.parseInt(sr);
            int g = Integer.parseInt(sg);
            int b = Integer.parseInt(sb);
            int a = Integer.parseInt(sa);
            Color value = new Color(r, g, b, a);
            return value;
        }
        catch (NumberFormatException e)
        {
            logger.warning(e.toString());
            return null;
        }
    }
    
    @Override
    public void restoreColor(
        String prefix, Consumer<? super Color> consumer)
    {
        Color value = readColor(prefix);
        if (value != null)
        {
            consumer.accept(value);
        }
    }
    

}
