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
package de.javagl.common.ui.closeable;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * Methods to create {@link CloseCallback} instances
 */
public class CloseCallbacks
{
    /**
     * Returns a {@link CloseCallback} that always returns <code>true</code>
     * 
     * @return The {@link CloseCallback}
     */
    public static CloseCallback alwaysTrue()
    {
        return new CloseCallback()
        {
            @Override
            public boolean mayClose(Component componentInTab)
            {
                return true;
            }
        };
    }

    /**
     * Returns a {@link CloseCallback} that shows a confirmation dialog
     * 
     * @return The {@link CloseCallback}
     */
    public static CloseCallback confirming()
    {
        return confirming("Close this container?");
    }
    
    /**
     * Returns a {@link CloseCallback} that shows a confirmation dialog that
     * asks the given question
     * 
     * @param question The question
     * @return The {@link CloseCallback}
     */
    public static CloseCallback confirming(String question)
    {
        return new CloseCallback()
        {
            @Override
            public boolean mayClose(Component componentInTab)
            {
                int option = JOptionPane.showConfirmDialog(
                    componentInTab, question, 
                    "Confirm", JOptionPane.YES_NO_OPTION);
                return option == JOptionPane.YES_OPTION;
            }
        };
    }

    /**
     * Private constructor to prevent instantiation
     */
    private CloseCallbacks()
    {
        // Private constructor to prevent instantiation
    }
    
}
