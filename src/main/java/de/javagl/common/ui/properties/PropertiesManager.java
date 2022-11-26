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
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * A class for managing properties of an application that should be maintained
 * in a properties file.
 */
public class PropertiesManager
{
    /**
     * The {@link PropertiesHandle} for actually handling the properties
     */
    private final PropertiesHandle propertiesHandle;
    
    /**
     * The mapping from names to the types of properties
     */
    private final Map<String, Class<?>> types;
    
    /**
     * The mapping from names to suppliers for the property values
     */
    private final Map<String, Object> suppliers;
    
    /**
     * The mapping from names to consumers for the property values
     */
    private final Map<String, Object> consumers;

    /**
     * The window adapter that will be attached as a window listener
     * to the window passed in to {@link #saveOnClose(Window)}, and
     * call {@link #save()} when the window is closed.
     */
    private final WindowAdapter windowAdapter = new WindowAdapter()
    {
        @Override
        public void windowClosing(WindowEvent e)
        {
            save();
        }
    };
    
    /**
     * Creates a new instance that uses the given {@link PropertiesHandle}
     * 
     * @param propertiesHandle The {@link PropertiesHandle}
     */
    public PropertiesManager(PropertiesHandle propertiesHandle)
    {
        this.propertiesHandle = Objects.requireNonNull(
            propertiesHandle, "The propertiesHandle may not be null");
        this.types = new LinkedHashMap<String, Class<?>>();
        this.suppliers = new LinkedHashMap<String, Object>();
        this.consumers = new LinkedHashMap<String, Object>();
    }
    
    /**
     * Register the given supplier and consumer under the given name
     * 
     * @param name The name
     * @param supplier The supplier
     * @param consumer The consumer
     */
    public void registerString(String name, 
        Supplier<? extends String> supplier, 
        Consumer<? super String> consumer)
    {
        register(name, String.class, supplier, consumer);
    }
    
    /**
     * Register the given supplier and consumer under the given name
     * 
     * @param name The name
     * @param supplier The supplier
     * @param consumer The consumer
     */
    public void registerInteger(String name, 
        IntSupplier supplier, 
        IntConsumer consumer)
    {
        register(name, Integer.class, supplier, consumer);
    }
    
    /**
     * Register the given supplier and consumer under the given name
     * 
     * @param name The name
     * @param supplier The supplier
     * @param consumer The consumer
     */
    public void registerDouble(String name, 
        DoubleSupplier supplier, 
        DoubleConsumer consumer)
    {
        register(name, Double.class, supplier, consumer);
    }
    
    /**
     * Register the given supplier and consumer under the given name
     * 
     * @param name The name
     * @param supplier The supplier
     * @param consumer The consumer
     */
    public void registerPath(String name, 
        Supplier<? extends Path> supplier, 
        Consumer<? super Path> consumer)
    {
        register(name, Path.class, supplier, consumer);
    }

    /**
     * Register the given supplier and consumer under the given name
     * 
     * @param name The name
     * @param supplier The supplier
     * @param consumer The consumer
     */
    public void registerPoint(String name, 
        Supplier<? extends Point> supplier, 
        Consumer<? super Point> consumer)
    {
        register(name, Point.class, supplier, consumer);
    }

    /**
     * Register the given supplier and consumer under the given name
     * 
     * @param name The name
     * @param supplier The supplier
     * @param consumer The consumer
     */
    public void registerRectangle(String name, 
        Supplier<? extends Rectangle> supplier, 
        Consumer<? super Rectangle> consumer)
    {
        register(name, Rectangle.class, supplier, consumer);
    }

    /**
     * Register the given supplier and consumer under the given name
     * 
     * @param name The name
     * @param supplier The supplier
     * @param consumer The consumer
     */
    public void registerColor(String name, 
        Supplier<? extends Color> supplier, 
        Consumer<? super Color> consumer)
    {
        register(name, Color.class, supplier, consumer);
    }
    
    /**
     * Register the given supplier and consumer under the given name
     * 
     * @param name The name
     * @param type The type
     * @param supplier The supplier
     * @param consumer The consumer
     */
    private void register(
        String name, Class<?> type, Object supplier, Object consumer)
    {
        if (types.containsKey(name))
        {
            throw new IllegalArgumentException("Duplicate name: " + name);
        }
        types.put(name, type);
        suppliers.put(name, supplier);
        consumers.put(name, consumer);
    }
    
