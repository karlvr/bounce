/*
 * $Id: XMLTreeViewerTest.java,v 1.1 2008/04/15 20:59:50 edankert Exp $
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

package org.bounce.viewer.xml;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

/**
 * Simple wrapper around JEditorPane to browse java text using the XMLEditorKit
 * plug-in.
 */
public class XMLTreeViewerTest {
    private static DOMNodeTree tree = null;

    /**
     * Main method...
     * 
     * @param args
     */
    public static void main( String[] args) {
        if ( args.length != 1) {
            System.err.println( "need filename argument");
            System.exit( 1);
        }

        try {
            File file = new File( args[0]);
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder builder = factory.newDocumentBuilder();
        	Document doc = builder.parse(file);
            
        	tree = new DOMNodeTree(doc);
        	TreePath path = tree.getPathForNode(doc.getDocumentElement().getElementsByTagName("child").item(0).getFirstChild());
        	tree.expandPath(path);
        	tree.setSelectionPath(path);
            
            JScrollPane scroller = new JScrollPane(tree);

            JFrame f = new JFrame( "XmlViewerTest: " + args[0]);
            f.getContentPane().setLayout( new BorderLayout());
            f.getContentPane().add( scroller, BorderLayout.CENTER);
            
            f.setSize( 600, 600);
            f.setVisible( true);
        } catch ( Throwable e) {
            e.printStackTrace();
            System.exit( 1);
        }
    }
}