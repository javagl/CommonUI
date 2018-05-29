/*
 * www.javagl.de - Common - UI
 *
 * Copyright (c) 2013-2018 Marco Hutter - http://www.javagl.de
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
package de.javagl.common.ui.tree.renderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

/**
 * Base implementation of a {@link TreeCellRenderer} that allows
 * adding arbitrary components to the tree cells. 
 */
public abstract class GenericTreeCellRenderer implements TreeCellRenderer
{
    /**
     * The delegate {@link TreeCellRenderer}
     */
    private final DefaultTreeCellRenderer delegate = 
        new DefaultTreeCellRenderer();

    @Override
    public Component getTreeCellRendererComponent(
        JTree tree, Object value, boolean selected, boolean expanded, 
        boolean leaf, int row, boolean hasFocus) 
    {
        delegate.getTreeCellRendererComponent(
            tree, value, selected, expanded, leaf, row, hasFocus);
        JPanel component = new JPanel(new BorderLayout(5, 0));
        component.setOpaque(false);

        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(delegate.getIcon());
        component.add(iconLabel, BorderLayout.WEST);

        JPanel container = new JPanel(new GridLayout(1,1))
        {
            /**
             * Serial UID
             */
            private static final long serialVersionUID = -7852184126192862958L;

            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                g.setColor(getBackgroundColor());
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        
            /**
             * Returns the background color based on selection and focus
             * 
             * @return The background color
             */
            private Color getBackgroundColor()
            {
                if (selected || hasFocus)
                {
                    return delegate.getBackgroundSelectionColor();
                }
                Color color = delegate.getBackgroundNonSelectionColor();
                if (color == null)
                {
                    return delegate.getBackground();
                }
                return color;
            }
        };
        component.add(container, BorderLayout.CENTER);
        
        prepare(value, container);

        if (selected || hasFocus)
        {
            container.setBorder(BorderFactory.createLineBorder(
                delegate.getBorderSelectionColor()));
            container.setBackground(
                delegate.getBackgroundSelectionColor());
        }
        else
        {
            container.setBorder(
                BorderFactory.createEmptyBorder(1, 1, 1, 1));
            container.setBackground(null);
        }

        return component;
    }

    /**
     * This method will be called in order to prepare the returned cell 
     * renderer component for displaying the given object. The given
     * container will initially be empty. Arbitrary components may be
     * added to this container, in order to display the given 
     * tree node object or its contents in the tree cell. 
     * 
     * @param nodeObject The tree node object
     * @param container The target container
     */
    protected void prepare(Object nodeObject, JPanel container)
    {
        // Empty default implementation
    }
}