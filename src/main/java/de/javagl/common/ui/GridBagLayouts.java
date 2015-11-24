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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Utility methods related to containers with a <code>GridBagLayout</code> 
 */
public class GridBagLayouts
{
    /**
     * Add the given components as a new row in the given container, 
     * which must have a <code>GridBagLayout</code>.
     * 
     * @param container The container
     * @param row The row
     * @param extraSpaceColumn The index of the column that should receive
     * any extra space
     * @param components The components to add
     * @throws IllegalArgumentException If the given container does 
     * not have a <code>GridBagLayout</code>.
     */
    public static void addRow(
        Container container, int row, int extraSpaceColumn, 
        Component ... components)
    {
        if (!(container.getLayout() instanceof GridBagLayout))
        {
            throw new IllegalArgumentException(
                "Container does not have a " +
                "GridBagLayout: "+container.getLayout());
        }
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.weighty = 1.0;
        constraints.weightx = 1.0;
        constraints.gridy = row;
        for (int i=0; i<components.length; i++)
        {
            Component component = components[i];
            constraints.gridx = i;
            if (i == extraSpaceColumn)
            {
                constraints.weightx = 0.0;
            }
            else
            {
                constraints.weightx = 1.0;
            }
            container.add(component, constraints);
        }
    }

    /**
     * Private constructor to prevent instantiation
     */
    private GridBagLayouts()
    {
        // Private constructor to prevent instantiation
    }
}
