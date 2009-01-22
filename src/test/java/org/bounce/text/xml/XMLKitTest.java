/*
 * $Id$
 *
 * Copyright (c) 2002 - 2008, Edwin Dankert
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, 
 *	 this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright 
 * 	 notice, this list of conditions and the following disclaimer in the 
 *	 documentation and/or other materials provided with the distribution. 
 * * Neither the name of 'Edwin Dankert' nor the names of its contributors 
 *	 may  be used to endorse or promote products derived from this software 
 *	 without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR 
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.bounce.text.xml;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.PlainDocument;

import org.bounce.text.LineNumberMargin;
import org.bounce.text.ScrollableEditorPanel;

/**
 * Simple wrapper around JEditorPane to browse java text using the XMLEditorKit
 * plug-in.
 */
public class XMLKitTest {
    private static JEditorPane editor = null;

    /**
     * Main method...
     * 
     * @param args
     */
    public static void main( String[] args) {
        try {
            editor = new JEditorPane();
            
            // Instantiate a XMLEditorKit with wrapping enabled.
            XMLEditorKit kit = new XMLEditorKit(true);

            // Set the wrapping style.
            kit.setWrapStyleWord(true);
            kit.setFolding(true);
            
            editor.setEditorKit(kit);

            editor.read(XMLKitTest.class.getResourceAsStream("/test.xml"), null);

            // Set the font style.
            editor.setFont(new Font("Courier", Font.PLAIN, 12));

            // Set the tab size
            editor.getDocument().putProperty(PlainDocument.tabSizeAttribute, 
                                             new Integer(4));

            // Enable auto indentation.
            editor.getDocument().putProperty(XMLDocument.AUTO_INDENTATION_ATTRIBUTE, 
                                             new Boolean(true));

            // Enable tag completion.
            editor.getDocument().putProperty(XMLDocument.TAG_COMPLETION_ATTRIBUTE, 
                                             new Boolean(true));
            
            // Set a style
            kit.setStyle( XMLStyleConstants.ATTRIBUTE_NAME, new Color( 255, 0, 0), 
                          Font.BOLD);
            
            // Put the editor in a panel that will force it to resize, when a different 
            // view is choosen.
            ScrollableEditorPanel editorPanel = new ScrollableEditorPanel( editor);

            JScrollPane scroller = new JScrollPane(editorPanel);

            // Add the number margin as a Row Header View
            scroller.setRowHeaderView(new LineNumberMargin(editor));
            
            JFrame f = new JFrame( "XmlEditorKitTest: " + args[0]);
            f.getContentPane().setLayout( new BorderLayout());
            f.getContentPane().add( scroller, BorderLayout.CENTER);
            
            JButton button = new JButton( "Toggle Wrapping");
            button.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e) {
                    XMLEditorKit kit = (XMLEditorKit)editor.getEditorKit();
                    kit.setLineWrappingEnabled( !kit.isLineWrapping());
                    
                    // Update the UI and create a new view...
                    editor.updateUI();
                }
            });
            f.getContentPane().add( button, BorderLayout.SOUTH);
            f.setSize( 600, 600);
            f.setVisible( true);
        } catch ( Throwable e) {
            e.printStackTrace();
            System.exit( 1);
        }
    }
}