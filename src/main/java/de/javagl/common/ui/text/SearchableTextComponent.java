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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

/**
 * A class that is wrapped around a text component, and adds search 
 * functionality
 */
public class SearchableTextComponent extends JPanel
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(SearchableTextComponent.class.getName());
    
    /**
     * Serial UID
     */
    private static final long serialVersionUID = -2539643515896947981L;

    /**
     * The text component
     */
    private final JTextComponent textComponent;
    
    /**
     * The {@link SearchPanel}
     */
    private final SearchPanel searchPanel;
    
    /**
     * Whether the {@link #searchPanel} is currently visible
     */
    private boolean searchPanelVisible;
    
    /**
     * The mapping from points (containing the start and end indices)
     * to the highlight handles
     */
    private final Map<Point, Object> highlights;

    /**
     * The color used for highlighting
     */
    private final Color highlightColor = new Color(128, 255, 128, 64);
    
    /**
     * Creates a new instance
     * 
     * @param textComponent The text component
     */
    public SearchableTextComponent(JTextComponent textComponent)
    {
        super(new BorderLayout());
        this.textComponent = Objects.requireNonNull(
            textComponent, "The textComponent may not be null");
        
        this.highlights = new LinkedHashMap<Point, Object>();

        JScrollPane scrollPane = new JScrollPane(textComponent);
        add(scrollPane, BorderLayout.CENTER);

        this.searchPanel = new SearchPanel(this);
        
        KeyStroke doSearchKeyStroke = 
            KeyStroke.getKeyStroke('F', InputEvent.CTRL_DOWN_MASK);
        textComponent.getInputMap().put(
            doSearchKeyStroke, 
            "SearchableTextComponent.doSearch");
        textComponent.getActionMap().put(
            "SearchableTextComponent.doSearch", 
            Actions.create(this::doSearch));
        
        KeyStroke findNextKeyStroke = 
            KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);
        textComponent.getInputMap(JComponent.WHEN_FOCUSED).put(
            findNextKeyStroke, 
            "SearchableTextComponent.doFindNext");
        textComponent.getActionMap().put(
            "SearchableTextComponent.doFindNext", 
            Actions.create(this::doFindNext));
        
        KeyStroke findPreviousKeyStroke =
            KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.SHIFT_DOWN_MASK);
        textComponent.getInputMap(JComponent.WHEN_FOCUSED).put(
            findPreviousKeyStroke, 
            "SearchableTextComponent.doFindPrevious");
        textComponent.getActionMap().put(
            "SearchableTextComponent.doFindPrevious", 
            Actions.create(this::doFindPrevious));
     
        // The listener to update the search results
        textComponent.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                updateSearchResults(false);
            }
            
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                updateSearchResults(false);
            }
            
            @Override
            public void changedUpdate(DocumentEvent e)
            {
                updateSearchResults(false);
            }
        });
        
    }
    
    /**
     * Called to initiate the search. Will show the search panel, and
     * set the currently selected text as the query
     */
    private void doSearch()
    {
        setSearchPanelVisible(true);
        String selectedText = textComponent.getSelectedText();
        if (selectedText != null)
        {
            searchPanel.setQuery(selectedText);
        }
        searchPanel.requestFocusForTextField();
    }
    
    /**
     * Set whether the search panel is currently visible
     * 
     * @param b The state
     */
    void setSearchPanelVisible(boolean b)
    {
        if (!searchPanelVisible && b)
        {
            add(searchPanel, BorderLayout.NORTH);
            revalidate();
        }
        else if (searchPanelVisible && !b)
        {
            remove(searchPanel);
            revalidate();
        }
        searchPanelVisible = b;
    }

    /**
     * Find the next appearance of the search panel query 
     */
    void doFindNext()
    {
        String query = searchPanel.getQuery();
        if (query.isEmpty())
        {
            return;
        }
        
        String text = getDocumentText();
        boolean ignoreCase = !searchPanel.isCaseSensitive();

        int caretPosition = textComponent.getCaretPosition();
        int textLength = text.length();
        int newCaretPosition = (caretPosition + 1) % textLength;
        Point match = JTextComponents.findNext(
            text, query, newCaretPosition, ignoreCase);
        if (match == null)
        {
            match = JTextComponents.findNext(
                text, query, 0, ignoreCase);
        }
        handleMatch(match);
    }
    
    /**
     * Find the previous appearance of the search panel query 
     */
    void doFindPrevious()
    {
        String query = searchPanel.getQuery();
        if (query.isEmpty())
        {
            return;
        }
        
        String text = getDocumentText();
        boolean ignoreCase = !searchPanel.isCaseSensitive();

        int caretPosition = textComponent.getCaretPosition();
        int textLength = text.length();
        int newCaretPosition = (caretPosition - 1);
        if (newCaretPosition < 0) 
        {
            newCaretPosition += textLength;
        }
        Point match = JTextComponents.findPrevious(
            text, query, newCaretPosition, ignoreCase);
        if (match == null)
        {
            match = JTextComponents.findPrevious(
                text, query, textLength - 1, ignoreCase);
        }
        handleMatch(match);
    }
    
    /**
     * Return the text of the document of the text component
     * 
     * @return The text
     */
    private String getDocumentText()
    {
        try
        {
            Document document = textComponent.getDocument();
            String text = document.getText(0, document.getLength());
            return text;
        }
        catch (BadLocationException e)
        {
            logger.warning(e.toString());
            return textComponent.getText();
        }
    }
    
    /**
     * Update the search results (highlighting) when the query in the
     * search panel changed
     */
    void updateSearchResults()
    {
        updateSearchResults(true);
    }
    
    /**
     * Update the search results (highlighting) when the query in the
     * search panel changed
     * 
     * @param selectFirstMatch Whether the first match should be selected
     */
    void updateSearchResults(boolean selectFirstMatch)
    {
        clearHighlights();
        searchPanel.setMessage("");
        
        String query = searchPanel.getQuery();

        if (query.isEmpty())
        {
            handleMatch(null);
            return;
        }
        
        String text = getDocumentText();
        boolean ignoreCase = !searchPanel.isCaseSensitive();
        List<Point> appearances = 
            JTextComponents.find(text, query, ignoreCase);
        addHighlights(appearances, highlightColor);

        int caretPosition = textComponent.getCaretPosition();
        Point match = JTextComponents.findNext(
            text, query, caretPosition, ignoreCase);
        if (match == null)
        {
            match = JTextComponents.findNext(
                text, query, 0, ignoreCase);
        }
        if (selectFirstMatch)
        {
            handleMatch(match);
        }
        else
        {
            handleMatch(null);
        }

        if (appearances.isEmpty())
        {
            searchPanel.setMessage("Not found");
        }
        else
        {
            searchPanel.setMessage(
                "Found " + appearances.size() + " times");
        }
    }
    
    /**
     * Handle a match described by the given point. If the given point is
     * <code>null</code>, then the current selection will be cleared.
     * Otherwise, the range denoted by the point will be selected
     * 
     * @param match The match
     */
    private void handleMatch(Point match)
    {
        if (match == null)
        {
            int caretPosition = textComponent.getCaretPosition();
            Document document = textComponent.getDocument();
            caretPosition = Math.max(0, Math.min(
                document.getLength(), caretPosition));
            textComponent.setCaretPosition(caretPosition);
            textComponent.moveCaretPosition(caretPosition);
            textComponent.getCaret().setSelectionVisible(false);
        }
        else
        {
            textComponent.setCaretPosition(match.y);
            textComponent.moveCaretPosition(match.x);
            textComponent.getCaret().setSelectionVisible(true);
        }
    }
    
    /**
     * Add highlights with the given color to the text component for all
     * the given points
     * 
     * @param points The points, containing start and end indices
     * @param color The color
     */
    private void addHighlights(Collection<? extends Point> points, Color color)
    {
        removeHighlights(points);
        Map<Point, Object> newHighlights = 
            JTextComponents.addHighlights(textComponent, points, color);
        highlights.putAll(newHighlights);
    }
    
    /**
     * Remove the highlights that are associated with the given points
     * 
     * @param points The points
     */
    private void removeHighlights(Collection<? extends Point> points)
    {
        Set<Object> highlightsToRemove = new LinkedHashSet<Object>();
        for (Point point : points)
        {
            Object oldHighlight = highlights.remove(point);
            if (oldHighlight != null)
            {
                highlightsToRemove.add(oldHighlight);
            }
        }
        JTextComponents.removeHighlights(textComponent, highlightsToRemove);
    }
    
    /**
     * Clear all highlights
     */
    private void clearHighlights()
    {
        removeHighlights(new ArrayList<Point>(highlights.keySet()));
    }
    
}