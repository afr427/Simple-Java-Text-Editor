package simplejavatexteditor;

import javax.swing.text.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class HighlightText extends DefaultHighlighter.DefaultHighlightPainter{
	private Map<String, Color> categoryColors = new HashMap<>();
	private Color defaultColor;

    public HighlightText(Color defaultColor) {
        super(defaultColor);
        this.defaultColor = defaultColor;
    }
    
    public void setCategoryColor(String category, Color color) {
        categoryColors.put(category, color);
    }

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
                      highlighter.addHighlight(pos, pos + keyword.length(), new DefaultHighlighter.DefaultHighlightPainter(color));
                      pos += keyword.length();
                    }               
                }

            }
        } catch (BadLocationException e) {}

    }
    
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
