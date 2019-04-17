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
package de.javagl.common.ui.table.renderer;

import java.awt.Color;
import java.util.Objects;
import java.util.function.DoubleFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

/**
 * Implementation of a <code>TableCellRenderer</code> that renders the 
 * background of cells that contain a number with a color that is computed
 * from mapping the number to a color using a coloring function
 */
public class NumberBackgroundColorTableCellRenderer 
    extends BackgroundColorTableCellRenderer 
{
    /**
     * Creates a new instance. Numeric values will be mapped from the range
     * (min, max) to the range (0,1). The resulting value will be passed
     * to the given color function in order to determine the background
     * color.
     * 
     * @param min The minimum value
     * @param max The maximum value
     * @param colorFunction The function that maps values in the range (0,1)
     * to a color. 
     * @param numberFormatter The formatter for numeric values
     */
    public NumberBackgroundColorTableCellRenderer(double min, double max, 
        DoubleFunction<? extends Color> colorFunction,
        Function<? super Number, String> numberFormatter)
    {
        super(new NumberTableCellRenderer(numberFormatter), 
            createCellColorFunction(min, max, colorFunction));
    }
    
    /**
     * Creates the function that returns the cell background color for a
     * given value. If the value is a number, it will be mapped from 
     * the range (min, max) to the range (0,1), and the given function
     * will be used to look up the color based on that value. If the
     * argument is not a number, then the function will return
     * <code>null</code>
     * 
     * @param min The minimum of the range
     * @param max The maximum of the range
     * @param colorFunction The function that maps values in the range (0,1)
     * to a color. 
     * @return The cell color function
     */
    private static Function<Object, ? extends Color> createCellColorFunction(
        double min, double max, DoubleFunction<? extends Color> colorFunction)
    {
        Objects.requireNonNull(colorFunction, 
            "The colorFunction may not be null");
        
        DoubleUnaryOperator mapping = value -> (value - min) / (max - min);
        Function<Object, Color> cellColorFunction = object ->
        {
            if (object instanceof Number)
            {
                Number number = (Number)object;
                double normalized = mapping.applyAsDouble(number.doubleValue());
                Color color = colorFunction.apply(normalized);
                return color;
            }
            return null;
        };
        return cellColorFunction;
    }
}