    /**
     * Save the current state, by obtaining all values from the registered
     * suppliers, and writing them into the properties file
     */
    public void save()
    {
        for (String name : types.keySet())
        {
            Class<?> type = types.get(name);
            Object supplier = suppliers.get(name);
            if (supplier == null)
            {
                continue;
            }
            if (type.equals(String.class))
            {
                @SuppressWarnings("unchecked")
                Supplier<? extends String> typedSupplier =
                    (Supplier<? extends String>) supplier;
                propertiesHandle.saveString(name, typedSupplier);
            }
            if (type.equals(Integer.class))
            {
                IntSupplier typedSupplier =
                    (IntSupplier) supplier;
                propertiesHandle.saveInteger(name, typedSupplier);
            }
            if (type.equals(Double.class))
            {
                DoubleSupplier typedSupplier =
                    (DoubleSupplier) supplier;
                propertiesHandle.saveDouble(name, typedSupplier);
            }
            if (type.equals(Path.class))
            {
                @SuppressWarnings("unchecked")
                Supplier<? extends Path> typedSupplier =
                    (Supplier<? extends Path>) supplier;
                propertiesHandle.savePath(name, typedSupplier);
            }
            if (type.equals(Point.class))
            {
                @SuppressWarnings("unchecked")
                Supplier<? extends Point> typedSupplier =
                    (Supplier<? extends Point>) supplier;
                propertiesHandle.savePoint(name, typedSupplier);
            }
            if (type.equals(Rectangle.class))
            {
                @SuppressWarnings("unchecked")
                Supplier<? extends Rectangle> typedSupplier =
                    (Supplier<? extends Rectangle>) supplier;
                propertiesHandle.saveRectangle(name, typedSupplier);
            }
            if (type.equals(Color.class))
            {
                @SuppressWarnings("unchecked")
                Supplier<? extends Color> typedSupplier =
                    (Supplier<? extends Color>) supplier;
                propertiesHandle.saveColor(name, typedSupplier);
            }
        }
    }

    /**
     * Restore the state, by passing all values from the properties file
     * to the registered consumers.
     */
    public void restore()
    {
        for (String name : types.keySet())
        {
            Class<?> type = types.get(name);
            Object consumer = consumers.get(name);
            if (consumer == null)
            {
                continue;
            }
            if (type.equals(String.class))
            {
                @SuppressWarnings("unchecked")
                Consumer<? super String> typedConsumer =
                    (Consumer<? super String>) consumer;
                propertiesHandle.restoreString(name, typedConsumer);
            }
            if (type.equals(Integer.class))
            {
                IntConsumer typedConsumer =
                    (IntConsumer) consumer;
                propertiesHandle.restoreInteger(name, typedConsumer);
            }
            if (type.equals(Double.class))
            {
                DoubleConsumer typedConsumer =
                    (DoubleConsumer) consumer;
                propertiesHandle.restoreDouble(name, typedConsumer);
            }
            if (type.equals(Path.class))
            {
                @SuppressWarnings("unchecked")
                Consumer<? super Path> typedConsumer =
                    (Consumer<? super Path>) consumer;
                propertiesHandle.restorePath(name, typedConsumer);
            }
            if (type.equals(Point.class))
            {
                @SuppressWarnings("unchecked")
                Consumer<? super Point> typedConsumer =
                    (Consumer<? super Point>) consumer;
                propertiesHandle.restorePoint(name, typedConsumer);
            }
            if (type.equals(Rectangle.class))
            {
                @SuppressWarnings("unchecked")
                Consumer<? super Rectangle> typedConsumer =
                    (Consumer<? super Rectangle>) consumer;
                propertiesHandle.restoreRectangle(name, typedConsumer);
            }
            if (type.equals(Color.class))
            {
                @SuppressWarnings("unchecked")
                Consumer<? super Color> typedConsumer =
                    (Consumer<? super Color>) consumer;
                propertiesHandle.restoreColor(name, typedConsumer);
            }
        }
    }
    
    /**
     * Attach a listener to the given window that will save the properties
     * when the window is closed
     * 
     * @param window The window
     */
    public void saveOnClose(Window window)
    {
        window.addWindowListener(windowAdapter);
    }

    /**
     * Detaches the listener that was attached to the given window when
     * it was passed to {@link #saveOnClose(Window)}
     * 
     * @param window The window
     */
    public void detachSaveOnClose(Window window)
    {
        window.removeWindowListener(windowAdapter);
    }
    
    
    
}