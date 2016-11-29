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
package de.javagl.common.ui.tree.checkbox;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.plaf.FontUIResource;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import de.javagl.common.ui.JTrees;

/**
 * Simple implementation of a JTree that contains a check box at each node,
 * and allows querying the selection state of the nodes.<br>
 * <br>
 * NOTE: This is a <b>very</b> simple implementation that can not (yet)
 * handle changes in the backing tree model! Only intended for internal
 * use with tree models that do not change dynamically.
 */
public class CheckBoxTree extends JTree
{
    /**
     * The logger used in this class
     */
    static final Logger logger = 
        Logger.getLogger(CheckBoxTree.class.getName());

    /**
     * Serial UID
     */
    private static final long serialVersionUID = -7750694236316853481L;

    /**
     * The selection state of a node
     */
    public enum State
    {
        /**
         * The node is selected
         */
        SELECTED,
        
        /**
         * The node is unselected
         */
        UNSELECTED, 
        
        /**
         * The node is in a mixed state, meaning that some
         * of its children are selected and some are not
         * selected
         */
        MIXED
    }
    
    /**
     * Interface for classes that want to be informed about changes
     * of the {@link State} of nodes
     */
    public interface StateListener
    {
        /**
         * Will be called when the {@link State} of the given node changed
         * 
         * @param node The node
         * @param oldState The old state
         * @param newState The new state
         */
        void stateChanged(Object node, State oldState, State newState);
    }
    
    /**
     * Stores the selection state for each node
     */
    private final Map<Object, State> selectionStates;

    /**
     * The list of {@link StateListener}s
     */
    private final List<StateListener> stateListeners;
    
