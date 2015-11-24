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

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Implementation of a tab for a JTabbedPane that can be closed
 */
public class CloseableTab extends JPanel
{
    // NOTE: This is partially based on the Swing sample code

    /**
     * Interface for classes that may check whether a tab may be closed
     */
    public static interface CloseCallback
    {
        /**
         * Will be called when the button for closing the tab with the
         * given component was clicked. This method may perform 
         * cleanup operations, and returns whether the tab containing 
         * the given component may be closed.
         * 
         * @param componentInTab The component in the tab
         * @return Whether the tab may be closed
         */
        boolean mayClose(Component componentInTab);
    }
    
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
            addTo(tabbedPane, title, component, confirmingCloseCallback());
        }
        else
        {
            addTo(tabbedPane, title, component, alwaysTrueCloseCallback());
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
     * Returns a {@link CloseCallback} that always returns <code>true</code>
     * 
     * @return The {@link CloseCallback}
     */
    private static CloseCallback alwaysTrueCloseCallback()
    {
        return new CloseCallback()
        {
            @Override
            public boolean mayClose(Component componentInTab)
            {
                return true;
            }
        };
    }

    /**
     * Returns a {@link CloseCallback} that shows a confirmation dialog
     * 
     * @return The {@link CloseCallback}
     */
    private static CloseCallback confirmingCloseCallback()
    {
        return new CloseCallback()
        {
            @Override
            public boolean mayClose(Component componentInTab)
            {
                int option = JOptionPane.showConfirmDialog(
                    componentInTab, "Close this tab?", 
                    "Confirm", JOptionPane.YES_NO_OPTION);
                return option == JOptionPane.YES_OPTION;
            }
        };
    }
    
    /**
     * Serial UID
     */
    private static final long serialVersionUID = -7605099240060933602L;
    
    /**
     * The tabbed pane that this tab belongs to 
     */
    private final JTabbedPane tabbedPane;
    
    /**
     * The close callback
     */
    private final CloseCallback closeCallback;

    /**
     * Creates a new closeable tab for the given tabbed pane. It will
     * call the given {@link CloseCallback} when it is about to be
     * closed.
     * 
     * @param tabbedPane The tabbed pane
     * @param closeCallback The {@link CloseCallback}
     */
    CloseableTab(final JTabbedPane tabbedPane, CloseCallback closeCallback)
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
        this.tabbedPane = tabbedPane;
        this.closeCallback = closeCallback;

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
        CloseableTabButton closeableTabButton = new CloseableTabButton();
        add(closeableTabButton, BorderLayout.EAST);
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        
        setOpaque(false);
    }

    /**
     * The button for closing a tab
     */
    private class CloseableTabButton extends JButton
    {
        /**
         * Serial UID
         */
        private static final long serialVersionUID = 5601512731544692098L;

        /**
         * The {@link MouseListener} that will cause the border
         * of the button to be painted when it is hovered by
         * the mouse
         */
        private final MouseListener rolloverMouseListener = new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                Component component = e.getComponent();
                if (component instanceof AbstractButton)
                {
                    AbstractButton button = (AbstractButton) component;
                    button.setBorderPainted(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                Component component = e.getComponent();
                if (component instanceof AbstractButton)
                {
                    AbstractButton button = (AbstractButton) component;
                    button.setBorderPainted(false);
                }
            }
        };
        
        /**
         * Default constructor
         */
        CloseableTabButton()
        {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("Close tab");
            setContentAreaFilled(false);
            setFocusable(false);
            
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            addMouseListener(rolloverMouseListener);
            setRolloverEnabled(true);
            
            addActionListener(new ActionListener()
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
            });
        }


        @Override
        protected void paintComponent(Graphics gr)
        {
            super.paintComponent(gr);
            Graphics2D g = (Graphics2D) gr.create();
            if (getModel().isPressed())
            {
                g.translate(1, 1);
            }
            g.setStroke(new BasicStroke(2));
            g.setColor(Color.GRAY);
            if (getModel().isRollover())
            {
                g.setColor(Color.BLACK);
            }
            int delta = 6;
            int w = getWidth();
            int h = getHeight();
            g.drawLine(delta, delta, w - delta - 1, h - delta - 1);
            g.drawLine(w - delta - 1, delta, delta, h - delta - 1);
            g.dispose();
        }
    }

}
