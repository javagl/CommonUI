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
package de.javagl.common.ui.utils.desktop;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JInternalFrame.JDesktopIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/**
 * A layout manager for {@link JDesktopPane}s.<br>
 * <br>
 * <b>This class is even more preliminary than all other classes in 
 * this library!</b>
 */
public class JDesktopPaneLayout
{
    /**
     * A container that is associated with a desktop pane layout.
     * It is used internally for computing the actual layout.
     */
    private static class LayoutContainer extends Container
    {
        /**
         * Serial UID
         */
        private static final long serialVersionUID = 6455895807345631762L;
        
        /**
         * The owner of this container
         */
        private final JDesktopPaneLayout owner;
        
        /**
         * Creates a new layout container with the given owner
         * 
         * @param owner The owner
         */
        LayoutContainer(JDesktopPaneLayout owner)
        {
            this.owner = owner;
        }
    }

    /**
     * The class for the dummy components that are added to the container
     * to determine the size of the internal frames
     */
    private class FrameComponent extends JPanel
    {
        /**
         * Serial UID
         */
        private static final long serialVersionUID = 7397992807282457681L;
        
        /**
         * The internal frame for which this component was created
         */
        private final JInternalFrame internalFrame;
        
        /**
         * Creates a new dummy component for the given internal frame,
         * which was added with the given layout information
         * 
         * @param internalFrame The internal frame
         * @param constraints The constraints
         * @param index The index
         */
        FrameComponent(
            JInternalFrame internalFrame, 
            final Object constraints, final int index)
        {
            this.internalFrame = internalFrame;
            internalFrame.addInternalFrameListener(new InternalFrameAdapter()
            {
                @Override
                public void internalFrameIconified(InternalFrameEvent e)
                {
                    container.remove(FrameComponent.this);
                }
                
                @Override
                public void internalFrameDeiconified(InternalFrameEvent e)
                {
                    container.add(FrameComponent.this, constraints, index);
                }
                
                @Override
                public void internalFrameClosed(InternalFrameEvent e)
                {
                    container.remove(FrameComponent.this);
                }
                
            });
        }
        
        /**
         * Returns the internal frame for which this component was created
         * 
         * @return The internal frame
         */
        JInternalFrame getInternalFrame()
        {
            return internalFrame;
        }
        
        @Override
        public Dimension getPreferredSize()
        {
            return internalFrame.getPreferredSize();
        }
        @Override
        public Dimension getMaximumSize()
        {
            return internalFrame.getMaximumSize();
        }
        @Override
        public Dimension getMinimumSize()
        {
            return internalFrame.getMinimumSize();
        }
    }
    
    /**
     * The parent layout
     */
    private final JDesktopPaneLayout parent;
    
    /**
     * The desktop pane that this layout manager is applied to 
     */
    private final JDesktopPane desktopPane;
    
    /**
     * The dummy container that will be used for computing the layout
     */
    private final LayoutContainer container;
    
    /**
     * The root container. This is the layout container of the root
     * of the hierarchy of desktop pane layouts.
     */
    private final LayoutContainer rootContainer;
    
    /**
     * A map from internal frames to {@link FrameComponent}s
     * that are placed into the dummy container
     */
    private final Map<JInternalFrame, FrameComponent> frameToComponent;
    
    /**
     * Creates a new desktop pane layout for the given desktop pane
     * 
     * @param desktopPane The desktop pane
     */
    public JDesktopPaneLayout(JDesktopPane desktopPane)
    {
        this(null, desktopPane, null);
    }
    
    /**
     * Constructor for a child layout that was created with 
     * {@link #createChild()}
     * 
     * @param parent The parent layout
     * @param desktopPane The desktop pane of the parent
     * @param rootContainer The root container of the parent
     */
    private JDesktopPaneLayout(
        JDesktopPaneLayout parent, 
        JDesktopPane desktopPane, 
        LayoutContainer rootContainer)
    {
        this.parent = parent;
        this.desktopPane = desktopPane;
        this.container = new LayoutContainer(this);
        if (rootContainer == null)
        {
            this.rootContainer = container;
        }
        else
        {
            this.rootContainer = rootContainer;
        }
        this.frameToComponent = new HashMap<JInternalFrame, FrameComponent>();
    }
    
    /**
     * Create a new desktop pane layout that may be added as a child
     * to this layout
     * 
     * @return The new child layout
     */
    public JDesktopPaneLayout createChild()
    {
        return new JDesktopPaneLayout(this, desktopPane, rootContainer);
    }
    
    /**
     * Add the given desktop pane layout as a child to this one
     * 
     * @param child The child to add
     * @throws IllegalArgumentException If the given child was not
     * created by calling {@link #createChild()} on this layout
     */
    public void add(JDesktopPaneLayout child)
    {
        add(child, null);
    }
    
    /**
     * Add the given desktop pane layout as a child to this one
     * 
     * @param child The child to add
     * @param constraints The constraints. 
     * See {@link Container#add(Component, Object)}
     * @throws IllegalArgumentException If the given child was not
     * created by calling {@link #createChild()} on this layout
     */
    public void add(JDesktopPaneLayout child, Object constraints)
    {
        add(child, constraints, -1);
    }

