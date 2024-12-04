/**
 * @name        Simple Java NotePad
 * @package     ph.notepad
 * @file        UI.java
 * @author      SORIA Pierre-Henry
 * @email       pierrehs@hotmail.com
 * @link        http://github.com/pH-7
 * @copyright   Copyright Pierre-Henry SORIA, All Rights Reserved.
 * @license     Apache (http://www.apache.org/licenses/LICENSE-2.0)
 * @create      2012-05-04
 * @update      2015-09-4
 *
 *
 * @modifiedby  Achintha Gunasekara
 * @modweb      http://www.achinthagunasekara.com
 * @modemail    contact@achinthagunasekara.com
 *
 * @Modifiedby SidaDan
 * @modemail Fschultz@sinf.de
 * Center this JFrame to the JTextArea
 * Bug fixed. If JTextArea txt not empty and the user will
 * shutdown the Simple Java NotePad, then the Simple Java NotePad
 * is only hidden (still running). We need DISPOSE_ON_CLOSE for
 * this JFrame.
 * Tested with java 8.
 *
 * @Modifiedby SidaDan
 * @modemail Fschultz@sinf.de
 * Removed unsused imports
 */

package simplejavatexteditor;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Find class represents a dialog window for finding and replacing text in a JTextArea.
 * It extends JFrame and implements ActionListener to handle button events.
 */
