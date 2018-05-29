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
package de.javagl.common.ui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;

/**
 * An implementation of the LayoutManager interface, for containers
 * that contain components that should be as large as possible under 
 * the constraint that a given aspect ratio (width/height) is 
 * maintained for each of them.
 */
public final class AspectLayout implements LayoutManager
{
    /**
     * The aspect ratio that should be maintained
     */
    private final double aspect;
    
    /**
     * The alignment in horizontal or vertical direction.
     * If there is extra space in either direction, then
     * the component will aligned according to this value
     */
    private final double alignment;
    
    /**
     * Creates a new AspectLayout for the given aspect. This is
     * equivalent to calling  {@link #AspectLayout(double, double)}
     * with an alignment of 0.5.  
     * 
     * @param aspect The aspect for the contained component
     */
    public AspectLayout(double aspect)
    {
        this(aspect, 0.5);
    }

    /**
     * Creates a new AspectLayout for the given aspect and alignment.<br>
     * <br>
     * The layout will maintain the given aspect ratio for all its
     * child components, arranging the child components in a grid
     * so that as little space as possible is wasted.<br>
     * <br>
     * If there is any excess space for the grid cells, the components
     * will be aligned in their cells based on the given alignment 
     * value. The alignment is a value in [0,1], where a value of 
     * 0.0 means that the components are aligned at the left/top of
     * their grid cell, 0.5 that they are centered, and 1.0 means 
     * that they are aligned at the bottom/right of their grid cell.
     * 
     * @param aspect The aspect for the contained component
     * @param alignment The alignment for the contained component
     */
    public AspectLayout(double aspect, double alignment)
    {
        this.aspect = aspect;
        this.alignment = alignment;
    }
    
    @Override
	public void layoutContainer(Container parent)
    {
        synchronized (parent.getTreeLock()) 
        {
            int componentCount = parent.getComponentCount();
            if (componentCount == 0)
            {
                return;
            }
            Insets insets = parent.getInsets();
            int insetsX = insets.left;
            int insetsY = insets.top;
            int totalSizeX = parent.getWidth() - (insetsX + insets.right);
            int totalSizeY = parent.getHeight() - (insetsY + insets.bottom);
            
            Point gridSize = computeGridSize(parent, componentCount, aspect);
            int cellSizeX = totalSizeX / gridSize.x;
            int cellSizeY = totalSizeY / gridSize.y;
            
            for (int cx = 0; cx < gridSize.x; cx++)
            {
                for (int cy = 0; cy < gridSize.y; cy++)
                {
                    int componentIndex = cx + cy * gridSize.x;
                    if (componentIndex < componentCount)
                    {
                        Component component = 
                            parent.getComponent(componentIndex);
                        int cellX = insetsX + cx * cellSizeX;
                        int cellY = insetsY + cy * cellSizeY;
                        layoutChild(component, 
                            cellX, cellY, cellSizeX, cellSizeY);
                    }
                }
            }
        }
    }

    /**
     * Lay out the given child component inside its parent, obeying
     * the aspect ratio and alignment constraints of this layout.
     * 
     * @param component The child component
     * @param cellX The (pixel) x-coordinate of the cell inside the parent
     * @param cellY The (pixel) y-coordinate of the cell inside the parent
     * @param cellSizeX The cell size in x-direction, in pixels
     * @param cellSizeY The cell size in y-direction, in pixels
     */
    private void layoutChild(
        Component component, int cellX, int cellY, int cellSizeX, int cellSizeY)
    {
        int maxAspectW = (int)(cellSizeY * aspect);
        int maxAspectH = (int)(cellSizeX / aspect);
        
        if (maxAspectW > cellSizeX)
        {
            int w = cellSizeX;
            int h = maxAspectH;
            int space = cellSizeY - h;
            int offset = (int)(alignment * space);
            component.setBounds(cellX, cellY + offset, w, h);
        }
        else
        {
            int w = maxAspectW;
            int h = cellSizeY;
            int space = cellSizeX - w;
            int offset = (int)(alignment * space);
            component.setBounds(cellX + offset, cellY, w, h);
        }
    }
    
    /**
     * Compute a grid size for the given container, for the given number 
     * of components with the specified aspect ratio, optimizing the 
     * number of rows/columns so that the space is used optimally.
     * 
     * @param container The container
     * @param numComponents The number of components
     * @param aspect The aspect ratio of the components
     * @return A point (x,y) storing the (columns,rows) that the
     * grid should have in order to waste as little space as possible
     */
    private static Point computeGridSize(
        Container container, int numComponents, double aspect)
    {
        double containerSizeX = container.getWidth();
        double containerSizeY = container.getHeight();
        double minTotalWastedSpace = Double.MAX_VALUE;
        int minWasteGridSizeX = -1;
        for (int gridSizeX = 1; gridSizeX <= numComponents; gridSizeX++)
        {
            int gridSizeY = numComponents / gridSizeX;
            if (gridSizeX * gridSizeY < numComponents)
            {
                gridSizeY++;
            }
            double cellSizeX = containerSizeX / gridSizeX;
            double cellSizeY = containerSizeY / gridSizeY;
            double wastedSpace =
                computeWastedSpace(cellSizeX, cellSizeY, aspect);
            double totalWastedSpace = gridSizeX * gridSizeY * wastedSpace;
            if (totalWastedSpace < minTotalWastedSpace)
            {
                minTotalWastedSpace = totalWastedSpace;
                minWasteGridSizeX = gridSizeX;
            }
        }
        int gridSizeX = minWasteGridSizeX;
        int gridSizeY = numComponents / gridSizeX;
        if (gridSizeX * gridSizeY < numComponents)
        {
            gridSizeY++;
        }
        return new Point(gridSizeX, gridSizeY);
    }
    
    /**
     * Compute the wasted space that is implied by the specified layout
     * 
     * @param maxSizeX The maximum size in x-direction 
     * @param maxSizeY The maximum size in y-direction
     * @param aspect The aspect ratio
     * @return The wasted space
     */
    private static double computeWastedSpace(
        double maxSizeX, double maxSizeY, double aspect)
    {
        int maxAspectX = (int) (maxSizeY * aspect);
        int maxAspectY = (int) (maxSizeX / aspect);
        if (maxAspectX > maxSizeX)
        {
            double sizeX = maxSizeX;
            double sizeY = maxAspectY;
            double waste = maxSizeY - sizeY;
            return waste * sizeX;
        }
        double sizeX = maxAspectX;
        double sizeY = maxSizeY;
        double waste = maxSizeX - sizeX;
        return waste * sizeY;
    }
    

    @Override
	public Dimension minimumLayoutSize(Container parent)
    {
        if (parent.getComponentCount() == 0)
        {
            return new Dimension(0,0);
        }
        Component component = parent.getComponent(0);
        return component.getMinimumSize();
    }

    @Override
	public Dimension preferredLayoutSize(Container parent)
    {
        if (parent.getComponentCount() == 0)
        {
            return new Dimension(0,0);
        }
        Component component = parent.getComponent(0);
        return component.getPreferredSize();
    }

    @Override
    public void addLayoutComponent(String name, Component comp)
    {
        // Nothing to do here
    }
    
    @Override
	public void removeLayoutComponent(Component comp)
    {
        // Nothing to do here
    }

    @Override
    public String toString() 
    {
        return getClass().getName() + "[aspect=" + aspect + "]";
    }
    
}
