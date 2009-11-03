package org.bounce.xml;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class DOMUtils {
	public static boolean isWhiteSpace(Text node) {
		return node.getData().trim().length() == 0;
	}

	public static boolean isMixed(Element element) {
		boolean elementFound = false;
		boolean textFound = false;

		NodeList nodes = element.getChildNodes();

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			if (node instanceof Element) {
				elementFound = true;
			} else if (node instanceof Text) {
				if (!isWhiteSpace((Text) node)) {
					textFound = true;
				}
			}

			if (textFound && elementFound) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param parent
	 *            the parent element.
	 * @param name
	 *            the name of the tag to match on. The special value "*" matches
	 *            all tags.
	 * @return the first matching element.
	 */
	public Element getElementByTagName(Element parent, String name) {
		Element result = null;

		NodeList list = parent.getElementsByTagName(name);
		if (list.getLength() > 0) {
			result = (Element) list.item(0);
		}

		return result;
	}

	/**
	 * @param parent
	 *            the parent element.
	 * @param namespaceURI
	 *            The namespace URI of the elements to match on. The special
	 *            value "*" matches all namespaces.
	 * @param localName
	 *            The local name of the elements to match on. The special value
	 *            "*" matches all local names.
	 * @return the first matching element.
	 */
	public Element getElementByTagNameNS(Element parent, String namespaceURI, String localName) {
		Element result = null;

		NodeList list = parent.getElementsByTagNameNS(namespaceURI, localName);
		if (list.getLength() > 0) {
			result = (Element) list.item(0);
		}

		return result;
	}

	public static String getName(Attr attribute) {
		if (attribute.getLocalName() == null) {
			return attribute.getName();
		}

		return attribute.getLocalName();
	}

	public static String getName(Element element) {
		if (element.getLocalName() == null) {
			return element.getTagName();
		}

		return element.getLocalName();
	}

	public static String getQName(Element element) {
		if (element.getTagName() != null) {
			return element.getTagName();
		}

		return element.getLocalName();
	}

	public static String getQName(Attr attribute) {
		if (attribute.getLocalName() != null) {
			return attribute.getName();
		}

		return attribute.getLocalName();
	}

	public static boolean hasContent(Element element) {
		return element.getChildNodes().getLength() > 0;
	}

	public static String getText(Element element) {
		StringBuilder text = new StringBuilder();
		NodeList nodes = element.getChildNodes();

		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i) instanceof Text) {
				text.append(((Text) nodes.item(i)).getData());
			}
		}

		return text.toString().trim();
	}

	public static boolean isTextOnly(Element element) {
		NodeList nodes = element.getChildNodes();

		for (int i = 0; i < nodes.getLength(); i++) {
			if (!(nodes.item(i) instanceof Text)) {
				return false;
			}
		}

		return true;
	}
}
