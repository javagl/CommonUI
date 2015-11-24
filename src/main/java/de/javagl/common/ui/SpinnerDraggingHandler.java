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

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

/**
 * A mouse motion listener for spinner buttons that allows changing
 * the spinner value via mouse drags. Used in 
 * {@link JSpinners#setSpinnerDraggingEnabled(JSpinner, boolean)}.
 */
class SpinnerDraggingHandler extends MouseAdapter
{
    /**
     * The spinner
     */
    private final JSpinner spinner;
    
    /**
     * The model of the spinner
     */
    private final SpinnerNumberModel model;
    
    /**
     * The previous mouse position
     */
    private Point previousPoint = new Point();
    
    /**
     * The current speed for the changes while dragging
     */
    private int currentFactor = 1;

    /**
     * The robot for wrapping the cursor
     */
    private final Robot robot;
    
    /**
     * Default constructor
     * 
     * @param spinner The spinner
     * @throws IllegalArgumentException If the given spinner does not
     * have a SpinnerNumberModel
     */
    SpinnerDraggingHandler(JSpinner spinner)
    {
        SpinnerModel spinnerModel = spinner.getModel();
        if (!(spinnerModel instanceof SpinnerNumberModel))
        {
            throw new IllegalArgumentException(
                "Dragging is only possible for spinners with a " +
                "SpinnerNumberModel, found "+spinnerModel.getClass());
        }
        this.spinner = spinner;
        this.model = (SpinnerNumberModel)spinnerModel;
        
        Robot theRobot = null;
        try
        {
            theRobot = new Robot();
        }
        catch (AWTException e)
        {
            // Ignore
        }
        catch (SecurityException e)
        {
            // Ignore
        }
        robot = theRobot;
    }

    /**
     * Computes n+toAdd*times, obeying the types of the numbers
     * 
     * @param n The input
     * @param toAdd The addend
     * @param times The factor
     * @return The result
     */
    private Number addTimes(Object n, Number toAdd, int times)
    {
        Number m = (Number)n;
        if (m instanceof Double)
        {
            return m.doubleValue() + times * toAdd.doubleValue();
        }
        return m.intValue() + times * toAdd.intValue();
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        Number stepSize = model.getStepSize();
        Number value = (Number)model.getValue();
        double d = Math.abs(value.doubleValue()) / stepSize.doubleValue();
        // TODO This value is somewhat arbitrary...
        int i = (int)Math.ceil(d / 250.0);
        currentFactor = Math.max(1, i);
        System.out.println("For "+value+" with step size "+
            stepSize+" use factor "+currentFactor);
    }
    
    @Override
    public void mouseMoved(MouseEvent e)
    {
        previousPoint = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (!spinner.isEnabled())
        {
            return;
        }
        boolean wrapped = handleWrapping(e);
        if (wrapped)
        {
            return;
        }
        int dy = previousPoint.y - e.getY();
        Object value = model.getValue();
        Number stepSize = model.getStepSize();
        Number newValue = addTimes(value, stepSize, dy*currentFactor);
        Number min = (Number)model.getMinimum();
        Number max = (Number)model.getMaximum();
        if (newValue.doubleValue() < min.doubleValue())
        {
            newValue = min;
        }
        if (newValue.doubleValue() > max.doubleValue())
        {
            newValue = max;
        }
        spinner.setValue(newValue);
        tryCommit();
        previousPoint = e.getPoint();
    }

    /**
     * Let the mouse wrap from the top of the screen to the bottom
     * or vice versa
     * 
     * @param e The mouse event
     * @return Whether the mouse wrapped
     */
    private boolean handleWrapping(MouseEvent e)
    {
        if (robot == null)
        {
            return false;
        }
        PointerInfo pointerInfo = null;
        try
        {
            pointerInfo = MouseInfo.getPointerInfo();
        }
        catch (SecurityException ex)
        {
            return false;
        }
        Rectangle r = 
            pointerInfo.getDevice().getDefaultConfiguration().getBounds();
        Point onScreen = pointerInfo.getLocation();
        if (onScreen.y == 0)
        {
            robot.mouseMove(onScreen.x, r.height-2);
            previousPoint = new Point(onScreen.x, r.height-2);
            SwingUtilities.convertPointFromScreen(previousPoint, spinner);
            return true;
        } 
        else if (onScreen.y == r.height - 1)
        {
            robot.mouseMove(onScreen.x, 1);
            previousPoint = new Point(onScreen.x, 1);
            SwingUtilities.convertPointFromScreen(previousPoint, spinner);
            return true;
        }
        return false;
    }

    /**
     * Try to commit the current value to the spinner editor. This is 
     * necessary in order to validate the number in the model against 
     * the displayed value.  
     */
    private void tryCommit()
    {
        try
        {
            JComponent editor = spinner.getEditor();
            if (editor instanceof JSpinner.DefaultEditor)
            {
                JSpinner.DefaultEditor defaultEditor =
                    (JSpinner.DefaultEditor)editor;
                defaultEditor.commitEdit();
            }
        }
        catch (ParseException e1)
        {
            // Ignored
        }
    }
    
}