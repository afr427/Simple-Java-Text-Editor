package simplejavatexteditor;

import javax.swing.*;
import javax.swing.text.BadLocationException;

import java.awt.*;

/**
 * 
 *  @author Alex Frankel
 *  @class LineNumbering
 *  @extends JComponent <br>
 *  Purpose: Displays line numbers to the text editor, along with the word and character count (length) 
 *  <br>
 *  		 
 *  
 * The LineNumbering class is a custom JComponent that provides line numbering for a JTextArea.
 * It displays line numbers along with the character count for each line.
 */

public class LineNumbering extends JComponent {
	
    private JTextArea textArea;
    
    /**
     * Constructs a LineNumbering component for the given JTextArea.
     *
     * @param textArea The JTextArea to which this line numbering component is attached.
     */
    public LineNumbering(JTextArea textArea) {
        this.textArea = textArea;
        // Add a caret listener to repaint the component when the caret position changes
        textArea.addCaretListener(e -> repaint());
        // Add a document listener to repaint the component when the document changes
        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { repaint(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { repaint(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { repaint(); }
        });
        
    }
    
    /**
     * Paints the line numbers and character counts on the component.
     *
     * @param g The Graphics object used for painting.
     */
    @Override
    protected void paintComponent(Graphics g) {
    	
        super.paintComponent(g);
        int lineHeight = textArea.getFontMetrics(textArea.getFont()).getHeight();
        int startLine = 0;
        int endLine = 0;

        try {
        	
            startLine = textArea.getLineOfOffset(0);
            endLine = textArea.getLineOfOffset(textArea.getDocument().getLength());
            
        } catch (BadLocationException ex) {
        	
            ex.printStackTrace();
            
        }
        // Iterate over each line and draw the line number and character count
        for (int i = startLine; i <= endLine; i++) {
        	
            try {
            	
                String lineNumber = String.valueOf(i + 1);
                String lineText = textArea.getLineStartOffset(i) < textArea.getDocument().getLength()
                        ? textArea.getDocument().getText(textArea.getLineStartOffset(i), 
                        textArea.getLineEndOffset(i) - textArea.getLineStartOffset(i)).trim()
                        : "";
                String charCount = String.valueOf(lineText.length());
                g.drawString(lineNumber + " (" + charCount + ")", 0, (i + 1) * lineHeight);
                
            } catch (BadLocationException ex) {
            	
                ex.printStackTrace();
                
            }
            
        }
        
    }
    
    /**
     * Returns the preferred size of this component.
     *
     * @return The preferred size as a Dimension object.
     */
    @Override
    public Dimension getPreferredSize() {
    	
        return new Dimension(50, textArea.getHeight());
        
    }
    
}