    /**
     * Creates a new checkbox tree with the given model
     * 
     * @param treeModel The tree model
     */
    public CheckBoxTree(TreeModel treeModel)
    {
        super(treeModel);
        selectionStates = new LinkedHashMap<Object, State>();
        stateListeners = new CopyOnWriteArrayList<StateListener>();
        
        CheckBoxRenderer checkBoxRenderer = 
            new CheckBoxRenderer(this, getCellRenderer());
        setCellRenderer(checkBoxRenderer);
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                handleMousePress(e);
            }
        });
        
        List<Object> allNodes = JTrees.getAllNodes(getModel());
        for (Object node : allNodes)
        {
            selectionStates.put(node, State.UNSELECTED);
        }
    }

    /**
     * Add the given {@link StateListener} to be informed about changes
     * of the {@link State} of a node.
     * 
     * @param stateListener The {@link StateListener}
     */
    public void addStateListener(StateListener stateListener)
    {
        stateListeners.add(stateListener);
    }
    
    /**
     * Remove the given {@link StateListener}
     * 
     * @param stateListener The {@link StateListener}
     */
    public void removeStateListener(StateListener stateListener)
    {
        stateListeners.add(stateListener);
    }
    
    /**
     * Notify all {@link StateListener}s about a {@link State} change
     * 
     * @param node The node whose state changed
     * @param oldState The old state
     * @param newState The new state
     */
    private void fireStateChanged(Object node, State oldState, State newState)
    {
        for (StateListener stateListener : stateListeners)
        {
            stateListener.stateChanged(node, oldState, newState);
        }
    }
    
    
    /**
     * Returns the selection state of the given node, or <code>null</code>
     * if the given object is not a node in this tree.
     * 
     * @param node The node
     * @return The selection state
     */
    public State getSelectionState(Object node)
    {
        return selectionStates.get(node);
    }
    
    /**
     * Select all nodes
     */
    public void selectAll()
    {
        setSelectionStateOfAll(State.SELECTED);
    }

    /**
     * Unselect all nodes
     */
    public void unselectAll()
    {
        setSelectionStateOfAll(State.UNSELECTED);
    }
    
    /**
     * Set the selection state of all nodes to the given state
     * 
     * @param state The state
     */
    private void setSelectionStateOfAll(State state)
    {
        Objects.requireNonNull(state, "The state may not be null");
        List<Object> allNodes = JTrees.getAllNodes(getModel());
        for (Object node : allNodes)
        {
            setSelectionState(node, state);
        }
    }
    
    /**
     * Set the selection state of the given node
     * 
     * @param node The node
     * @param state The state
     */
    public void setSelectionState(Object node, State state)
    {
        setSelectionState(node, state, true);
    }
    
    /**
     * Set the selection state of the given node
     * 
     * @param node The node
     * @param state The state
     * @param propagate Whether the state change should be propagated
     * to its children and ancestor nodes
     */
    private void setSelectionState(Object node, State state, boolean propagate)
    {
        Objects.requireNonNull(state, "The state may not be null");
        Objects.requireNonNull(node, "The node may not be null");
        State oldState = selectionStates.put(node, state);
        if (!state.equals(oldState))
        {
            fireStateChanged(node, oldState, state);
            if (propagate)
            {
                updateSelection(node);
            }
        }
        repaint();
    }
    
    
    
    /**
     * Handle a mouse press and possibly toggle the selection state 
     * 
     * @param e The mouse event
     */
    private void handleMousePress(MouseEvent e)
    {
        int row = getRowForLocation(e.getX(), e.getY());
        TreePath path = getPathForLocation(e.getX(), e.getY());
        if (path == null)
        {
            return;
        }
        TreeCellRenderer cellRenderer = getCellRenderer();
        TreeNode node = (TreeNode) path.getLastPathComponent();
        Component cellRendererComponent = 
            cellRenderer.getTreeCellRendererComponent(
                CheckBoxTree.this, null, true, true, node.isLeaf(), row, true);
        Rectangle bounds = getRowBounds(row);
        Point localPoint = new Point();
        localPoint.x = e.getX() - bounds.x;
        localPoint.y = e.getY() - bounds.y;
        Container container = (Container)cellRendererComponent;
        Component clickedComponent = null;
        for (Component component : container.getComponents())
        {
            Rectangle b = component.getBounds();
            if (b.contains(localPoint))
            {
                clickedComponent = component;
            }
        }
        if (clickedComponent != null)
        {
            if (clickedComponent instanceof JCheckBox)
            {
                toggleSelection(path);
                repaint();
            }
        }
    }
    
    /**
     * Toggle the selection of the given path due to a mouse click
     * 
     * @param path The path
     */
    private void toggleSelection(TreePath path)
    {
        Object node = path.getLastPathComponent();
        State state = getSelectionState(node);
        if (state == null)
        {
            return;
        }
        if (state == State.SELECTED)
        {
            setSelectionState(node, State.UNSELECTED);
            updateSelection(node);
        }
        else if (state == State.UNSELECTED)
        {
            setSelectionState(node, State.SELECTED);
            updateSelection(node);
        }
        else
        {
            setSelectionState(node, State.SELECTED);
            updateSelection(node);
        }
        
    }

    /**
     * Update the selection state of the given node based on the state
     * of its children
     * 
     * @param node The node
     */
    private void updateSelection(Object node)
    {
        State newState = getSelectionState(node);
        List<Object> descendants = 
            JTrees.getAllDescendants(getModel(), node);
        for (Object descendant : descendants)
        {
            setSelectionState(descendant, newState, false);
        }
        
        Object ancestor = JTrees.getParent(getModel(), node);
        while (ancestor != null)
        {
            List<Object> childrenOfAncestor = 
                JTrees.getChildren(getModel(), ancestor);
            State stateForAncestor = computeState(childrenOfAncestor);
            setSelectionState(ancestor, stateForAncestor, false);
            ancestor = JTrees.getParent(getModel(), ancestor);
        }
    }

    /**
     * Compute the state for a node with the given children
     * 
     * @param nodes The nodes
     * @return The state
     */
    private State computeState(List<Object> nodes)
    {
        Set<State> set = new LinkedHashSet<State>();
        for (Object node : nodes)
        {
            set.add(getSelectionState(node));
        }
        // Should never happen
        while (set.contains(null))
        {
            logger.warning("null found in selection states");
            set.remove(null);
        }
        if (set.size() == 0)
        {
            // Should never happen
            logger.warning("Empty selection states");
            return State.SELECTED;
        }
        if (set.size() > 1)
        {
            return State.MIXED;
        }
        return set.iterator().next();
    }
    

    /**
     * Implementation of a tree cell renderer that renders a check box
     */
    private static class CheckBoxRenderer extends JPanel 
        implements TreeCellRenderer 
    {
        /**
         * Serial UID
         */
        private static final long serialVersionUID = 6530238349185184391L;

        /**
         * The check box tree to which this renderer belongs
         */
        private final CheckBoxTree checkBoxTree;
        
        /**
         * The delegate renderer
         */
        private final TreeCellRenderer delegate;
        
        /**
         * The check box
         */
        private final JCheckBox checkBox;
        
        /**
         * The current component from the delegate renderer
         */
        private Component currentDelegateComponent;
        
        /**
         * Creates a new renderer
         * 
         * @param checkBoxTree The owner
         * @param delegate The delegate
         */
        CheckBoxRenderer(CheckBoxTree checkBoxTree, TreeCellRenderer delegate)
        {
            this.checkBoxTree = checkBoxTree;
            this.delegate = delegate;
            this.checkBox = new JCheckBox();
            setLayout(new BorderLayout());
            add(checkBox, BorderLayout.WEST);
            checkBox.setOpaque(false);
            setOpaque(false);
        }
        
        @Override
        public void setFont(Font font) 
        {
            if(font instanceof FontUIResource)
            {
                super.setFont(null);
            }
            else
            {
                super.setFont(font);
            }
        }
        

        @Override
        public Component getTreeCellRendererComponent(
            JTree tree, Object value, boolean selected,
            boolean expanded, boolean leaf, int row, boolean hasFocus) 
        {
            Component delegateComponent = 
                delegate.getTreeCellRendererComponent(
                    tree, value, selected, expanded, leaf, row, hasFocus);
            
            TreePath path = tree.getPathForRow(row);
            if (path != null)
            {
                Object node = path.getLastPathComponent();
                State state = checkBoxTree.getSelectionState(node);
                if (state == State.SELECTED)
                {
                    checkBox.setSelected(true);
                    checkBox.setEnabled(true);
                }
                else if (state == State.UNSELECTED)
                {
                    checkBox.setSelected(false);
                    checkBox.setEnabled(true);
                }
                else if (state == State.MIXED)
                {
                    checkBox.setSelected(true);
                    checkBox.setEnabled(false);
                }
            }
            if (currentDelegateComponent != null)
            {
                remove(currentDelegateComponent);
            }
            add(delegateComponent, BorderLayout.CENTER);
            currentDelegateComponent = delegateComponent;
            return this;
        }
    }    

    
}


