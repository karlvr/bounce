/*
 * $Id: XMLTreeViewerTests.java,v 1.1 2008/04/15 20:59:50 edankert Exp $
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

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.w3c.dom.Document;

public class XMLTreeViewerTests extends TestCase {

    protected void setUp() {
        // nothing to do
    }

    protected void tearDown() {
        // nothing to do
    }
    
    public void testRootElement() throws Exception {
    	String value = "<test>text</test>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	  	DocumentBuilder builder = factory.newDocumentBuilder();
	  	Document doc = builder.parse(new ByteArrayInputStream(value.getBytes()));
	      
	  	DOMNodeTree tree = new DOMNodeTree(doc);
	  	RootTreeNode node = (RootTreeNode)tree.getModel().getRoot();
	  	assertEquals(1, node.getChildCount());
	  	assertEquals(1, ((ElementTreeNode)node.getChildAt(0)).getLines().size());
	  	assertEquals("<test>text</test>", getText(((ElementTreeNode)node.getChildAt(0)).getLines()));
    }
    
    public void testComment() throws Exception {
    	String value = "<test><!-- comment info --></test>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	  	DocumentBuilder builder = factory.newDocumentBuilder();
	  	Document doc = builder.parse(new ByteArrayInputStream(value.getBytes()));
	      
	  	DOMNodeTree tree = new DOMNodeTree(doc);
	  	RootTreeNode node = (RootTreeNode)tree.getModel().getRoot();
	  	assertEquals(1, node.getChildCount());
	  	ElementTreeNode child1 = (ElementTreeNode)node.getChildAt(0);

	  	assertEquals(1, child1.getLines().size());
	  	assertEquals(2, child1.getChildCount());
	  	assertEquals("<test>", getText(child1.getLines()));

	  	CommentTreeNode comment = (CommentTreeNode)child1.getChildAt(0);
	  	assertEquals(1, comment.getLines().size());
	  	assertEquals("<!-- comment info -->", getText(comment.getLines()));

	  	ElementEndTreeNode child2 = (ElementEndTreeNode)child1.getChildAt(1);
	  	assertEquals(1, child2.getLines().size());
	  	assertEquals(0, child2.getChildCount());
	  	assertEquals("</test>", getText(child2.getLines()));
    }

    public void testMultilineText() throws Exception {
    	String value = "<test>text \n" +
    			"multiline \n" +
    			"info</test>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	  	DocumentBuilder builder = factory.newDocumentBuilder();
	  	Document doc = builder.parse(new ByteArrayInputStream(value.getBytes()));
	      
	  	DOMNodeTree tree = new DOMNodeTree(doc);
	  	RootTreeNode node = (RootTreeNode)tree.getModel().getRoot();
	  	assertEquals(1, node.getChildCount());
	  	ElementTreeNode child1 = (ElementTreeNode)node.getChildAt(0);

	  	assertEquals(3, child1.getLines().size());
	  	assertEquals(0, child1.getChildCount());
	  	assertEquals("<test>text\nmultiline\ninfo</test>", getText(child1.getLines()));
    }

    public void testCDATAText() throws Exception {
    	String value = "<test><![CDATA[<test> & whatever]]></test>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	  	DocumentBuilder builder = factory.newDocumentBuilder();
	  	Document doc = builder.parse(new ByteArrayInputStream(value.getBytes()));
	      
	  	DOMNodeTree tree = new DOMNodeTree(doc);
	  	RootTreeNode node = (RootTreeNode)tree.getModel().getRoot();
	  	assertEquals(1, node.getChildCount());
	  	ElementTreeNode child1 = (ElementTreeNode)node.getChildAt(0);

	  	assertEquals(1, child1.getLines().size());
	  	assertEquals(0, child1.getChildCount());
	  	assertEquals("<test><test> & whatever</test>", getText(child1.getLines()));
    }

    public void testMixedText() throws Exception {
    	String value = "<test>text <b>bold</b> info</test>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	  	DocumentBuilder builder = factory.newDocumentBuilder();
	  	Document doc = builder.parse(new ByteArrayInputStream(value.getBytes()));
	      
	  	DOMNodeTree tree = new DOMNodeTree(doc);
	  	RootTreeNode node = (RootTreeNode)tree.getModel().getRoot();
	  	assertEquals(1, node.getChildCount());
	  	ElementTreeNode child1 = (ElementTreeNode)node.getChildAt(0);

	  	assertEquals(1, child1.getLines().size());
	  	assertEquals(0, child1.getChildCount());
	  	assertEquals("<test>text <b>bold</b> info</test>", getText(child1.getLines()));
    }

    public void testMixedTextWithComment() throws Exception {
    	String value = "<test>text <b>bold</b> <!-- commented out --> info</test>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	  	DocumentBuilder builder = factory.newDocumentBuilder();
	  	Document doc = builder.parse(new ByteArrayInputStream(value.getBytes()));
	      
	  	DOMNodeTree tree = new DOMNodeTree(doc);
	  	RootTreeNode node = (RootTreeNode)tree.getModel().getRoot();
	  	assertEquals(1, node.getChildCount());
	  	ElementTreeNode child1 = (ElementTreeNode)node.getChildAt(0);

	  	assertEquals(1, child1.getLines().size());
	  	assertEquals(0, child1.getChildCount());
	  	assertEquals("<test>text <b>bold</b> <!-- commented out --> info</test>", getText(child1.getLines()));
    }

    public void testChildInline() throws Exception {
    	String value = "<test><child>child text</child></test>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	  	DocumentBuilder builder = factory.newDocumentBuilder();
	  	Document doc = builder.parse(new ByteArrayInputStream(value.getBytes()));
	      
	  	DOMNodeTree tree = new DOMNodeTree(doc);
	  	RootTreeNode node = (RootTreeNode)tree.getModel().getRoot();
	  	assertEquals(1, node.getChildCount());
	  	ElementTreeNode child1 = (ElementTreeNode)node.getChildAt(0);

	  	assertEquals(1, child1.getLines().size());
	  	assertEquals(2, child1.getChildCount());
	  	ElementTreeNode child2 = (ElementTreeNode)child1.getChildAt(0);
	  	assertEquals("<child>child text</child>", getText(child2.getLines()));
	  	assertEquals("</test>", getText(((ElementEndTreeNode)child1.getChildAt(1)).getLines()));
    }

    public void testChildAndCommentInline() throws Exception {
    	String value = "<test><child>child text</child><!-- comment --></test>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	  	DocumentBuilder builder = factory.newDocumentBuilder();
	  	Document doc = builder.parse(new ByteArrayInputStream(value.getBytes()));
	      
	  	DOMNodeTree tree = new DOMNodeTree(doc);
	  	RootTreeNode node = (RootTreeNode)tree.getModel().getRoot();
	  	assertEquals(1, node.getChildCount());
	  	ElementTreeNode child1 = (ElementTreeNode)node.getChildAt(0);

	  	assertEquals(1, child1.getLines().size());
	  	assertEquals(3, child1.getChildCount());
	  	assertEquals("<child>child text</child>", getText(((ElementTreeNode)child1.getChildAt(0)).getLines()));
	  	assertEquals("<!-- comment -->", getText(((CommentTreeNode)child1.getChildAt(1)).getLines()));
	  	assertEquals("</test>", getText(((ElementEndTreeNode)child1.getChildAt(2)).getLines()));
    }

    public void testChildCommentWhitespaceInline() throws Exception {
    	String value = "<test>  <child>child text</child>  \n \t <!-- comment -->  </test>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	  	DocumentBuilder builder = factory.newDocumentBuilder();
	  	Document doc = builder.parse(new ByteArrayInputStream(value.getBytes()));
	      
	  	DOMNodeTree tree = new DOMNodeTree(doc);
	  	RootTreeNode node = (RootTreeNode)tree.getModel().getRoot();
	  	assertEquals(1, node.getChildCount());
	  	ElementTreeNode child1 = (ElementTreeNode)node.getChildAt(0);

	  	assertEquals(1, child1.getLines().size());
	  	assertEquals(3, child1.getChildCount());
	  	assertEquals("<child>child text</child>", getText(((ElementTreeNode)child1.getChildAt(0)).getLines()));
	  	assertEquals("<!-- comment -->", getText(((CommentTreeNode)child1.getChildAt(1)).getLines()));
	  	assertEquals("</test>", getText(((ElementEndTreeNode)child1.getChildAt(2)).getLines()));
    }

    private static final String getText(List<Line> lines) {
    	StringBuilder builder = new StringBuilder();
    	for (Line line : lines) {
    		if (builder.length() > 0) {
        		builder.append('\n');
    		}

    		builder.append(line.getText().trim());
    	}
    	
    	return builder.toString();
    }
}
