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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * Simple implementation of a collapsible panel. It is a panel with a 
 * titled border that may be collapsed to only show that title.
 */
public class CollapsiblePanel extends JPanel
{
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 665411946824373388L;

    /**
     * Whether this panel is currently minimized
     */
    private boolean minimized;

    /**
     * The current height of this panel
     */
    private int currentHeight = -1;
    
    /**
     * The height of this panel when it is minimized
     */
    private int minimizedHeight;
    
    /**
     * The timer for the collapsing animation
     */
    private Timer timer;
    
    /**
     * The duration of the transition between the collapsed and
     * expanded state, in milliseconds
     */
    private final int durationMS = 250;
    
    /**
     * The delay between two animation steps, in milliseconds
     */
    private final int delayMS = 25;
    
    /**
     * A titled border that additionally paints a [+] or [-] in the
     * upper right corner to indicate whether the panel is currently
     * minimized or expanded, respectively
     */
    private class CollapsibleTitledBorder extends TitledBorder
    {
        /**
         * Serial UID
         */
        private static final long serialVersionUID = 8843873866801716306L;

        /**
         * Creates a new border with the given title
         * 
         * @param title The title
         */
        private CollapsibleTitledBorder(String title)
        {
            super(title);
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y,
            int width, int height)
        {
            super.paintBorder(c, g, x, y, width, height);
            
            Insets insets = getInsets();
            int space = 4;
            int rectSize = insets.top - space - space;
            int rectMinY = space;
            int rectMinX = width - rectSize - space - space;
            g.setColor(getBackground());
            g.fillRect(rectMinX, rectMinY, rectSize, rectSize);
            g.setColor(getForeground());
            g.drawRect(rectMinX, rectMinY, rectSize, rectSize);

            int x0 = 2;
            int x1 = rectSize - 2;
            int y0 = rectSize / 2;
            int y1 = rectSize / 2;
            g.drawLine(
                rectMinX + x0, rectMinY + y0, 
                rectMinX + x1, rectMinY + y1);
            if (isMinimized())
            {
                g.drawLine(
                    rectMinX + y0, rectMinY + x0, 
                    rectMinX + y1, rectMinY + x1);
            }
        }
    }
    
    /**
     * Creates a new collapsible panel with a FlowLayout and the given
     * title. 
     * 
     * @param title The title
     */
    public CollapsiblePanel(String title)
    {
        this(new FlowLayout(), title, false);
    }
    
    /**
     * Creates a new collapsible panel with a FlowLayout and the given
     * title. 
     * 
     * @param title The title
     * @param minimized Whether the panel should be minimized initially
     */
    public CollapsiblePanel(String title, boolean minimized)
    {
        this(new FlowLayout(), title, minimized);
    }

    /**
     * Creates a new collapsible panel with the given layout and title
     *  
     * @param layoutManager The layout
     * @param title The title
     */
    public CollapsiblePanel(
        LayoutManager layoutManager, String title)
    {
        this(layoutManager, title, false);
    }
    
    /**
     * Creates a new collapsible panel with the given layout and title
     *  
     * @param layoutManager The layout
     * @param title The title
     * @param minimized Whether the panel should be minimized initially
     */
    public CollapsiblePanel(
        LayoutManager layoutManager, String title, boolean minimized)
    {
        super(layoutManager);
        this.minimized = minimized;
        
        Border border = new CollapsibleTitledBorder(title);
        setBorder(border);
        
        Insets insets = border.getBorderInsets(this);
        minimizedHeight = insets.top + 1;
        if (minimized)
        {
            currentHeight = minimizedHeight;
        }
        else
        {
            currentHeight = Integer.MAX_VALUE;
        }
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getY() < minimizedHeight)
                {
                    setMinimized(!isMinimized());
                }
            }
        });
    }
    
    @Override
    public void invalidate()
    {
        super.invalidate();
    }
    
    /**
     * Returns the preferred height from the super implementation
     * 
     * @return The preferred height
     */
    private int getSuperPreferredHeight()
    {
        Dimension d = super.getPreferredSize();
        return d.height;
    }
    
    /**
     * Returns whether this panel is currently minimized (collapsed)
     * 
     * @return Whether this panel is currently minimized 
     */
    public boolean isMinimized()
    {
        return minimized;
    }
    
    /**
     * Set the new state of this panel
     * 
     * @param minimized Whether the panel should be minimized (collapsed)
     */
    public void setMinimized(boolean minimized)
    {
        if (this.minimized && !minimized)
        {
            maximize();
        }
        else if (!this.minimized && minimized)
        {
            minimize();
        }
    }

    /**
     * Performs the minimization process
     */
    private void minimize()
    {
        if (timer != null)
        {
            timer.stop();
            timer = null;
        }
        
        currentHeight = getHeight();
        double steps = (double)durationMS / delayMS;
        double delta = currentHeight - minimizedHeight;
        final int stepSize = (int)Math.ceil(delta / steps);
        
        //System.out.println("steps " + steps);
        //System.out.println("currentHeight " + currentHeight);
        //System.out.println("delta " + delta);
        //System.out.println("stepSize " + stepSize);
        
        timer = new Timer(delayMS, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                currentHeight -= stepSize;
                currentHeight = Math.max(currentHeight, minimizedHeight);
                if (currentHeight <= minimizedHeight)
                {
                    minimized = true;
                    timer.stop();
                    timer = null;
                }
                revalidate();
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }
    
    /**
     * Performs the maximization process
     */
    private void maximize()
    {
        if (timer != null)
        {
            timer.stop();
            timer = null;
        }
        
        final int targetHeight = getSuperPreferredHeight();
        double steps = (double)durationMS / delayMS;
        double delta = targetHeight - currentHeight;
        final int stepSize = (int)Math.ceil(delta / steps);
        currentHeight = getHeight();
        
        //System.out.println("steps " + steps);
        //System.out.println("currentHeight " + currentHeight);
        //System.out.println("delta " + delta);
        //System.out.println("stepSize " + stepSize);
        
        timer = new Timer(delayMS, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                minimized = false;
                currentHeight += stepSize;
                currentHeight = Math.min(currentHeight, targetHeight);
                if (currentHeight >= targetHeight)
                {
                    currentHeight = Integer.MAX_VALUE;
                    timer.stop();
                    timer = null;
                }
                revalidate();
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }
    
    
    
    @Override
    public Dimension getPreferredSize()
    {
        Dimension d = super.getPreferredSize();
        if (super.isPreferredSizeSet())
        {
            return d;
        }
        if (currentHeight != Integer.MAX_VALUE)
        {
            d.height = currentHeight;
        }
        return d;
    }
    

    @Override
    public Dimension getMinimumSize()
    {
        Dimension d = super.getMinimumSize();
        if (super.isMinimumSizeSet())
        {
            return d;
        }
        if (minimized)
        {
            d.height = minimizedHeight;
        }
        return d;
    }
    
    @Override
    public Dimension getMaximumSize()
    {
        Dimension d = super.getMaximumSize();
        if (super.isMaximumSizeSet())
        {
            return d;
        }
        if (minimized)
        {
            d.height = minimizedHeight;
        }
        return d;
    }
    
    
    
}
