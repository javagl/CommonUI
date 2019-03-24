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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

/**
 * Simple implementation of a panel with a "close" button
 */
public class CloseablePanel extends JPanel
{
    /**
     * Serial UID 
     */
    private static final long serialVersionUID = 581192014966771340L;
    
    /**
     * The close button
     */
    private final CloseableContainerButton closeableContainerButton;

    /**
     * The container for the close button
     */
    private final JPanel buttonContainer;
    
    /**
     * The optional title of the panel
     */
    private final String title;
    
    /**
     * The content of this panel
     */
    private final JComponent content;

    /**
     * Create a new instance that shows the given content
     * 
     * @param content The content
     */
    public CloseablePanel(JComponent content)
    {
        this(null, content, CloseCallbacks.alwaysTrue());
    }
    
    /**
     * Creates a new instance that shows the given title and content
     * 
     * @param title The optional title
     * @param content The content
     */
    public CloseablePanel(String title, JComponent content)
    {
        this(title, content, CloseCallbacks.alwaysTrue());
    }
    
    /**
     * Creates a new instance with the given title and content. 
     * 
     * @param title The optional title
     * @param content The content
     * @param closeCallback The {@link CloseCallback}
     */
    public CloseablePanel(
        String title, JComponent content, CloseCallback closeCallback)
    {
        if (closeCallback == null)
        {
            throw new NullPointerException("closeCallback is null");
        }
        this.title = title;
        this.content = content;
        
        ActionListener closeActionListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                boolean mayClose = closeCallback.mayClose(content);
                if (mayClose)
                {
                    CloseablePanel closeablePanel = CloseablePanel.this;
                    Container parent = closeablePanel.getParent();
                    if (parent != null)
                    {
                        parent.remove(closeablePanel);
                        parent.revalidate();
                        parent.repaint();
                    }
                }
            }
        };
        
        this.closeableContainerButton = 
            new CloseableContainerButton(closeActionListener);
        this.buttonContainer = new JPanel(new BorderLayout());
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(closeableContainerButton, BorderLayout.EAST);
        buttonContainer.add(p, BorderLayout.NORTH);
        buttonContainer.setOpaque(false);
        
        if (title == null)
        {
            // This could be done more elegantly with a proper
            // layout manager, but should be OK for now...
            setLayout(new OverlayLayout(this));
            add(content);
            add(buttonContainer);
            setComponentZOrder(content, 1);
            setComponentZOrder(buttonContainer, 0);
        }
        else
        {
            setLayout(new BorderLayout());
            add(content, BorderLayout.CENTER);
            
            JPanel titlePanel = new JPanel(new BorderLayout());
            titlePanel.add(new JLabel(title), BorderLayout.CENTER);
            titlePanel.add(closeableContainerButton, BorderLayout.EAST);
            add(titlePanel, BorderLayout.NORTH);
        }
    }
    
    @Override
    public boolean isOptimizedDrawingEnabled()
    {
        return false;
    }
    
    @Override
    public Dimension getPreferredSize()
    {
        if (super.isPreferredSizeSet())
        {
            return super.getPreferredSize();
        }
        if (title == null)
        {
            return content.getPreferredSize();
        }
        return super.getPreferredSize();
    }
}

