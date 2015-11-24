/*
 * www.javagl.de - Common - UI
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
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
package de.javagl.common.ui;

import java.awt.Component;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 * Methods related to JSpinners
 */
public class JSpinners
{
    /**
     * Create a spinner with the given model, showing the given number
     * of fraction digits for number models
     * 
     * @param model The model
     * @param fractionDigits The fraction digits
     * @return The spinner
     */
    public static JSpinner createSpinner(
        SpinnerModel model, final int fractionDigits)
    {
        return new JSpinner(model)
        {
            /**
             * Serial UID
             */
            private static final long serialVersionUID = -9185142711286020504L;

            @Override
            protected JComponent createEditor(SpinnerModel model) 
            {
                if (model instanceof SpinnerNumberModel) 
                {
                    NumberEditor editor = new NumberEditor(this);
                    DecimalFormat format = editor.getFormat();
                    format.setMaximumFractionDigits(fractionDigits);
                    return editor;                    
                }
                return super.createEditor(model);
            }
        };
    }
    
    
    /**
     * Set whether the value of the given spinner may be changed with
     * mouse drags
     * 
     * @param spinner The spinner
     * @param enabled Whether dragging is enabled
     * @throws IllegalArgumentException If the given spinner does not
     * have a SpinnerNumberModel
     */
    public static void setSpinnerDraggingEnabled(
        final JSpinner spinner, boolean enabled)
    {
        SpinnerModel spinnerModel = spinner.getModel();
        if (!(spinnerModel instanceof SpinnerNumberModel))
        {
            throw new IllegalArgumentException(
                "Dragging is only possible for spinners with a " +
                "SpinnerNumberModel, found "+spinnerModel.getClass());
        }
        
        if (enabled)
        {
            disableSpinnerDragging(spinner);
            enableSpinnerDragging(spinner);
        }
        else
        {
            disableSpinnerDragging(spinner);
        }
    }
    
    /**
     * Disable dragging for the given spinner, by removing all 
     * mouse motion listeners that are {@link SpinnerDraggingHandler}s
     * from the spinner buttons
     * 
     * @param spinner The spinner
     */
    private static void disableSpinnerDragging(JSpinner spinner)
    {
        int n = spinner.getComponentCount();
        for (int i=0; i<n; i++)
        {
            Component c = spinner.getComponent(i);
            String name = c.getName();
            if ("Spinner.nextButton".equals(name) ||
                "Spinner.previousButton".equals(name))
            {
                MouseMotionListener mouseMotionListeners[] =
                    c.getMouseMotionListeners();
                for (MouseMotionListener m : mouseMotionListeners)
                {
                    if (m instanceof SpinnerDraggingHandler)
                    {
                        c.removeMouseMotionListener(m);
                    }
                }
                MouseListener mouseListeners[] =
                    c.getMouseListeners();
                for (MouseListener m : mouseListeners)
                {
                    if (m instanceof SpinnerDraggingHandler)
                    {
                        c.removeMouseListener(m);
                    }
                }
            }
        }
    }

    /**
     * Enable dragging for the given spinner, by adding a 
     * {@link SpinnerDraggingHandler} to the spinner buttons
     * 
     * @param spinner The spinner
     */
    private static void enableSpinnerDragging(JSpinner spinner)
    {
        SpinnerDraggingHandler spinnerDraggingHandler = 
            new SpinnerDraggingHandler(spinner);
        int n = spinner.getComponentCount();
        for (int i=0; i<n; i++)
        {
            Component c = spinner.getComponent(i);
            String name = c.getName();
            if ("Spinner.nextButton".equals(name) ||
                "Spinner.previousButton".equals(name))
            {
                c.addMouseListener(spinnerDraggingHandler);
                c.addMouseMotionListener(spinnerDraggingHandler);
            }
        }
    }

    /**
     * Private constructor to prevent instantiation
     */
    private JSpinners()
    {
        // Private constructor to prevent instantiation
    }

}
