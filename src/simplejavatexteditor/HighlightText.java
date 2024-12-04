package simplejavatexteditor;

import javax.swing.text.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The HighlightText class provides functionality to highlight text in a JTextComponent
 * with different colors based on keyword categories.
 */
public class HighlightText extends DefaultHighlighter.DefaultHighlightPainter{
	private Map<String, Color> categoryColors = new HashMap<>();
	private Color defaultColor;
	
	private Map<Color, DefaultHighlighter.DefaultHighlightPainter> painterCache = new HashMap<>();

	private DefaultHighlighter.DefaultHighlightPainter getPainter(Color color) {
	    return painterCache.computeIfAbsent(color, DefaultHighlighter.DefaultHighlightPainter::new);
	}

	
	
	/**
     * Constructs a HighlightText object with a default color.
     *
     * @param defaultColor The default color to use for highlighting.
     */
    public HighlightText(Color defaultColor) {
        super(defaultColor);
        this.defaultColor = defaultColor;
    }
    
    /**
     * Sets the color for a specific keyword category.
     *
     * @param category The category of keywords.
     * @param color The color to use for highlighting this category.
     */
    public void setCategoryColor(String category, Color color) {
        categoryColors.put(category, color);
    }
    
    /**
     * Highlights the specified keywords in the given text component.
     *
     * @param textComp The text component to highlight.
     * @param pattern An array of keywords to highlight.
     */
    public void highLight(JTextComponent textComp, String[] pattern) {
        removeHighlights(textComp);
        
        try {
            Highlighter highlighter = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());

            for (String keyword : pattern) {
                int pos = 0;
                Color color = getCategoryColor(keyword);
                
                
                while ((pos = text.indexOf(keyword, pos)) >= 0) {
                  pos = findWholeWord(text, keyword, pos);
                    if (pos >= 0) {
                      highlighter.addHighlight(pos, pos + keyword.length(), getPainter(color));
                      pos += keyword.length();
                    }               
                }

            }
        } catch (BadLocationException e) {}

    }
    
    /**
     * Determines the color for a given keyword based on its category.
     *
     * @param keyword The keyword to get the color for.
     * @return The color associated with the keyword's category or the default color if keyword 
     * is present it SupportedKeywords but not given association here
     */
    private Color getCategoryColor(String keyword) {
    	// Class and interface keywords
        if (keyword.equals("class") || keyword.equals("interface") || keyword.equals("enum")) {
            return categoryColors.getOrDefault("classKeywords", Color.BLUE);
        }
        // Access modifiers
        else if (keyword.equals("public") || keyword.equals("private") || keyword.equals("protected")) {
            return categoryColors.getOrDefault("accessModifiers", Color.GREEN);
        }
        // Control flow keywords
        else if (keyword.equals("if") || keyword.equals("else") || keyword.equals("switch") || 
                 keyword.equals("case") || keyword.equals("default") || keyword.equals("for") || 
                 keyword.equals("while") || keyword.equals("do") || keyword.equals("break") || 
                 keyword.equals("continue")) {
            return categoryColors.getOrDefault("controlFlow", Color.ORANGE);
        }
        // Exception handling
        else if (keyword.equals("try") || keyword.equals("catch") || keyword.equals("finally") || 
                 keyword.equals("throw") || keyword.equals("throws")) {
            return categoryColors.getOrDefault("exceptionHandling", Color.RED);
        }
        // Primitive data types
        else if (keyword.equals("int") || keyword.equals("long") || keyword.equals("float") || 
                 keyword.equals("double") || keyword.equals("boolean") || keyword.equals("char") || 
                 keyword.equals("byte") || keyword.equals("short")) {
            return categoryColors.getOrDefault("primitiveTypes", Color.MAGENTA);
        }
        // Object-oriented keywords
        else if (keyword.equals("new") || keyword.equals("this") || keyword.equals("super") || 
                 keyword.equals("instanceof") || keyword.equals("extends") || keyword.equals("implements")) {
            return categoryColors.getOrDefault("oopKeywords", Color.CYAN);
        }
        // Modifiers
        else if (keyword.equals("static") || keyword.equals("final") || keyword.equals("abstract") || 
                 keyword.equals("synchronized") || keyword.equals("volatile") || keyword.equals("transient")) {
            return categoryColors.getOrDefault("modifiers", Color.PINK);
        }
        // Package and import
        else if (keyword.equals("package") || keyword.equals("import")) {
            return categoryColors.getOrDefault("packageImport", Color.GRAY);
        }
        // C++ specific keywords
        else if (keyword.equals("namespace") || keyword.equals("using") || keyword.equals("template") || 
                 keyword.equals("typename") || keyword.equals("virtual") || keyword.equals("friend")) {
            return categoryColors.getOrDefault("cppSpecific", Color.LIGHT_GRAY);
        }
        // Default color for unspecified keywords
        return defaultColor;
    }

    public void removeHighlights(JTextComponent textComp) {

        Highlighter highlighter = textComp.getHighlighter();
        Highlighter.Highlight[] hilites = highlighter.getHighlights();

        for (int i = 0; i < hilites.length; i++) {
            if (hilites[i].getPainter() instanceof HighlightText) {
                highlighter.removeHighlight(hilites[i]);
            }
        }
    }
    
    /**
     * Finds the position of a whole word in the text.
     *
     * @param text The text to search in.
     * @param word The word to find.
     * @param start The starting position for the search.
     * @return The position of the whole word, or -1 if not found.
     */
    private int findWholeWord(String text, String word, int start) {
        int pos = text.indexOf(word, start);
        while (pos >= 0) {
            if ((pos == 0 || !Character.isLetterOrDigit(text.charAt(pos - 1))) &&
                (pos + word.length() == text.length() || !Character.isLetterOrDigit(text.charAt(pos + word.length())))) {
                return pos;
            }
            pos = text.indexOf(word, pos + 1);
        }
        return -1;
    }
}
