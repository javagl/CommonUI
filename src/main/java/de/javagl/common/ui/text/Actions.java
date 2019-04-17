/*
 * www.javagl.de - Common - UI
 *
 * Copyright (c) 2013-2019 Marco Hutter - http://www.javagl.de
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
package de.javagl.common.ui.text;

import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Utility methods related to actions
 */
class Actions
{
    /**
     * Create a new action that simply executes the given command
     * 
     * @param command The command
     * @return The action
     */
    static Action create(Runnable command)
    {
        Objects.requireNonNull(command, "The command may not be null");
        return new AbstractAction()
        {
            /**
             * Serial UID
             */
            private static final long serialVersionUID = 8693271079128413874L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                command.run();
            }
        };
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private Actions()
    {
        // Private constructor to prevent instantiation
    }

}