    /**
     * Add the given desktop pane layout as a child to this one
     * 
     * @param child The child to add
     * @param constraints The constraints. 
     * See {@link Container#add(Component, Object)}
     * @param index The index. 
     * See {@link Container#add(Component, Object, int)}
     * @throws IllegalArgumentException If the given child was not
     * created by calling {@link #createChild()} on this layout
     */
    public void add(JDesktopPaneLayout child, Object constraints, int index)
    {
        if (child.parent != this)
        {
            throw new IllegalArgumentException(
                "Layout is not a child of this layout");
        }
        container.add(child.container, constraints, index);
    }
    
    /**
     * Remove the given child layout
     *  
     * @param child The child to remove
     * @throws IllegalArgumentException If the given child was not
     * created by calling {@link #createChild()} on this layout
     */
    public void remove(JDesktopPaneLayout child)
    {
        if (child.parent != this)
        {
            throw new IllegalArgumentException(
                "Layout is not a child of this layout");
        }
        container.remove(child.container);
    }
    
    /**
     * Set the delegate layout manager for this layout
     * 
     * @param layoutManager The delegate layout manager
     */
    public void setLayout(LayoutManager layoutManager)
    {
        container.setLayout(layoutManager);
    }

    /**
     * Add the given internal frame to this layout manager.<br>
     * <br> 
     * Note that this will <b>not</b> add the internal frame to the 
     * desktop pane that this layout belongs to!
     * 
     * @param internalFrame The internal frame to add
     */
    public void add(JInternalFrame internalFrame)
    {
        add(internalFrame, null);
    }

    /**
     * Add the given internal frame to this layout manager.<br>
     * <br> 
     * Note that this will <b>not</b> add the internal frame to the 
     * desktop pane that this layout belongs to!
     * 
     * @param internalFrame The internal frame to add
     * @param constraints The constraints. 
     * See {@link Container#add(Component, Object)}
     */
    public void add(
        JInternalFrame internalFrame, Object constraints)
    {
        add(internalFrame, constraints, -1);
    }
    
    /**
     * Add the given internal frame to this layout manager.<br>
     * <br> 
     * Note that this will <b>not</b> add the internal frame to the 
     * desktop pane that this layout belongs to!
     * 
     * @param internalFrame The internal frame to add
     * @param constraints The constraints. 
     * See {@link Container#add(Component, Object)}
     * @param index The index. 
     * See {@link Container#add(Component, Object, int)}
     */
    public void add(
        JInternalFrame internalFrame, Object constraints, int index)
    {
        FrameComponent frameComponent = 
            new FrameComponent(internalFrame, constraints, index);
        frameToComponent.put(internalFrame, frameComponent);
        if (!internalFrame.isIcon())
        {
            container.add(frameComponent, constraints, index);
        }
    }

    /**
     * Remove the given internal frame from this layout.<br>
     * <br> 
     * Note that this will <b>not</b> remove the internal frame from the 
     * desktop pane that this layout belongs to!
     * 
     * @param internalFrame The internal frame to remove
     */
    void remove(JInternalFrame internalFrame)
    {
        Component component = frameToComponent.get(internalFrame);
        container.remove(component);
    }
    
    /**
     * Validate the layout after internal frames have been added
     * or removed
     */
    public void validate()
    {
        Dimension size = desktopPane.getSize();
        size.height -= computeDesktopIconsSpace();
        layoutInternalFrames(size);
    }
    
    /**
     * Compute the space for iconified desktop icons  
     * 
     * @return The space
     */
    private int computeDesktopIconsSpace()
    {
        for (JInternalFrame f : frameToComponent.keySet())
        {
            if (f.isIcon())
            {
                JDesktopIcon desktopIcon = f.getDesktopIcon();
                return desktopIcon.getPreferredSize().height;
            }
        }
        return 0;
    }
    
    /**
     * Layout the internal frames for the given size
     * 
     * @param size The size
     */
    private void layoutInternalFrames(Dimension size)
    {
        container.setSize(size);
        callDoLayout(container);
        applyLayout();
    }
    
    /**
     * Recursively call doLayout on the container and all its
     * sub-containers
     * 
     * @param container The container
     */
    private void callDoLayout(Container container)
    {
        container.doLayout();
        int n = container.getComponentCount();
        for (int i=0; i<n; i++)
        {
            Component component = container.getComponent(i);
            if (component instanceof Container)
            {
                Container subContainer = (Container)component;
                callDoLayout(subContainer);
            }
        }
    }
    
    
    /**
     * Apply the current layout to the internal frames
     */
    private void applyLayout()
    {
        int n = container.getComponentCount();
        for (int i=0; i<n; i++)
        {
            Component component = container.getComponent(i);
            if (component instanceof FrameComponent)
            {
                FrameComponent frameComponent = (FrameComponent)component;
                JInternalFrame internalFrame = 
                    frameComponent.getInternalFrame();
                Rectangle bounds = SwingUtilities.convertRectangle(
                    container, component.getBounds(), rootContainer);
                //System.out.println(
                //    "Set bounds of "+internalFrame.getTitle()+" to "+bounds);
                internalFrame.setBounds(bounds);
            }
            else
            {
                LayoutContainer childLayoutContainer = 
                    (LayoutContainer)component;
                //System.out.println(
                //    "Child with "+childLayoutContainer.getLayout());
                childLayoutContainer.owner.applyLayout();
            }
        }
    }
    
    
    
}
