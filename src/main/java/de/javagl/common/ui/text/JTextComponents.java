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

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

/**
 * Utility methods for text components
 */
class JTextComponents
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(JTextComponents.class.getName());
    
    /**
     * Find the next appearance of the given query in the given text, 
     * starting at the given index. The result will be a point 
     * <code>(startIndex, endIndex)</code> indicating the appearance, 
     * or <code>null</code> if none is found.
     * 
     * @param text The text
     * @param query The query
     * @param startIndex The start index
     * @param ignoreCase Whether the case should be ignored
     * @return The next appearance
     */
    static Point findNext(
        String text, String query, int startIndex, boolean ignoreCase)
    {
        int offset = StringUtils.indexOf(
            text, query, startIndex, ignoreCase);
        int length = query.length();
        while (offset != -1)
        {
            return new Point(offset, offset + length);
        }
        return null;
    }
    
    /**
     * Find the previous appearance of the given query in the given text, 
     * starting at the given index. The result will be a point 
     * <code>(startIndex, endIndex)</code> indicating the appearance, 
     * or <code>null</code> if none is found.
     * 
     * @param text The text
     * @param query The query
     * @param startIndex The start index
     * @param ignoreCase Whether the case should be ignored
     * @return The previous appearance
     */
    static Point findPrevious(
        String text, String query, int startIndex, boolean ignoreCase)
    {
        int offset = StringUtils.lastIndexOf(
            text, query, startIndex, ignoreCase);
        int length = query.length();
        while (offset != -1)
        {
            return new Point(offset, offset + length);
        }
        return null;
    }
    
    /**
     * Find all appearances of the given query in the given text. The
     * result will be points <code>(startIndex, endIndex)</code> indicating 
     * the appearances.
     * 
     * @param text The text
     * @param query The query
     * @param ignoreCase Whether the case should be ignored
     * @return The appearances
     */
    static List<Point> find(
        String text, String query, boolean ignoreCase)
    {
        List<Point> appearances = new ArrayList<Point>();
        int offset = StringUtils.indexOf(text, query, 0, ignoreCase);
        int length = query.length();
        while (offset != -1)
        {
            appearances.add(new Point(offset, offset + length));
            offset = StringUtils.indexOf(text, query, offset + 1, ignoreCase);
        }
        return appearances;
    }
    
    /**
     * Add the given highlights to the given text component.
     * 
     * @param textComponent The text component
     * @param appearances The appearances
     * @param color The color
     * @return The mapping from appearances to highlights
     */
    static Map<Point, Object> addHighlights(JTextComponent textComponent,
        Iterable<? extends Point> appearances, Color color)
    {
        Highlighter.HighlightPainter painter =
            new DefaultHighlighter.DefaultHighlightPainter(color);
        Highlighter highlighter = textComponent.getHighlighter();
        Map<Point, Object> highlights = new LinkedHashMap<Point, Object>();
        for (Point appearance : appearances)
        {
            try
            {
                Object highlight = highlighter.addHighlight(
                    appearance.x, appearance.y, painter);
                highlights.put(appearance, highlight);
            }
            catch (BadLocationException e)
            {
                // Should never happen
                logger.severe(e.toString());
            }
        }
        return highlights;
    }

    /**
     * Remove the given highlights from the given text component
     * 
     * @param textComponent The text component
     * @param highlights The highlights
     */
    static void removeHighlights(
        JTextComponent textComponent, Iterable<?> highlights)
    {
        Highlighter highlighter = textComponent.getHighlighter();
        for (Object highlight : highlights)
        {
            highlighter.removeHighlight(highlight);
        }
    }

    /**
     * Private constructor to prevent instantiation
     */
    private JTextComponents()
    {
        // Private constructor to prevent instantiation
    }

    
}
