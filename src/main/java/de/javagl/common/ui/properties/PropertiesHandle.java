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
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * Interface with convenience functions for handling properties. Instances
 * if classes implementing this interface can be obtained from the
 * {@link PropertiesHandles} class.<br>
 * <br>
 * The methods in this interface come in two flavors, and obey the 
 * following contracts:
 * <ul>
 *   <li>
 *     <b>read/write:</b><br>
 *     The <b>read</b> methods will directly read the value from the 
 *     properties. If the value is not contained in the properties, 
 *     then the method returns <code>null</code>. If the value can
 *     not be parsed, then a warning is printed, and the method 
 *     returns <code>null</code>.<br>
 *     The <b>write</b> methods will directly write the value to
 *     the properties. If the value is <code>null</code>, then 
 *     nothing will be done. Otherwise, properties file will be 
 *     updated immediately.
 *   </li> 
 *   <li>
 *     <b>save/restore:</b><br>
 *     The <b>save</b> method will obtain the value from a supplier,
 *     and pass it to the corresponding <b>write</b> method. So if the
 *     returned value is not <code>null</code>, then the properties
 *     file will be updated immediately.<br> 
 *     The <b>restore</b> method will obtain the value from the 
 *     properties using the corresponding <b>read</b> method, and 
 *     if the returned value is not <code>null</code>, it will
 *     be passed to a consumer.<br> 
 *   </li>
 * </ul>
 * 
 */
public interface PropertiesHandle
{
    /**
     * Write the given value
     * 
     * @param name The name
     * @param value The value
     */
    void writeString(String name, String value);

    /**
     * Save the value from the given supplier
     * 
     * @param name The name
     * @param supplier The supplier
     */
    void saveString(String name, Supplier<? extends String> supplier);

    /**
     * Read the specified value
     * 
     * @param name The name
     * @return The value
     */
    String readString(String name);

    /**
     * Restore the specified value
     * 
     * @param name The name
     * @param consumer The consumer
     */
    void restoreString(String name, Consumer<? super String> consumer);

    
    /**
     * Write the given value
     * 
     * @param name The name
     * @param value The value
     */
    void writeInteger(String name, Integer value);

    /**
     * Save the value from the given supplier
     * 
     * @param name The name
     * @param supplier The supplier
     */
    void saveInteger(String name, IntSupplier supplier);

    /**
     * Read the specified value
     * 
     * @param name The name
     * @return The value
     */
    Integer readInteger(String name);

    /**
     * Restore the specified value
     * 
     * @param name The name
     * @param consumer The consumer
     */
    void restoreInteger(String name, IntConsumer consumer);
    

    /**
     * Write the given value
     * 
     * @param name The name
     * @param value The value
     */
    void writeDouble(String name, Double value);

    /**
     * Save the value from the given supplier
     * 
     * @param name The name
     * @param supplier The supplier
     */
    void saveDouble(String name, DoubleSupplier supplier);

    /**
     * Read the specified value
     * 
     * @param name The name
     * @return The value
     */
    Double readDouble(String name);

    /**
     * Restore the specified value
     * 
     * @param name The name
     * @param consumer The consumer
     */
    void restoreDouble(String name, DoubleConsumer consumer);

    
    /**
     * Write the given value
     * 
     * @param name The name
     * @param value The value
     */
    void writePath(String name, Path value);

    /**
     * Save the value from the given supplier
     * 
     * @param name The name
     * @param supplier The supplier
     */
    void savePath(String name, Supplier<? extends Path> supplier);

    /**
     * Read the specified value
     * 
     * @param name The name
     * @return The value
     */
    Path readPath(String name);

    /**
     * Restore the specified value
     * 
     * @param name The name
     * @param consumer The consumer
     */
    void restorePath(String name, Consumer<? super Path> consumer);

    
    /**
     * Write the given value
     * 
     * @param prefix The prefix
     * @param value The value
     */
    void writePoint(String prefix, Point value);

    /**
     * Save the value from the given supplier
     * 
     * @param prefix The name
     * @param supplier The supplier
     */
    void savePoint(String prefix, Supplier<? extends Point> supplier);

    /**
     * Read the specified value
     * 
     * @param prefix The prefix
     * @return The value
     */
    Point readPoint(String prefix);

    /**
     * Restore the specified value
     * 
     * @param prefix The prefix
     * @param consumer The consumer
     */
    void restorePoint(String prefix, Consumer<? super Point> consumer);
    

    /**
     * Write the given value
     * 
     * @param prefix The prefix
     * @param value The value
     */
    void writeRectangle(String prefix, Rectangle value);

    /**
     * Save the value from the given supplier
     * 
     * @param prefix The prefix
     * @param supplier The supplier
     */
    void saveRectangle(String prefix, Supplier<? extends Rectangle> supplier);

    /**
     * Read the specified value
     * 
     * @param prefix The prefix
     * @return The value
     */
    Rectangle readRectangle(String prefix);

    /**
     * Restore the specified value
     * 
     * @param prefix The prefix
     * @param consumer The consumer
     */
    void restoreRectangle(String prefix, Consumer<? super Rectangle> consumer);
    

    /**
     * Write the given value
     * 
     * @param prefix The prefix
     * @param value The value
     */
    void writeColor(String prefix, Color value);

    /**
     * Save the value from the given supplier
     * 
     * @param prefix The prefix
     * @param supplier The supplier
     */
    void saveColor(String prefix, Supplier<? extends Color> supplier);

    /**
     * Read the specified value
     * 
     * @param prefix The prefix
     * @return The value
     */
    Color readColor(String prefix);

    /**
     * Restore the specified value
     * 
     * @param prefix The prefix
     * @param consumer The consumer
     */
    void restoreColor(String prefix, Consumer<? super Color> consumer);
    

}

