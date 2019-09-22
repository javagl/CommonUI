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

import java.awt.event.ActionEvent;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Methods to create <code>Action</code> instances.<br>
 * <br>
 * The {@link #create(String)} method may be called to create an
 * {@link ActionBuilder} that allows setting the properties of
 * the actions fluently. Finally, the action can be created by
 * calling {@link ActionBuilder#build()}.
 */
public class Actions
{
    /**
     * An action where the action method may be set
     */
    private static class ActionBuilderAction extends AbstractAction
    {
        /**
         * Serial UID
         */
        private static final long serialVersionUID = 1L;
        
        /**
         * The method that is the implementation of {@link #actionPerformed}
         */
        private Consumer<? super ActionEvent> actionMethod;
        
        /**
         * Creates a new instance
         * 
         * @param name The name of the action
         */
        ActionBuilderAction(String name)
        {
            super(name);
        }
        
        /**
         * Set the method that is executed in {@link #actionPerformed}
         * 
         * @param actionMethod The action method
         */
        void setActionMethod(
            Consumer<? super ActionEvent> actionMethod)
        {
            this.actionMethod = actionMethod;
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (actionMethod != null)
            {
                actionMethod.accept(e);
            }
        }
    }
    
    /**
     * A builder for <code>Action</code> instances
     */
    public static class ActionBuilder
    {
        /**
         * The action that is currently built
         */
        private ActionBuilderAction action;
        
        /**
         * Creates a new instance
         * 
         * @param name The name of the action
         */
        ActionBuilder(String name)
        {
            this.action = new ActionBuilderAction(name);
        }

        /**
         * Set the name of the action
         * 
         * @param name The name
         * @return This builder
         */
        public ActionBuilder setName(String name)
        {
            action.putValue(AbstractAction.NAME, name);
            return this;
        }

        /**
         * Set the short description (tooltip) of the action
         * 
         * @param shortDescription The short description
         * @return This builder
         */
        public ActionBuilder setShortDescription(String shortDescription)
        {
            action.putValue(AbstractAction.SHORT_DESCRIPTION, shortDescription);
            return this;
        }

        /**
         * Set the mnemonic key of the action
         * 
         * @param key The key
         * @return This builder
         */
        public ActionBuilder setMnemonicKey(int key)
        {
            action.putValue(AbstractAction.MNEMONIC_KEY, Integer.valueOf(key));
            return this;
        }

        /**
         * Set the method that should serve as the implementation of the
         * <code>actionPerformed</code> method of the action
         * 
         * @param actionMethod the action method
         * @return This builder
         */
        public ActionBuilder setActionMethod(
            Consumer<? super ActionEvent> actionMethod)
        {
            action.setActionMethod(actionMethod);
            return this;
        }
        
        /**
         * Set the method that should serve as the implementation of the
         * <code>actionPerformed</code> method of the action
         * 
         * @param call the action method
         * @return This builder
         */
        public ActionBuilder setActionCall(Runnable call)
        {
            action.setActionMethod(e -> call.run());
            return this;
        }
        
        /**
         * Build the action
         * 
         * @return The action
         */
        public Action build()
        {
            return action;
        }
    }
    
    /**
     * Creates a new {@link ActionBuilder} to create an action with the
     * given name
     * 
     * @param name The name
     * @return The {@link ActionBuilder}
     * @throws NullPointerException If the name is <code>null</code>
     */
    public static ActionBuilder create(String name)
    {
        Objects.requireNonNull(name, "The name may not be null");
        return new ActionBuilder(name);
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private Actions()
    {
        // Doing naughty stuff here
    }
}
