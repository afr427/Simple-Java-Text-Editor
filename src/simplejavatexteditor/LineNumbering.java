package simplejavatexteditor;

import javax.swing.*;
import javax.swing.text.BadLocationException;

import java.awt.*;

/**
 * 
 *  @author Alex Frankel
 *  @class LineNumbering
 *  @extends JComponent <br>
 *  Purpose: Displays line numbers to the text editor
 *  <br>
 *  		 
 *  
*/

//public class LineNumbering extends JComponent {
//	
//    private JTextArea textArea;
//
//    public LineNumbering(JTextArea textArea) {
//    	
//        this.textArea = textArea;
//        textArea.addCaretListener(e -> repaint());
//        
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//    	
//        super.paintComponent(g);
//        int lineHeight = textArea.getFontMetrics(textArea.getFont()).getHeight();
//        int startLine = 0;
//        int endLine = 0;
//        
//        try {
//        	
//            startLine = textArea.getLineOfOffset(0);
//            endLine = textArea.getLineOfOffset(textArea.getDocument().getLength());
//
//            for (int i = startLine; i <= endLine; i++) {
//            	
//                g.drawString(String.valueOf(i + 1), 0, (i + 1) * lineHeight);
//                
//            }
//            
//        } catch (BadLocationException ex) {
//        	
//            ex.printStackTrace();
//            
//        }
//
//        for (int i = startLine; i <= endLine; i++) {
//        	
//            g.drawString(String.valueOf(i + 1), 0, (i + 1) * lineHeight);
//            
//        }
//        
//    }
//
//    @Override
//    public Dimension getPreferredSize() {
//    	
//        return new Dimension(30, textArea.getHeight());
//        
//    }
//    
//}

public class LineNumbering extends JComponent {
    private JTextArea textArea;

    public LineNumbering(JTextArea textArea) {
        this.textArea = textArea;
        textArea.addCaretListener(e -> repaint());
        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { repaint(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { repaint(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { repaint(); }
        });
    }

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

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(50, textArea.getHeight());
    }
}