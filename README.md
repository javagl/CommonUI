# CommonUI

Common UI utility classes for the javagl libraries.

These classes should not be considered as part of a public, stable API, 
and are not intended to be used by third party libraries.

    <dependency>
        <groupId>de.javagl</groupId>
        <artifactId>common-ui</artifactId>
        <version>0.0.4</version>
    </dependency>


# Change log:

Version 0.0.7-SNAPSHOT:

* ...

Version 0.0.6, 2019-04-17

* Added `JOptionPanes` class with a method to show an option pane that does 
  an input validation
* Added a `ToggleListSelectionModel` that allows toggling the selection
  state with clicks
* Added `GenericTableModel` that builds the table contents with accessor
  functions
* Added `CustomizedTableHeader` that allows adding custom components to
  a table header
* Added a `MultiColumnRegexFilter` that allows adding text fields to
  table columns that may be used to filter a table based on regular expressions
* Added various table cell renderers
* Added text component utilities: An `UndoRedoHandler` that generically adds
  undo/redo functionality to text components, and a `SearchableTextComponent`
  that adds a simple search functionality to text components  
  
Version 0.0.5, 2019-03-24

* Fixed preferred size handling for `CloseablePanel` without title
* Added validation and option to clear an `AccordionPanel`

Version 0.0.4, 2018-08-08:

* Moved the `CloseableTab` into an own package, now containing the 
  `CloseCallback` interface and related classes as top-level classes
  that are also used by the newly added `CloseablePanel`
* Added `AccordionPanel` to combine multiple `CollapsiblePanel` instances
* Bugfix in `CollapsiblePanel` that prevented expansion animation

Version 0.0.3, 2018-05-29:

* Minimized state changes and notifications in `CheckBoxTree`
* Extended `JTrees` class with additional methods
* Added `GenericTreeCellRenderer`, e.g. for for buttons in tree cells
* Added `AspectLayout`

Version 0.0.2:

* Bugfix in `GridBagLayouts#addRow`: Extra space was not properly distributed
* Refactored `FilteredTree` for higher performance

     
Version 0.0.1, 2015-11-25:

* Initial commit
