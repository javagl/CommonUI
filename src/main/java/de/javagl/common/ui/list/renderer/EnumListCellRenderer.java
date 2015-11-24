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
package de.javagl.common.ui.list.renderer;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * A <code>ListCellRenderer</code> that renders <code>Enum</code> values
 * with their name transformed from the constant naming scheme to a 
 * "nicer", camel-case naming: <code>"SOME_ENUM_VALUE"</code> will be
 * transformed to <code>"Some Enum Value"</code>
 */
public final class EnumListCellRenderer 
    extends DefaultListCellRenderer
{
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 4328336723181463676L;

    @Override
    public Component getListCellRendererComponent(
        JList<?> list, Object value, int index,
        boolean isSelected, boolean cellHasFocus)
    {
        super.getListCellRendererComponent(
            list, value, index, isSelected, cellHasFocus);
        setText(transform(String.valueOf(value)));
        return this;
    }
    
    /**
     * Transform the given constant name, which is assumed to be in the
     * form <code>"SOME_CONSTANT_NAME"</code>, into a string that has
     * the form <code>"Some Constant Name"</code>.
     * 
     * @param constantNameString The constant name 
     * @return The transformed name
     */
    private static String transform(String constantNameString)
    {
        StringBuilder sb = new StringBuilder();
        String tokens[] = constantNameString.split("_");
        List<String> list = new ArrayList<String>();
        for (String token : tokens)
        {
            String t = token.trim();
            if (!t.isEmpty())
            {
                list.add(t);
            }
        }
        for (int i=0; i<list.size(); i++)
        {
            if (i > 0)
            {
                sb.append(" ");
            }
            String t = list.get(i);
            sb.append(Character.toUpperCase(t.charAt(0)));
            sb.append(t.substring(1).toLowerCase());
        }
        return sb.toString();
    }

//    // A basic test
//    public static void main(String[] args)
//    {
//        System.out.println(transform("CONSTANT"));
//        System.out.println(transform("CONSTANT_NAME"));
//        System.out.println(transform("SOME_CONSTANT_NAME"));
//        System.out.println(transform("_ODD"));
//        System.out.println(transform("ODD_"));
//        System.out.println(transform("ODD__ONE"));
//    }
    
}