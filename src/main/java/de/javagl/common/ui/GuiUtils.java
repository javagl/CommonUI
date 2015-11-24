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
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import de.javagl.common.ui.panel.collapsible.CollapsiblePanel;

/**
 * Various GUI utility methods
 */
public class GuiUtils
{
    
    /**
     * Wrap the given component into a {@link CollapsiblePanel} with
     * the given title
     * 
     * @param title The title
     * @param component The component
     * @return The panel
     */
    public static CollapsiblePanel wrapCollapsible(
        String title, JComponent component)
    {
        CollapsiblePanel collapsiblePanel = 
            new CollapsiblePanel(new GridLayout(1,1), title);
        collapsiblePanel.add(component);
        return collapsiblePanel;
    }
    
    
    /**
     * Wrap the given component into a panel with a titled border with
     * the given title
     * 
     * @param title The title
     * @param component The component
     * @return The panel
     */
    public static JPanel wrapTitled(String title, JComponent component)
    {
        JPanel p = new JPanel(new GridLayout(1,1));
        p.setBorder(BorderFactory.createTitledBorder(title));
        p.add(component);
        return p;
    }

    /**
     * Wrap the given component into a panel with flow layout
     * 
     * @param component The component
     * @return The panel
     */
    public static JPanel wrapFlow(JComponent component)
    {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        p.add(component);
        return p;
    }
    
    /**
     * Enables or disables the given component and all its children
     * recursively
     *  
     * @param component The component
     * @param enabled Whether the component tree should be enabled
     */
    public static void setDeepEnabled(Component component, boolean enabled)
    {
        component.setEnabled(enabled);
        if (component instanceof Container)
        {
            Container container = (Container)component;
            for (Component c : container.getComponents())
            {
                setDeepEnabled(c, enabled);
            }
        }
    }
    

    /**
     * Private constructor to prevent instantiation
     */
    private GuiUtils()
    {
        // Private constructor to prevent instantiation
    }
}
