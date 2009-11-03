/*
 * $Id: ScannerUtils.java,v 1.2 2008/05/20 20:19:20 edankert Exp $
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

import java.util.List;

import org.bounce.xml.DOMUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class ScannerUtils {
	private static final int MAX_LINE_LENGTH = 80;

	public static final String SHOW_ATTRIBUTES = "org.bounce.viewer.xml.showAttributes";
	public static final String SHOW_NAMESPACES = "org.bounce.viewer.xml.showNamespaces";
	public static final String SHOW_VALUES = "org.bounce.viewer.xml.showValues";
	public static final String SHOW_COMMENTS = "org.bounce.viewer.xml.showComments";

	static Line scanComment(List<Line> lines, Line current, Comment comment) {
		StyledElement styledElement = new StyledElement();
		styledElement.addString(StyledString.COMMENT_START);

		current.addStyledElement(styledElement);
		current = ScannerUtils.scanContent(lines, current, comment);

		styledElement = new StyledElement();
		styledElement.addString(StyledString.COMMENT_END);
		current.addStyledElement(styledElement);

		return current;
	}

	// Create a styled version of the end-tag.
	static Line scanEndTag(List<Line> lines, Line current, Element elem) {
		StyledElement styledEnd = new StyledElement();
		styledEnd.addString(StyledString.OPEN_BRACKET);
		styledEnd.addString(StyledString.SLASH);

		if (showNamespaces()) {
			String prefix = elem.getPrefix();

			if (prefix != null && prefix.length() > 0) {
				styledEnd.addString(new StyledString.ElementPrefix(prefix));
				styledEnd.addString(StyledString.ELEMENT_COLON);
			}
		}

		styledEnd.addString(new StyledString.ElementName(DOMUtils.getName(elem)));
		styledEnd.addString(StyledString.CLOSE_BRACKET);
		current.addStyledElement(styledEnd);

		return current;
	}

	static Line scanElement(List<Line> lines, Line current, Element elem) {

		if (DOMUtils.isMixed(elem)) {
			current = ScannerUtils.scanMixedElement(lines, current, elem);

			if (showValues()) {
				current = ScannerUtils.scanEndTag(lines, current, elem);
			}
		} else {
			current = ScannerUtils.scanStartTag(lines, current, elem);

			if (showValues()) {
				if (DOMUtils.hasContent(elem)) {
					current = ScannerUtils.scanContent(lines, current, elem);

					if (DOMUtils.isTextOnly(elem)) {
						current = ScannerUtils.scanEndTag(lines, current, elem);
					}
				}
			}
		}

		return current;
	}

	// Elements parsed here can be both mixed and normal but then contained in a
	// mixed element...
	private static Line scanMixedElement(List<Line> lines, Line current, Element elem) {

		current = ScannerUtils.scanStartTag(lines, current, elem);

		if (showValues()) {
			NodeList nodes = elem.getChildNodes();

			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);

				if (node instanceof Element) {
					current = scanMixedElement(lines, current, (Element)node);
					current = ScannerUtils.scanEndTag(lines, current, (Element)node);
				} else if (node instanceof Text) {
					current = scanContent(lines, current, node);
				} else if (node instanceof Comment && showComments()) {
					current = scanComment(lines, current, (Comment)node);
				}
			}
		}

		return current;
	}

	// Create a styled version of the start-tag.
	private static Line scanStartTag(List<Line> lines, Line current, Element elem) {
		StyledElement styledElement = new StyledElement();
		styledElement.addString(StyledString.OPEN_BRACKET);

		if (showNamespaces()) {
			String prefix = elem.getPrefix();

			if (prefix != null && prefix.length() > 0) {
				styledElement.addString(new StyledString.ElementPrefix(prefix));
				styledElement.addString(StyledString.ELEMENT_COLON);
			}
		}

		styledElement.addString(new StyledString.ElementName(DOMUtils.getName(elem)));
		current.addStyledElement(styledElement);

		NamedNodeMap attributes = elem.getAttributes();

		for (int i = 0; i < attributes.getLength(); i++) {
			Attr attribute = (Attr)attributes.item(i);
			String prefix = attribute.getPrefix();
			StyledElement sa = null;

			if (prefix != null && prefix.length() > 0 && "xmlns".equals(prefix)) {
				sa = ScannerUtils.formatNamespace(attribute);
			} else if ((prefix == null || prefix.length() == 0) && "xmlns".equals(DOMUtils.getName(attribute)) && elem.getNamespaceURI() != null) {
				sa = ScannerUtils.formatNamespace(attribute);
			} else {
				sa = ScannerUtils.formatAttribute(attribute);
			}

			if (sa != null) {
				if (current.length() + sa.length() + 1 > MAX_LINE_LENGTH) {
					current = new Line();
					lines.add(current);
					current.addStyledString(StyledString.TAB);
				} else {
					current.addStyledString(StyledString.SPACE);
				}

				current.addStyledElement(sa);
			}
		}

		if (!DOMUtils.hasContent(elem)) {
			current.addStyledString(StyledString.SLASH);
		} else if (DOMUtils.isTextOnly(elem) && !showValues()) {
			current.addStyledString(StyledString.SLASH);
		}

		current.addStyledString(StyledString.CLOSE_BRACKET);

		return current;
	}

	// Create a styled version of the element content.
	private static Line scanContent(List<Line> lines, Line current, Node node) {
        String text = null;
        
        if (node instanceof CharacterData) {
            text = ((CharacterData)node).getData();
        } else if (node instanceof Element) {
            text = DOMUtils.getText((Element)node);
        }

		if ((current.length() + 1 >= MAX_LINE_LENGTH) && (text.length() > 0)) {
			current = new Line();
			lines.add(current);
			current.addStyledString(StyledString.TAB);
		}

		if (text.length() > 0) {
			boolean parsed = false;

			while (!parsed) {
				int length = MAX_LINE_LENGTH - (current.length() + 1);

				if (length > text.length()) {
                    int index = text.indexOf("\n");

                    if (index == -1) {
                        index = text.indexOf("\r");
                    }

                    if (index == -1) {
                        index = text.length();
                    }

					if (index != 0) {
                        if (node instanceof Element || node instanceof Text) {
                            current.addStyledString(new StyledString.ElementValue(text.substring(0, index)));
                        } else if (node instanceof Comment) {
                            current.addStyledString(new StyledString.CommentText(text.substring(0, index)));
                        }
					}

					if (index == text.length()) {
						parsed = true;
					} else {
						text = text.substring(index + 1, text.length());
					}
				} else {
                    String sub = text.substring(0, length);
                    int index = sub.indexOf("\n");

                    if (index == -1) {
                        index = sub.indexOf("\r");
                    }

                    if (index == -1) {
                        index = sub.lastIndexOf(" ");
                    }

					if (index > 0) {
                        if (node instanceof Element || node instanceof Text) {
                            current.addStyledString(new StyledString.ElementValue(sub.substring(0, index)));
                        } else if (node instanceof Comment) {
                            current.addStyledString(new StyledString.CommentText(sub.substring(0, index)));
                        }

						text = text.substring(index + 1, text.length());
					} else { // Text is too long without any whitespaces...
						int nlindex = text.indexOf("\n");
						int rindex = text.indexOf("\r");
						int spindex = sub.indexOf(" ");

						if (nlindex == -1) {
							nlindex = Integer.MAX_VALUE;
						}
						if (rindex == -1) {
							rindex = Integer.MAX_VALUE;
						}
						if (spindex == -1) {
							spindex = Integer.MAX_VALUE;
						}

						index = Math.min(nlindex, rindex);
						index = Math.min(index, spindex);
						index = Math.min(index, text.length());

                        if (node instanceof Element || node instanceof Text) {
                            current.addStyledString(new StyledString.ElementValue(text.substring(0, index)));
                        } else if (node instanceof Comment) {
                            current.addStyledString(new StyledString.CommentText(text.substring(0, index)));
                        }

						if (index == text.length()) {
							parsed = true;
						} else {
							text = text.substring(index + 1, text.length());
						}
					}
				}

				if (!parsed) {
					current = new Line();
					lines.add(current);
					current.addStyledString(StyledString.TAB);
				}
			}
		}

		return current;
	}

	private static StyledElement formatAttribute(Attr a) {
		StyledElement styledAttribute = null;
		
		if (showAttributes()) {
			styledAttribute = new StyledElement();
			String prefix = a.getPrefix();
	
			if (showNamespaces()) {
				if (prefix != null && prefix.length() > 0) {
					styledAttribute.addString(new StyledString.AttributePrefix(prefix));
					styledAttribute.addString(StyledString.ATTRIBUTE_COLON);
				}
			}
	
			styledAttribute.addString(new StyledString.AttributeName(DOMUtils.getName(a)));
			styledAttribute.addString(StyledString.ATTRIBUTE_ASIGN);
			styledAttribute.addString(new StyledString.AttributeValue(a.getValue()));
		}

		return styledAttribute;
	}

	private static StyledElement formatNamespace(Attr a) {
		StyledElement styledNamespace = null;

		if (showNamespaces()) {
			styledNamespace = new StyledElement();
			String prefix = a.getPrefix();
	
			styledNamespace.addString(StyledString.NAMESPACE_NAME);

			if (prefix != null && prefix.length() > 0) {
				styledNamespace.addString(StyledString.NAMESPACE_COLON);
				styledNamespace.addString(new StyledString.NamespacePrefix(DOMUtils.getName(a)));
			}
	
			styledNamespace.addString(StyledString.NAMESPACE_ASIGN);
			styledNamespace.addString(new StyledString.NamespaceURI(a.getValue()));
		}
		
		return styledNamespace;
	}

//	private static boolean isWhiteSpace(Text node) {
//		return node.getData().trim().length() == 0;
//	}
//
//	// solves a problem in the Element that hasMixedContent returns true when
//	// the content has comment information.
//	public static boolean isMixed(Element element) {
//		boolean elementFound = false;
//		boolean textFound = false;
//
//		NodeList nodes = element.getChildNodes();
//
//		for (int i = 0; i < nodes.getLength(); i++) {
//			Node node = nodes.item(i);
//
//			if (node instanceof Element) {
//				elementFound = true;
//			} else if (node instanceof Text) {
//				if (!isWhiteSpace((Text) node)) {
//					textFound = true;
//				}
//			}
//
//			if (textFound && elementFound) {
//				return true;
//			}
//		}
//
//		return false;
//	}
//
//	public static String getName(Attr attribute) {
//		if (attribute.getLocalName() == null) {
//			return attribute.getName();
//		}
//
//		return attribute.getLocalName();
//	}
//
//	public static String getName(Element element) {
//		if (element.getLocalName() == null) {
//			return element.getTagName();
//		}
//
//		return element.getLocalName();
//	}
//
//	public static String getQName(Element element) {
//		if (element.getTagName() != null) {
//			return element.getTagName();
//		}
//
//		return element.getLocalName();
//	}
//
//	public static String getQName(Attr attribute) {
//		if (attribute.getLocalName() != null) {
//			return attribute.getName();
//		}
//
//		return attribute.getLocalName();
//	}
//
//	private static boolean hasContent(Element element) {
//		return element.getChildNodes().getLength() > 0;
//	}
//
//	private static String getText(Element element) {
//		StringBuilder text = new StringBuilder();
//		NodeList nodes = element.getChildNodes();
//
//		for (int i = 0; i < nodes.getLength(); i++) {
//			if (nodes.item(i) instanceof Text) {
//				text.append(((Text)nodes.item(i)).getData());
//			}
//		}
//
//		return text.toString().trim();
//	}
//
//	private static boolean isTextOnly(Element element) {
//		NodeList nodes = element.getChildNodes();
//
//		for (int i = 0; i < nodes.getLength(); i++) {
//			if (!(nodes.item(i) instanceof Text)) {
//				return false;
//			}
//		}
//
//		return true;
//	}

	public static boolean showNamespaces() {
		return System.getProperty(SHOW_NAMESPACES, String.valueOf(true)).equals(String.valueOf(true));
	}

    public static boolean showValues() {
		return System.getProperty(SHOW_VALUES, String.valueOf(true)).equals(String.valueOf(true));
	}

    public static boolean showAttributes() {
		return System.getProperty(SHOW_ATTRIBUTES, String.valueOf(true)).equals(String.valueOf(true));
	}

    public static boolean showComments() {
		return System.getProperty(SHOW_COMMENTS, String.valueOf(true)).equals(String.valueOf(true));
	}
}
