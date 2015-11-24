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
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

/**
 * A {@link MouseListener} that may show a popup menu, and prepare
 * all {@link LocationBasedAction}s that are contained in the 
 * menu items of the menu before it is shown. 
 */
public final class LocationBasedPopupHandler extends MouseAdapter
{
    /**
     * The popup menu that is shown
     */
    private final JPopupMenu popupMenu;
    
    /**
     * Creates a new location based popup handler that shows the
     * given popup menu
     * 
     * @param popupMenu The popup menu. 
     */
    public LocationBasedPopupHandler(JPopupMenu popupMenu)
    {
        this.popupMenu = popupMenu;
    }
    
    @Override
    public void mousePressed(MouseEvent e)
    {
        if (e.isPopupTrigger())
        {
            prepareAndShowPopup(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (e.isPopupTrigger())
        {
            prepareAndShowPopup(e);
        }
    }

    /**
     * Prepare all {@link LocationBasedAction}s that are contained in the
     * items of the menu based on the component and coordinates of the
     * given mouse event
     *   
     * @param e The mouse event
     */
    private void prepareAndShowPopup(MouseEvent e)
    {
        prepareMenu(popupMenu, e.getComponent(), e.getX(), e.getY());
        popupMenu.show(e.getComponent(), e.getX(), e.getY());
    }
    
    /**
     * Prepare the given menu recursively with the given parameters
     * 
     * @param menu The menu
     * @param component The component
     * @param x The x-coordinate
     * @param y THe y-coordinate
     */
    private void prepareMenu(Container menu, Component component, int x, int y)
    {
        int n = menu.getComponentCount();
        for (int i=0; i<n; i++)
        {
            Component menuComponent = popupMenu.getComponent(i);
            if (menuComponent instanceof JMenu)
            {
                JMenu subMenu = (JMenu)menuComponent;
                prepareMenu(subMenu, component, x, y);
            }
            if (menuComponent instanceof AbstractButton)
            {
                AbstractButton abstractButton = (AbstractButton)menuComponent;
                Action action = abstractButton.getAction();
                if (action != null && action instanceof LocationBasedAction)
                {
                    LocationBasedAction locationBasedAction = 
                        (LocationBasedAction)action;
                    locationBasedAction.prepareShow(component, x, y);
                }
            }
        }
    }
}