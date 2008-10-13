/*
 * $Id: ElementTreeNode.java,v 1.1 2008/04/15 20:59:50 edankert Exp $
 *
 * Copyright (c) 2002 - 2008, Edwin Dankert
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, 
 *   this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright 
 *   notice, this list of conditions and the following disclaimer in the 
 *   documentation and/or other materials provided with the distribution. 
 * * Neither the name of 'Edwin Dankert' nor the names of its contributors 
 *   may  be used to endorse or promote products derived from this software 
 *   without specific prior written permission. 
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


import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The node for the XML tree, containing an XML element.
 *
 * @author Edwin Dankert <edankert@gmail.com>
 */
class ElementTreeNode extends NodeTreeNode {
	private static final long serialVersionUID = 2859132085591886595L;
	
	/**
	 * Constructs the node for the XML element.
	 *
	 * @param element the XML element.
	 */	
	ElementTreeNode(Element element) {
        super(element);
		
		if (!ScannerUtils.isMixed(element)) {
            boolean hasChildElements = false;
            NodeList nodes = element.getChildNodes();
			for ( int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				
				if (node instanceof Element) {
                    hasChildElements = true;
					add(new ElementTreeNode((Element)node));
				} else if ((node instanceof Comment) && ScannerUtils.showComments()) {
                    hasChildElements = true;
					add(new CommentTreeNode((Comment)node));
				}
			}
			
			// create an end node...	
			if (hasChildElements) {
				add(new ElementEndTreeNode(element));
			}
		} 

		format();
	}
	
	void format() {
		setCurrent(new Line());
		
		ScannerUtils.scanElement(getLines(), getCurrent(), (Element)getNode());
	}
} 
