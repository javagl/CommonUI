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

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 * Methods related to JScrollPanes
 */
public class JScrollPanes
{
    /**
     * Creates a JScrollPane for vertically scrolling the given component.
     * The scroll pane will take into account that the vertical scroll bar 
     * will be shown when needed, and return a preferred size that takes 
     * the width of this scroll bar into account, so that when the vertical 
     * scroll bar appears, the contained component can still have its 
     * preferred width.
     * 
     * @param component The view component
     * @return The scroll pane
     */
    public static JScrollPane createVerticalScrollPane(
        final JComponent component)
    {
        JScrollPane scrollPane = new JScrollPane(component)
        {
            /**
             * Serial UID
             */
            private static final long serialVersionUID = -177913025197077320L;

            @Override
            public Dimension getPreferredSize() 
            {
                Dimension d = super.getPreferredSize();
                if (super.isPreferredSizeSet())
                {
                    return d;
                }
                JScrollBar scrollBar = getVerticalScrollBar();
                Dimension sd = scrollBar.getPreferredSize();
                d.width += sd.width;
                return d;
            }
            
            @Override
            public boolean isValidateRoot()
            {
                return false;
            }
        };
        return scrollPane;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private JScrollPanes()
    {
        // Private constructor to prevent instantiation
    }
}
