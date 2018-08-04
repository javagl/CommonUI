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
package de.javagl.common.ui.closeable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Implementation of a tab for a JTabbedPane that can be closed
 */
public class CloseableTab extends JPanel
{
    // NOTE: This is partially based on the Swing sample code

    /**
     * Adds a {@link CloseableTab} with the given title and component to
     * the given tabbed pane. 
     * 
     * @param tabbedPane The tabbed pane
     * @param title The title of the tab
     * @param component The component in the tab
     */
    public static void addTo(
        JTabbedPane tabbedPane, String title, JComponent component)
    {
        addTo(tabbedPane, title, component, false);
    }
    
    /**
     * Adds a {@link CloseableTab} with the given title and component to
     * the given tabbed pane. 
     * 
     * @param tabbedPane The tabbed pane
     * @param title The title of the tab
     * @param component The component in the tab
     * @param confirmClose Whether closing a tab has to be confirmed
     */
    public static void addTo(
        JTabbedPane tabbedPane, String title, JComponent component, 
        boolean confirmClose)
    {
        if (confirmClose)
        {
            addTo(tabbedPane, title, component, CloseCallbacks.confirming());
        }
        else
        {
            addTo(tabbedPane, title, component, CloseCallbacks.alwaysTrue());
        }
    }
    
    /**
     * Adds a {@link CloseableTab} with the given title and component to
     * the given tabbed pane. The given {@link CloseCallback} will be
     * consulted to decide whether the tab may be closed
     * 
     * @param tabbedPane The tabbed pane
     * @param title The title of the tab
     * @param component The component in the tab
     * @param closeCallback The {@link CloseCallback}
     */
    public static void addTo(
        JTabbedPane tabbedPane, String title, JComponent component, 
        CloseCallback closeCallback)
    {
        int index = tabbedPane.getTabCount();
        tabbedPane.addTab(title, component);
        tabbedPane.setTabComponentAt(
            index, new CloseableTab(tabbedPane, closeCallback));
        tabbedPane.setSelectedIndex(index);
    }
    
    
    
    /**
     * Serial UID
     */
    private static final long serialVersionUID = -7605099240060933602L;
    
    /**
     * Creates a new closeable tab for the given tabbed pane. It will
     * call the given {@link CloseCallback} when it is about to be
     * closed.
     * 
     * @param tabbedPane The tabbed pane
     * @param closeCallback The {@link CloseCallback}
     */
    CloseableTab(JTabbedPane tabbedPane, CloseCallback closeCallback)
    {
        super(new BorderLayout());
        if (tabbedPane == null)
        {
            throw new NullPointerException("tabbedPane is null");
        }
        if (closeCallback == null)
        {
            throw new NullPointerException("closeCallback is null");
        }

        // Create and add a label that displays the tab title
        JLabel label = new JLabel()
        {
            /**
             * Serial UID
             */
            private static final long serialVersionUID = 5632763968012666836L;

            @Override
            public String getText()
            {
                int i = tabbedPane.indexOfTabComponent(CloseableTab.this);
                if (i != -1)
                {
                    return tabbedPane.getTitleAt(i);
                }
                return null;
            }
        };
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        add(label, BorderLayout.CENTER);

        // Create the CloseableTabButton
        ActionListener closeActionListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int i = tabbedPane.indexOfTabComponent(CloseableTab.this);
                if (i != -1)
                {
                    Component tabComponent = 
                        tabbedPane.getSelectedComponent();
                    boolean mayClose = closeCallback.mayClose(tabComponent);
                    if (mayClose)
                    {
                        tabbedPane.remove(i);
                    }
                }
            }
        };
        
        CloseableContainerButton closeableTabButton = 
            new CloseableContainerButton(closeActionListener);
        add(closeableTabButton, BorderLayout.EAST);
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        
        setOpaque(false);
    }


}
