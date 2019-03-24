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
package de.javagl.common.ui.panel.collapsible;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.javagl.common.ui.GuiUtils;
import de.javagl.common.ui.JScrollPanes;
import de.javagl.common.ui.panel.collapsible.CollapsiblePanel;

/**
 * Convenience class to collect a set of {@link CollapsiblePanel} instances.<br>
 * <br>
 * Note that the layout of this component is not supposed to be changed,
 * and adding or removing components should only be done via the
 * {@link #addToAccordion} or {@link #removeFromAccordion} methods.
 */
public class AccordionPanel extends JPanel
{
    /**
     * Serial UID 
     */
    private static final long serialVersionUID = 7425427793372692306L;
    
    /**
     * The panel that contains the other panels 
     */
    private final JPanel contentPanel;
    
    /**
     * The mapping from components to their containing collapsible panels
     */
    private final Map<JComponent, CollapsiblePanel> collapsiblePanels;
    
    /**
     * Default constructor
     */
    public AccordionPanel()
    {
        super(new BorderLayout());
        
        contentPanel = new ScrollablePanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        collapsiblePanels = new LinkedHashMap<JComponent, CollapsiblePanel>();
        
        JScrollPane controlPanelScrollPane = 
            JScrollPanes.createVerticalScrollPane(contentPanel);
        controlPanelScrollPane.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        controlPanelScrollPane.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(controlPanelScrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Add the given component to this accordion, wrapping it into a 
     * collapsible panel with the given title.
     * 
     * @param title The title
     * @param component The component to add
     * @return The collapsible panel that has been created internally
     */
    public CollapsiblePanel addToAccordion(String title, JComponent component) 
    {
        return addToAccordion(title, component, false);
    }
    
    /**
     * Add the given component to this accordion, wrapping it into a 
     * collapsible panel with the given title.
     * 
     * @param title The title
     * @param component The component to add
     * @param minimized Whether the collapsible panel should be minimized
     * @return The collapsible panel that has been created internally
     */
    public CollapsiblePanel addToAccordion(
        String title, JComponent component, boolean minimized) 
    {
        CollapsiblePanel collapsiblePanel = 
            GuiUtils.wrapCollapsible(title, component);
        collapsiblePanel.setMinimized(minimized);
        contentPanel.add(collapsiblePanel);
        collapsiblePanels.put(component, collapsiblePanel);
        revalidate();
        return collapsiblePanel;
    }
    
    /**
     * Remove the given component from this accordion
     * 
     * @param component The component to remove
     */
    public void removeFromAccordion(JComponent component) 
    {
        CollapsiblePanel collapsiblePanel = collapsiblePanels.get(component);
        if (collapsiblePanel != null)
        {
            contentPanel.remove(collapsiblePanel);
            collapsiblePanels.remove(component);
            revalidate();
        }
    }
    
    /**
     * Remove all components that have previously been added by calling
     * {@link #addToAccordion}
     */
    public void clearAccordion()
    {
        List<JComponent> components = 
            new ArrayList<JComponent>(collapsiblePanels.keySet());
        for (JComponent component : components)
        {
            removeFromAccordion(component);
        }
    }

}
