# CommonUI

Common UI utility classes for the javagl libraries.

These classes should not be considered as part of a public, stable API, 
and are not intended to be used by third party libraries.

    <dependency>
        <groupId>de.javagl</groupId>
        <artifactId>common-ui</artifactId>
        <version>0.0.3</version>
    </dependency>


# Change log:

Version 0.0.4-SNAPSHOT:

* ...

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
