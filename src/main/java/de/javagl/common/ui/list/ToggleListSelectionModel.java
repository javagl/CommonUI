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
package de.javagl.common.ui.list;

import javax.swing.DefaultListSelectionModel;

/**
 * A list selection model that allows toggling the selection state with clicks. 
 * Based on http://java.sun.com/products/jfc/tsc/tech_topics/jlist_1/jlist.html
 */
public class ToggleListSelectionModel extends DefaultListSelectionModel
{
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 7259767896570315488L;
    
    /**
     * Whether the value is currently adjusting
     */
    private boolean adjusting = false;
    
    /**
     * Default constructor
     */
    public ToggleListSelectionModel()
    {
        // Default constructor
    }

    @Override
    public void setSelectionInterval(int index0, int index1)
    {
        if (!adjusting)
        {
            if (isSelectedIndex(index0))
            {
                super.removeSelectionInterval(index0, index1);
            }
            else
            {
                if (getSelectionMode() == SINGLE_SELECTION) 
                {
                    super.setSelectionInterval(index0, index1);
                }
                else
                {
                    super.addSelectionInterval(index0, index1);
                }
            }
        }
        adjusting = true;
    }

    @Override
    public void setValueIsAdjusting(boolean isAdjusting)
    {
        if (!isAdjusting)
        {
            adjusting = false;
        }
    }
}
