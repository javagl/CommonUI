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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;
import javax.swing.event.MenuDragMouseEvent;

/**
 * Utility methods based on methods from SwingUtilities
 */
public class MoreSwingUtilities
{
    /**
     * Wrapper for <code>SwingUtilities#convertRectangle</code> that accepts
     * a Rectangle2D and not only a Rectangle. (The Rectangle2D is internally 
     * converted to a Rectangle by casting all components to <code>int</code>)
     * 
     * @param sourceComponent The source component
     * @param rectangle The rectangle
     * @param targetComponent The target component
     * @return The new rectangle
     */
    static Rectangle2D convertRectangle(
        Component sourceComponent, 
        Rectangle2D rectangle,
        Component targetComponent)
    {
        return SwingUtilities.convertRectangle(
            sourceComponent, asRectangle(rectangle), targetComponent);        
    }
    
    /**
     * Converts the given Rectangle2D into a Rectangle by casting all
     * elements to <code>int</code>.
     * 
     * @param rectangle The rectangle
     * @return The resulting rectangle
     */
    public static Rectangle asRectangle(Rectangle2D rectangle)
    {
        Rectangle r = new Rectangle(
            (int)rectangle.getX(), 
            (int)rectangle.getY(),
            (int)rectangle.getWidth(),
            (int)rectangle.getHeight());
        return r;
    }
    

    /**
     * Wrapper for <code>SwingUtilities#convertPoint</code> that accepts
     * a Point2D and not only a Point. (The Point2D is internally converted
     * into a Point by casting all components to <code>int</code>)
     * 
     * @param sourceComponent The source component
     * @param point The point
     * @param targetComponent The target component
     * @return The new point
     */
    public static Point convertPoint(
        Component sourceComponent, 
        Point2D point,
        Component targetComponent)
    {
        Point p = new Point((int)point.getX(), (int)point.getY());
        return SwingUtilities.convertPoint(
            sourceComponent, p, targetComponent);
    }

    /**
     * Implementation of {@link SwingUtilities#convertMouseEvent(
     * Component, MouseEvent, Component)} that properly sets the
     * button of the returned event.
     * (See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=7181403 )
     * 
     * @param source The source component
     * @param sourceEvent The source event
     * @param destination The destination component
     * @return The resulting mouse event
     */
    public static MouseEvent convertMouseEvent(Component source,
        MouseEvent sourceEvent, Component destination)
    {
        Point p = SwingUtilities.convertPoint(
            source, sourceEvent.getPoint(), destination);
        
        Component newSource = source;
        if (destination != null)
        {
            newSource = destination;
        }
        
        MouseEvent newEvent = null;
        if (sourceEvent instanceof MouseWheelEvent)
        {
            MouseWheelEvent sourceWheelEvent = (MouseWheelEvent) sourceEvent;
            newEvent =
                new MouseWheelEvent(
                    newSource, 
                    sourceWheelEvent.getID(),
                    sourceWheelEvent.getWhen(),
                    sourceWheelEvent.getModifiersEx(), 
                    p.x, 
                    p.y,
                    sourceWheelEvent.getXOnScreen(),
                    sourceWheelEvent.getYOnScreen(),
                    sourceWheelEvent.getClickCount(),
                    sourceWheelEvent.isPopupTrigger(),
                    sourceWheelEvent.getScrollType(),
                    sourceWheelEvent.getScrollAmount(),
                    sourceWheelEvent.getWheelRotation());
        }
        else if (sourceEvent instanceof MenuDragMouseEvent)
        {
            MenuDragMouseEvent sourceMenuDragEvent =
                (MenuDragMouseEvent) sourceEvent;
            newEvent =
                new MenuDragMouseEvent(
                    newSource, 
                    sourceMenuDragEvent.getID(),
                    sourceMenuDragEvent.getWhen(),
                    sourceMenuDragEvent.getModifiersEx(), 
                    p.x, 
                    p.y,
                    sourceMenuDragEvent.getXOnScreen(),
                    sourceMenuDragEvent.getYOnScreen(),
                    sourceMenuDragEvent.getClickCount(),
                    sourceMenuDragEvent.isPopupTrigger(),
                    sourceMenuDragEvent.getPath(),
                    sourceMenuDragEvent.getMenuSelectionManager());
        }
        else
        {
            newEvent =
                new MouseEvent(
                    newSource, 
                    sourceEvent.getID(),
                    sourceEvent.getWhen(), 
                    sourceEvent.getModifiersEx(), 
                    p.x, 
                    p.y,
                    sourceEvent.getXOnScreen(),
                    sourceEvent.getYOnScreen(), 
                    sourceEvent.getClickCount(),
                    sourceEvent.isPopupTrigger(), 
                    sourceEvent.getButton());
        }
        return newEvent;
    }

    
    /**
     * Execute the given runnable on the event dispatch thread. <br>
     * <br>
     * If the calling thread is the EDT, then the runnable will be executed
     * directly. Otherwise, it will be executed with 
     * <code>SwingUtilities.invokeLater</code>
     * 
     * @param runnable The runnable
     */
    public static void invokeOnEventDispatchThread(Runnable runnable)
    {
        if (SwingUtilities.isEventDispatchThread())
        {
            runnable.run();
        }
        else
        {
            SwingUtilities.invokeLater(runnable);
        }
    }
    
    /**
     * Execute the given runnable on the event dispatch thread. <br>
     * <br>
     * If the calling thread is the EDT, then the runnable will be executed
     * directly. Otherwise, it will be executed with 
     * <code>SwingUtilities.invokeAndWait</code>
     * 
     * @param runnable The runnable
     * @throws RuntimeException If the given runnable causes a RuntimeException
     */
    public static void invokeOnEventDispatchThreadSync(Runnable runnable)
    {
        if (SwingUtilities.isEventDispatchThread())
        {
            runnable.run();
        }
        else
        {
            try
            {
                SwingUtilities.invokeAndWait(runnable);
            }
            catch (InvocationTargetException e)
            {
                throw new RuntimeException(e);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    
}
