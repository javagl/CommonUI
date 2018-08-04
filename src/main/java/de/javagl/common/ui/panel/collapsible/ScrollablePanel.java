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

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.Scrollable;

/**
 * A panel that is scrollable. Hence the name.
 */
class ScrollablePanel extends JPanel implements Scrollable
{
    /**
     * Serial UID 
     */
    private static final long serialVersionUID = 1584089555383680990L;

    /**
     * The scrollable unit increment
     */
    private int scrollableUnitIncrement;

    /**
     * The scrollable block increment
     */
    private int scrollableBlockIncrement;
    
    /**
     * Whether this scrollable tracks the viewport width
     */
    private boolean scrollableTracksViewportWidth; 

    /**
     * Whether this scrollable tracks the viewport height
     */
    private boolean scrollableTracksViewportHeight; 
    
    /**
     * Default constructor. The exact configuration of this scrollable
     * is not specified.
     */
    public ScrollablePanel()
    {
        this.scrollableUnitIncrement = 10;
        this.scrollableBlockIncrement = 50;
        this.scrollableTracksViewportWidth = true;
        this.scrollableTracksViewportHeight = false;
    }
    
    @Override
    public Dimension getPreferredScrollableViewportSize()
    {
        return super.getPreferredSize();
    }
    
    /**
     * Set the scrollable unit increment. 
     * See {@link #getScrollableUnitIncrement(Rectangle, int, int)}
     * 
     * @param scrollableUnitIncrement The scrollable unit increment
     */
    public void setScrollableUnitIncrement(int scrollableUnitIncrement)
    {
        this.scrollableUnitIncrement = scrollableUnitIncrement;
    }
    
    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect,
        int orientation, int direction)
    {
        return scrollableUnitIncrement;
    }

    /**
     * Set the scrollable block increment.
     * See {@link #getScrollableBlockIncrement(Rectangle, int, int)}
     * 
     * @param scrollableBlockIncrement The scrollable block increment
     */
    public void setScrollableBlockIncrement(int scrollableBlockIncrement)
    {
        this.scrollableBlockIncrement = scrollableBlockIncrement;
    }
    
    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect,
        int orientation, int direction)
    {
        return scrollableBlockIncrement;
    }
    
    /**
     * Set whether this scrollable tracks the viewport width.
     * See {@link #getScrollableTracksViewportWidth()}
     * 
     * @param scrollableTracksViewportWidth Whether the width is tracked
     */
    public void setScrollableTracksViewportWidth(
        boolean scrollableTracksViewportWidth)
    {
        this.scrollableTracksViewportWidth = scrollableTracksViewportWidth;
    }

    @Override
    public boolean getScrollableTracksViewportWidth()
    {
        return scrollableTracksViewportWidth;
    }

    /**
     * Set whether this scrollable tracks the viewport height.
     * See {@link #getScrollableTracksViewportHeight()}
     * 
     * @param scrollableTracksViewportHeight Whether the height is tracked
     */
    public void setScrollableTracksViewportHeight(
        boolean scrollableTracksViewportHeight)
    {
        this.scrollableTracksViewportHeight = scrollableTracksViewportHeight;
    }
    
    @Override
    public boolean getScrollableTracksViewportHeight()
    {
        return scrollableTracksViewportHeight;
    }
    
}