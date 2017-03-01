/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package ids.tx.utils;

import ids.tx.bpmn.BPMN2Requirements;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/*
 * FileChooserDemo.java uses these files:
 *   images/Open16.gif
 *   images/Save16.gif
 */
public class FileChooser extends JPanel implements ActionListener 
{
    static private final String newline = "\n";
    private JButton openButton;
    private JTextArea log;
    private JFileChooser fc;
    private Boolean dbInserting;
    private Boolean goalsPrinting;
    private Boolean createWarnings;
    private String warningFilePath;

    public FileChooser(Boolean dbInserting, Boolean goalsPrinting, Boolean createWarnings, String warningFilePath) 
    {
        super(new BorderLayout());

        this.dbInserting = dbInserting;
        this.goalsPrinting = goalsPrinting;
        this.createWarnings = createWarnings;
        this.warningFilePath = warningFilePath;
        
        //Create the log first, because the action listeners
        //need to refer to it.
        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        //Create a file chooser
        fc = new JFileChooser();

        //Create the open button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        openButton = new JButton("Open a File...",createImageIcon("images/Open16.gif"));
        openButton.addActionListener(this);

        //For layout purposes, put the button in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);

        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) 
    {
        //Handle open button action.
        if (e.getSource() == openButton) 
        {
            int returnVal = fc.showOpenDialog(FileChooser.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) 
            {
                File file = fc.getSelectedFile();
                try 
                {
					BPMN2Requirements extractor = new BPMN2Requirements(file,dbInserting,goalsPrinting,createWarnings,warningFilePath);
				} 
                catch (ParserConfigurationException e1) 
                {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                catch (SAXException e1) 
                {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                catch (IOException e1) 
                {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                log.append("Opening: " + file.getName() + "." + newline);
            } 
            else 
            {
                log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        } 
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = FileChooser.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    private static void createAndShowGUI(Boolean dbInserting, Boolean goalsPrinting, Boolean createWarnings, String warningFilePath)
    {
        //Create and set up the window.
        JFrame frame = new JFrame("FileChooserDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new FileChooser(dbInserting,goalsPrinting,createWarnings,warningFilePath));

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void render(Boolean dbInserting,Boolean goalsPrinting,Boolean createWarnings,String warningFilePath)
    {
    	final Boolean dbi = dbInserting;
    	final Boolean pri = goalsPrinting;
    	final Boolean wrn = createWarnings;
    	final String path = warningFilePath;
    	SwingUtilities.invokeLater(new Runnable(){
            public void run() 
            {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE); 
                createAndShowGUI(dbi,pri,wrn,path);
            }
        });
    }
}
