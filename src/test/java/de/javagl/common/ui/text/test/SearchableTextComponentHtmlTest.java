package de.javagl.common.ui.text.test;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import de.javagl.common.ui.text.SearchableTextComponent;

/**
 * An integration test for the {@link SearchableTextComponent} with HTML
 */
@SuppressWarnings("javadoc")
public class SearchableTextComponentHtmlTest
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }

    private static void createAndShowGui()
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        int n = 100;
        for (int i = 0; i < n; i++)
        {
            sb.append("This is ");
            if (i % 2 == 0)
            {
                sb.append("<em>line</em>");
            }
            else
            {
                sb.append("<b>line</b>");
            }
            sb.append(" " + i + " of " + n + "<br>");
        }
        sb.append("</html>");

        JEditorPane editorPane = new JEditorPane("text/html", null);
        editorPane.setText(sb.toString());
        editorPane.setCaretPosition(0);
        
        SearchableTextComponent t = new SearchableTextComponent(editorPane);
        f.getContentPane().add(t);
        f.setSize(800, 500);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

    }

}