public class Find extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    int startIndex=0;
    int select_start=-1;
    JLabel lab1, lab2;
    JTextField textF, textR;
    JButton findBtn, findNext, replace, replaceAll, cancel;
    private JTextArea txt;
    private JCheckBox regexCheckBox;

    public Find(JTextArea text) {
        this.txt = text;

        lab1 = new JLabel("Find:");
        lab2 = new JLabel("Replace:");
        textF = new JTextField(30);
        textR = new JTextField(30);
        findBtn = new JButton("Find");
        findNext = new JButton("Find Next");
        replace = new JButton("Replace");
        replaceAll = new JButton("Replace All");
        cancel = new JButton("Cancel");
        
        regexCheckBox = new JCheckBox("Use Regular Expressions");

        // Set Layout NULL
        setLayout(null);

        // Set the width and height of the label
        int labWidth = 80;
        int labHeight = 20;

        // Adding labels
        lab1.setBounds(10,10, labWidth, labHeight);
        add(lab1);
        textF.setBounds(10+labWidth, 10, 120, 20);
        add(textF);
        lab2.setBounds(10, 10+labHeight+10, labWidth, labHeight);
        add(lab2);
        textR.setBounds(10+labWidth, 10+labHeight+10, 120, 20);
        add(textR);

        // Adding buttons
        findBtn.setBounds(225, 6, 115, 20);
        add(findBtn);
        findBtn.addActionListener(this);

        findNext.setBounds(225, 28, 115, 20);
        add(findNext);
        findNext.addActionListener(this);

        replace.setBounds(225, 50, 115, 20);
        add(replace);
        replace.addActionListener(this);

        replaceAll.setBounds(225, 72, 115, 20);
        add(replaceAll);
        replaceAll.addActionListener(this);

        cancel.setBounds(225, 94, 115, 20);
        add(cancel);
        cancel.addActionListener(this);
        
        regexCheckBox.setBounds(20, 80, 200, labHeight);
        
        add(regexCheckBox);

        // Set the width and height of the window
        int width = 360;
        int height = 160;

        // Set size window
        setSize(width,height);

        // center the frame on the frame
        setLocationRelativeTo(txt);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    /**
     * Searches for the text entered in the search field within the main text area.
     * If regex is selected, it uses regular expression matching.
     */
    public void find() {
    	
        String searchText = textF.getText();
        
        if (regexCheckBox.isSelected()) {
        	
            try {
            	
                Pattern pattern = Pattern.compile(searchText);
                Matcher matcher = pattern.matcher(txt.getText());

                if (matcher.find(startIndex)) {
                	
                    select_start = matcher.start();
                    int select_end = matcher.end();
                    txt.select(select_start, select_end);
                    startIndex = select_end;
                    
                } else {
                	
                    startIndex = 0;
                    JOptionPane.showMessageDialog(null, "Could not find \"" + searchText + "\"!");
                    
                }
                
            } catch (Exception e) {
            	
                JOptionPane.showMessageDialog(null, "Invalid regular expression: " + e.getMessage());
                
            }
            return; 
        }
    	
        select_start = txt.getText().indexOf(textF.getText().toLowerCase());
        if(select_start == -1)
        {
            startIndex = 0;
            JOptionPane.showMessageDialog(null, "Could not find \"" + textF.getText() + "\"!");
            return;
        }
        if(select_start == txt.getText().lastIndexOf(textF.getText().toLowerCase()))
        {
            startIndex = 0;
        }
        int select_end = select_start + textF.getText().length();
        txt.select(select_start, select_end);
    }
    
    /**
     * Finds the next occurrence of the search text or the selected text.
     * If regex is selected, it uses regular expression matching.
     */
    public void findNext() {
        String selection = txt.getSelectedText();
        try
        {
            selection.equals("");
        }
        catch(NullPointerException e)
        {
            selection = textF.getText();
            try
            {
                selection.equals("");
            }
            catch(NullPointerException e2)
            {
                selection = JOptionPane.showInputDialog("Find:");
                textF.setText(selection);
            }
        }
        
        if (regexCheckBox.isSelected()) {
        	
            try {
            	
                Pattern pattern = Pattern.compile(selection);
                Matcher matcher = pattern.matcher(txt.getText());

                if (matcher.find(startIndex)) {
                	
                    select_start = matcher.start();
                    int select_end = matcher.end();
                    txt.select(select_start, select_end);
                    startIndex = select_end;
                    
                } else {
                	
                    startIndex = 0;
                    JOptionPane.showMessageDialog(null,"Could not find \"" + selection + "\"!");
                    
                }
                
            } catch (Exception e) {
            	
                JOptionPane.showMessageDialog(null,"Invalid regular expression: " + e.getMessage());
                
            }
            
            return;
            
        }
        
        int select_start = txt.getText() .indexOf(selection , startIndex);
        int select_end = select_start+selection.length();
        txt.select(select_start, select_end);
        startIndex = select_end+1;
        
//        try
//        {

//            if(select_start == txt.getText().lastIndexOf(selection.toLowerCase()))
//            {
//                startIndex = 0;
//            }
//        }
//        catch(NullPointerException e)
//        {}
    }
    
    /**
     * Replaces the selected text with the text in the replace field.
     */

    public void replace() {
        try
        {
            find();
            if (select_start != -1)
            txt.replaceSelection(textR.getText());
        }
        catch(NullPointerException e)
        {
            System.out.print("Null Pointer Exception: "+e);
        }
    }
    
    /**
     * Replaces all occurrences of the search text with the replace text.
     * If regex is selected, it uses regular expression replacement.
     */
    public void replaceAll() {
    	
        String searchText = textF.getText();
        
        if (regexCheckBox.isSelected()) {
        	
            String replacedText = txt.getText().replaceAll(searchText , textR.getText());
            txt.setText(replacedText); 
            
        } else {
        	
            txt.setText(txt.getText().replaceAll(searchText , textR.getText()));
            
        }
        
    }
    
    /**
     * Handles button click events for the find, replace, and cancel operations.
     *
     * @param e The action event triggered by button clicks.
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == findBtn)
        {
           find();
        }
        else if(e.getSource() == findNext)
        {
           findNext();
        }
        else if(e.getSource() == replace)
        {
            replace();
        }
        else if(e.getSource() == replaceAll)
        {
           replaceAll();
        }
        else if(e.getSource() == cancel)
        {
           this.setVisible(false);
        }
   }

}