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

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.PlainDocument;

import org.bounce.com.sun.syndication.io.XmlReader;
import org.bounce.text.LineNumberMargin;
import org.bounce.text.ScrollableEditorPanel;

/**
 * Simple wrapper around JEditorPane to browse java text using the XMLEditorKit
 * plug-in.
 * 
 * java XmlKitTest filename
 */
public class XMLKitTest {
	private static JEditorPane editor = null;

	/**
	 * Main method...
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
//		if (args.length != 1) {
//			System.err.println("need filename argument");
//			System.exit(1);
//		}

		try {
			editor = new JEditorPane();

			// Instantiate a XMLEditorKit with wrapping enabled.
			XMLEditorKit kit = new XMLEditorKit();

			editor.setEditorKit(kit);

			// Set the font style.
			editor.setFont(new Font("Courier", Font.PLAIN, 12));

			// Set the tab size
			editor.getDocument().putProperty(PlainDocument.tabSizeAttribute, new Integer(4));

			// Enable auto indentation.
			kit.setAutoIndentation(true);

			// Enable error highlighting.
			editor.getDocument().putProperty(XMLEditorKit.ERROR_HIGHLIGHTING_ATTRIBUTE, new Boolean(true));

			// Enable tag completion.
			kit.setTagCompletion(true);

			// Set a style
			kit.setStyle(XMLStyleConstants.ATTRIBUTE_NAME, new Color(255, 0, 0), Font.BOLD);

			// Put the editor in a panel that will force it to resize, when a
			// different view is choosen.
			ScrollableEditorPanel editorPanel = new ScrollableEditorPanel(editor);

			JScrollPane scroller = new JScrollPane(editorPanel);

			// Add the number margin and folding margin as a Row Header View
			JPanel rowHeader = new JPanel(new BorderLayout());
			rowHeader.add(new XMLFoldingMargin(editor), BorderLayout.EAST);
			rowHeader.add(new LineNumberMargin(editor), BorderLayout.WEST);
			scroller.setRowHeaderView(rowHeader);

			editor.read(new XmlReader(XMLKitTest.class.getResourceAsStream("/test.xml")), null);

			JFrame f = new JFrame("XmlEditorKitTest: " + "demo");
			f.getContentPane().setLayout(new BorderLayout());
			f.getContentPane().add(scroller, BorderLayout.CENTER);

			f.setSize(600, 600);
			f.setVisible(true);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
