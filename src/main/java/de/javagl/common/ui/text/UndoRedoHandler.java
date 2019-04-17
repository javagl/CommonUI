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

import java.awt.event.InputEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;
import java.util.logging.Logger;

import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 * Utility class to attach an undo/redo functionality to a text component.<br>
 * <br>
 * <pre><code>
 * UndoRedoHandler undoRedoHandler = UndoRedoHandler.attach(textComponent);
 * ...
 * // Later, detach the handler, if desired:
 * undoRedoHandler.detach();
 * <code></pre>
 */
public class UndoRedoHandler
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(UndoRedoHandler.class.getName());

    /**
     * Attach an undo/redo handler to the given text component. The returned
     * instance may be used to call {@link #detach()}, to remove the handler
     * from the given component if necessary.
     * 
     * @param textComponent The text component
     * @return The handler instance
     */
    public static UndoRedoHandler attach(JTextComponent textComponent)
    {
        return new UndoRedoHandler(textComponent);
    }
    
    /**
     * The name for the undo action
     */
    private static final String DO_UNDO_NAME = "UndoRedoHandler.doUndo";

    /**
     * The name for the redo action
     */
    private static final String DO_REDO_NAME = "UndoRedoHandler.doRedo";

    /**
     * The text component that this handler is attached to
     */
    private final JTextComponent textComponent;
    
    /**
     * The key stroke for the undo action
     */
    private final KeyStroke undoKeyStroke;

    /**
     * The key stroke for the redo action
     */
    private final KeyStroke redoKeyStroke;
    
    /**
     * A listener for the document of the text component. When the document
     * changes, the relevant listeners will be detached from the old one
     * and re-attached to the new one
     */
    private final PropertyChangeListener documentPropertyChangeListener;
    
    /**
     * The listener for undoable edits
     */
    private final UndoableEditListener undoableEditListener;
    
    /**
     * Detach this handler from the text component. This will remove all
     * listeners that have been attached to the text component, its 
     * document, and the elements of its input- and action maps.
     */
    public void detach()
    {
        textComponent.removePropertyChangeListener(
            documentPropertyChangeListener);
        Document document = textComponent.getDocument();
        document.removeUndoableEditListener(undoableEditListener);
        textComponent.getInputMap().remove(undoKeyStroke);
        textComponent.getActionMap().remove(DO_UNDO_NAME);
        textComponent.getInputMap().remove(redoKeyStroke);
        textComponent.getActionMap().remove(DO_REDO_NAME);
    }
    
    /**
     * Default constructor
     * 
     * @param textComponent The text component
     */
    private UndoRedoHandler(JTextComponent textComponent)
    {
        this.textComponent = Objects.requireNonNull(
            textComponent, "The textComponent may not be null");
        
        this.undoKeyStroke = 
            KeyStroke.getKeyStroke('Z', InputEvent.CTRL_MASK);
        this.redoKeyStroke = 
            KeyStroke.getKeyStroke('Y', InputEvent.CTRL_MASK);
        
        documentPropertyChangeListener = new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent e)
            {
                if ("document".equals(e.getPropertyName()))
                {
                    Object oldObject = e.getOldValue();
                    Document oldDocument = (Document)oldObject;
                    if (oldDocument != null)
                    {
                        oldDocument.removeUndoableEditListener(
                            undoableEditListener);
                    }
                    Object newObject = e.getNewValue();
                    Document newDocument = (Document)newObject;
                    if (newDocument != null)
                    {
                        newDocument.addUndoableEditListener(
                            undoableEditListener);
                    }
                }
            }
        };
        textComponent.addPropertyChangeListener(documentPropertyChangeListener);
        
        UndoManager undo = new UndoManager();
        Document document = textComponent.getDocument();
        undoableEditListener = new UndoableEditListener()
        {
            @Override
            public void undoableEditHappened(UndoableEditEvent e)
            {
                undo.addEdit(e.getEdit());
            }
        };
        document.addUndoableEditListener(undoableEditListener);
        
        textComponent.getInputMap().put(undoKeyStroke, DO_UNDO_NAME);
        textComponent.getActionMap().put(DO_UNDO_NAME, Actions.create(() -> 
        {
            try
            {
                if (undo.canUndo())
                {
                    undo.undo();
                }
            }
            catch (CannotUndoException ex)
            {
                logger.warning(ex.toString());
            }
        }));
        
        textComponent.getInputMap().put(redoKeyStroke, DO_REDO_NAME);
        textComponent.getActionMap().put(DO_REDO_NAME, Actions.create(() -> 
        {
            try
            {
                if (undo.canRedo())
                {
                    undo.redo();
                }
            }
            catch (CannotRedoException ex)
            {
                logger.warning(ex.toString());
            }
        })); 
    }    

}
