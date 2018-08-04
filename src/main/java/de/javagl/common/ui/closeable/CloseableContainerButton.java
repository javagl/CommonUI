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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * The button for closing a container, either a {@link CloseableTab} or
 * a {@link CloseablePanel}
 */
class CloseableContainerButton extends JButton
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
     * 
     * @param closeActionListener The action listener 
     */
    CloseableContainerButton(ActionListener closeActionListener)
    {
        int size = 17;
        setPreferredSize(new Dimension(size, size));
        setToolTipText("Close");
        setContentAreaFilled(false);
        setFocusable(false);
        
        setBorder(BorderFactory.createEtchedBorder());
        setBorderPainted(false);
        addMouseListener(rolloverMouseListener);
        setRolloverEnabled(true);
        
        addActionListener(closeActionListener);
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
