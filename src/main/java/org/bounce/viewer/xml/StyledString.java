/*
 * $Id: StyledString.java,v 1.1 2008/04/15 20:59:50 edankert Exp $
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

import java.awt.Color;

import javax.swing.UIManager;

/**
 * Holds information for a styled string.
 *
 * @author Edwin Dankert (edankert@gmail.com)
 */
public class StyledString {
    public static final String CONTROL_COLOR_ID = "org.bounce.viewer.xml.control-color";
    public static final String COMMENT_COLOR_ID = "org.bounce.viewer.xml.comment-color";
    public static final String ELEMENT_NAME_COLOR_ID = "org.bounce.viewer.xml.element-name-color";
    public static final String NAMESPACE_PREFIX_COLOR_ID = "org.bounce.viewer.xml.namespace-prefix-color";
    public static final String NAMESPACE_NAME_COLOR_ID = "org.bounce.viewer.xml.namespace-name-color";
    public static final String NAMESPACE_URI_COLOR_ID = "org.bounce.viewer.xml.namespace-uri-color";
    public static final String ATTRIBUTE_NAME_COLOR_ID = "org.bounce.viewer.xml.attribute-name-color";
    public static final String ATTRIBUTE_VALUE_COLOR_ID = "org.bounce.viewer.xml.attribute-value-color";
    public static final String ELEMENT_VALUE_COLOR_ID = "org.bounce.viewer.xml.element-value-color";

    private static final Color BRACKET_COLOR = new Color(102, 102, 102);
    private static final Color COMMENT_COLOR = new Color(153, 153, 153);
    private static final Color ELEMENT_PREFIX_COLOR = new Color(0, 102, 102);
    private static final Color ELEMENT_NAME_COLOR = new Color(0, 51, 102);
    private static final Color NAMESPACE_PREFIX_COLOR = ELEMENT_PREFIX_COLOR;
    private static final Color NAMESPACE_NAME_COLOR = new Color(102, 102, 102);
    private static final Color NAMESPACE_URI_COLOR = new Color(0, 51, 51);
    private static final Color ATTRIBUTE_PREFIX_COLOR = ELEMENT_PREFIX_COLOR;
    private static final Color ATTRIBUTE_NAME_COLOR = new Color(153, 51, 51);
    private static final Color ATTRIBUTE_VALUE_COLOR = new Color(102, 0, 0);
    private static final Color ELEMENT_VALUE_COLOR = Color.black;
	
    static final StyledString COMMENT_START = new StyledString("<!--");
	static final StyledString COMMENT_END = new StyledString( "-->");
	static final StyledString SPACE = new StyledString(" ");
	static final StyledString TAB = new StyledString("  ");
	static final StyledString SLASH = new StyledString("/");
	static final StyledString ATTRIBUTE_ASIGN = new StyledString("=");
	static final StyledString ATTRIBUTE_COLON = new StyledString(":");
	static final StyledString NAMESPACE_ASIGN = new StyledString("=");	protected static final StyledString NAMESPACE_COLON = new StyledString(":");
	static final StyledString NAMESPACE_NAME = new StyledString("xmlns", NAMESPACE_NAME_COLOR_ID, NAMESPACE_NAME_COLOR);
	static final StyledString ELEMENT_COLON = new StyledString(":");

	static final StyledString OPEN_BRACKET = new StyledString("<");
	static final StyledString CLOSE_BRACKET = new StyledString(">");

	static class CommentText extends StyledString {
		public CommentText(String text) {
			super(text, COMMENT_COLOR_ID, COMMENT_COLOR);
		}
	}

	static class ElementValue extends StyledString {
		public ElementValue(String text) {
			super(text, ELEMENT_VALUE_COLOR_ID, ELEMENT_VALUE_COLOR);
		}
	}

	static class AttributeValue extends StyledString {
		public AttributeValue(String text) {
			super("\""+text+"\"", ATTRIBUTE_VALUE_COLOR_ID, ATTRIBUTE_VALUE_COLOR);
		}
	}

	static class AttributePrefix extends StyledString {
		public AttributePrefix(String text) {
			super(text, NAMESPACE_PREFIX_COLOR_ID, ATTRIBUTE_PREFIX_COLOR);
		}
	}

	static class AttributeName extends StyledString {
		public AttributeName(String text) {
			super(text, ATTRIBUTE_NAME_COLOR_ID, ATTRIBUTE_NAME_COLOR);
		}
	}

	static class NamespaceURI extends StyledString {
		public NamespaceURI(String text) {
			super("\""+text+"\"", NAMESPACE_URI_COLOR_ID, NAMESPACE_URI_COLOR);
		}
	}

	static class NamespacePrefix extends StyledString {
		public NamespacePrefix(String text) {
			super(text, NAMESPACE_PREFIX_COLOR_ID, NAMESPACE_PREFIX_COLOR);
		}
	}

	static class ElementName extends StyledString {
		public ElementName(String text) {
			super(text, ELEMENT_NAME_COLOR_ID, ELEMENT_NAME_COLOR);
		}
	}

	static class ElementPrefix extends StyledString {
		public ElementPrefix(String text) {
			super(text, NAMESPACE_PREFIX_COLOR_ID, ELEMENT_PREFIX_COLOR);
		}
	}

    private String text = null;
    private String colorID = null;

    StyledString(String text) {
        this(text, CONTROL_COLOR_ID, BRACKET_COLOR);
    }

    StyledString(String text, String colorID, Color color) {
        this.text = text;
        this.colorID = colorID;
        UIManager.put(colorID, color);
    }
    
    String getText() {
        return text;
    }

    Color getColor() {
        return UIManager.getColor(colorID);
    }
}
