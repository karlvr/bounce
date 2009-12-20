package org.bounce.text.xml;

import java.io.IOException;

import javax.swing.text.BadLocationException;
import javax.swing.text.GapContent;
import javax.swing.text.PlainDocument;
import javax.swing.text.AbstractDocument.Content;
import javax.xml.stream.events.XMLEvent;

import junit.framework.TestCase;

public class XMLScannerTest extends TestCase {
	public void testStartTag() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test>");
		PlainDocument doc = new PlainDocument(content);
		assertEquals("document text", "<test>", doc.getText(0, 6));
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 5, scanner.getEndOffset());
		assertEquals("'test' start-offset", 1, scanner.getStartOffset());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 5, scanner.getStartOffset());
		assertEquals("'>' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 6, scanner.getStartOffset());
		assertEquals("'>' end-offset", 6, scanner.getEndOffset());
	}
	
	public void testBadStartTag() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test<");
		PlainDocument doc = new PlainDocument(content);
		assertEquals("document text", "<test<", doc.getText(0, 6));
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 5, scanner.getEndOffset());
		assertEquals("'test' start-offset", 1, scanner.getStartOffset());
		assertTrue("error", scanner.isError());

		scanner.scan();

		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'<' start-offset", 5, scanner.getStartOffset());
		assertEquals("'<' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("EOF start-offset", 6, scanner.getStartOffset());
		assertEquals("EOF end-offset", 6, scanner.getEndOffset());
		assertTrue("EOF error", scanner.isError());
	}

	public void testBadStartTagName() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<1test>");
		PlainDocument doc = new PlainDocument(content);
		assertEquals("document text", "<1test>", doc.getText(0, 7));
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'1test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'1test' start-offset", 1, scanner.getStartOffset());
		assertEquals("'1test' end-offset", 6, scanner.getEndOffset());
		assertTrue("'1test' error", scanner.isError());

//		scanner.scan();
//
//		assertEquals("'test'", XMLStyleConstants.ELEMENT_NAME, scanner.token);
//		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
//		assertEquals("'test' start-offset", 2, scanner.getStartOffset());
//		assertEquals("'test' end-offset", 6, scanner.getEndOffset());
//		assertFalse("'test' error", scanner.isError());

		scanner.scan();

		assertEquals(">", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("> start-offset", 6, scanner.getStartOffset());
		assertEquals("> end-offset", 7, scanner.getEndOffset());
		assertFalse("> error", scanner.isError());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 7, scanner.getStartOffset());
		assertEquals("'>' end-offset", 7, scanner.getEndOffset());
	}

	public void testStartTagPrefix() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<pre:test>");
		PlainDocument doc = new PlainDocument(content);
		assertEquals("document text", "<pre:test>", doc.getText(0, 10));
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'pre' = ELEMENT_PREFIX", XMLStyleConstants.ELEMENT_PREFIX, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'pre' end-offset", 4, scanner.getEndOffset());
		assertEquals("'pre' start-offset", 1, scanner.getStartOffset());

		scanner.scan();

		assertEquals("':' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("':' start-offset", 4, scanner.getStartOffset());
		assertEquals("':' end-offset", 5, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 9, scanner.getEndOffset());
		assertEquals("'test' start-offset", 5, scanner.getStartOffset());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 9, scanner.getStartOffset());
		assertEquals("'>' end-offset", 10, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 10, scanner.getStartOffset());
		assertEquals("'>' end-offset", 10, scanner.getEndOffset());
	}

	public void testStartTagWithAttributes() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test att='test'>");
		PlainDocument doc = new PlainDocument(content);
		assertEquals("document text", "<test att='test'>", doc.getText(0, doc.getLength()));
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 5, scanner.getEndOffset());
		assertEquals("'test' start-offset", 1, scanner.getStartOffset());

		scanner.scan();

		assertEquals("' ' = WHITESPACE", XMLStyleConstants.WHITESPACE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 5, scanner.getStartOffset());
		assertEquals("'att' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'att' = ATTRIBUTE-NAME", XMLStyleConstants.ATTRIBUTE_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 6, scanner.getStartOffset());
		assertEquals("'att' end-offset", 9, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'=' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'=' start-offset", 9, scanner.getStartOffset());
		assertEquals("'=' end-offset", 10, scanner.getEndOffset());

		scanner.scan();

		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'\"test\"' start-offset", 10, scanner.getStartOffset());
		assertEquals("'\"test\"' end-offset", 16, scanner.getEndOffset());
		assertEquals("'\"test\"' = ATTRIBUTE_VALUE", XMLStyleConstants.ATTRIBUTE_VALUE, scanner.token);

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 16, scanner.getStartOffset());
		assertEquals("'>' end-offset", 17, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("EOF start-offset", 17, scanner.getStartOffset());
		assertEquals("EOF end-offset", 17, scanner.getEndOffset());
	}

	public void testAttribute() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test att='t\"e>s<t'>");
		PlainDocument doc = new PlainDocument(content);
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 5, scanner.getEndOffset());
		assertEquals("'test' start-offset", 1, scanner.getStartOffset());

		scanner.scan();

		assertEquals("' ' = WHITESPACE", XMLStyleConstants.WHITESPACE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 5, scanner.getStartOffset());
		assertEquals("'att' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'att' = ATTRIBUTE-NAME", XMLStyleConstants.ATTRIBUTE_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 6, scanner.getStartOffset());
		assertEquals("'att' end-offset", 9, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'=' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'=' start-offset", 9, scanner.getStartOffset());
		assertEquals("'=' end-offset", 10, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'t\"e>s&lt;t' = ATTRIBUTE_VALUE", XMLStyleConstants.ATTRIBUTE_VALUE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'t\"e>s&lt;t' start-offset", 10, scanner.getStartOffset());
		assertEquals("'t\"e>s&lt;t' end-offset", 16, scanner.getEndOffset());
		assertTrue("'t\"e>s&lt;t' error", scanner.isError());

		scanner.scan();

		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'<' start-offset", 16, scanner.getStartOffset());
		assertEquals("'<' end-offset", 17, scanner.getEndOffset());
		assertFalse("'<' error", scanner.isError());

		scanner.scan();

		assertEquals("'t''", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'t'' start-offset", 17, scanner.getStartOffset());
		assertEquals("'t'' end-offset", 19, scanner.getEndOffset());
		assertTrue("'t'' error", scanner.isError());

		scanner.scan();

		assertEquals(">'", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals(">' start-offset", 19, scanner.getStartOffset());
		assertEquals(">' end-offset", 20, scanner.getEndOffset());
		assertFalse("'>' error", scanner.isError());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("EOF start-offset", 20, scanner.getStartOffset());
		assertEquals("EOF end-offset", 20, scanner.getEndOffset());
	}

	public void testEmptyAttribute() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test att=''>");
		PlainDocument doc = new PlainDocument(content);
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 5, scanner.getEndOffset());
		assertEquals("'test' start-offset", 1, scanner.getStartOffset());

		scanner.scan();

		assertEquals("' ' = Whitespace", XMLStyleConstants.WHITESPACE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 5, scanner.getStartOffset());
		assertEquals("'att' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'att' = ATTRIBUTE-NAME", XMLStyleConstants.ATTRIBUTE_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 6, scanner.getStartOffset());
		assertEquals("'att' end-offset", 9, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'=' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'=' start-offset", 9, scanner.getStartOffset());
		assertEquals("'=' end-offset", 10, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'' = ATTRIBUTE_VALUE", XMLStyleConstants.ATTRIBUTE_VALUE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'' start-offset", 10, scanner.getStartOffset());
		assertEquals("'' end-offset", 12, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 12, scanner.getStartOffset());
		assertEquals("'>' end-offset", 13, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 13, scanner.getStartOffset());
		assertEquals("'>' end-offset", 13, scanner.getEndOffset());
	}

	public void testAttributeWithOnlyEntity() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test att='&amp;'>");
		PlainDocument doc = new PlainDocument(content);
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 5, scanner.getEndOffset());
		assertEquals("'test' start-offset", 1, scanner.getStartOffset());

		scanner.scan();

		assertEquals("' ' = Whitespace", XMLStyleConstants.WHITESPACE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 5, scanner.getStartOffset());
		assertEquals("'att' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'att' = ATTRIBUTE-NAME", XMLStyleConstants.ATTRIBUTE_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 6, scanner.getStartOffset());
		assertEquals("'att' end-offset", 9, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'=' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'=' start-offset", 9, scanner.getStartOffset());
		assertEquals("'=' end-offset", 10, scanner.getEndOffset());

		scanner.scan();

		assertEquals("' = ATTRIBUTE_VALUE", XMLStyleConstants.ATTRIBUTE_VALUE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("' start-offset", 10, scanner.getStartOffset());
		assertEquals("' end-offset", 11, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'&amp;' = ATTRIBUTE_VALUE", XMLStyleConstants.ENTITY_REFERENCE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'&amp; start-offset", 11, scanner.getStartOffset());
		assertEquals("'&amp; end-offset", 16, scanner.getEndOffset());

		scanner.scan();

		assertEquals("' = ATTRIBUTE_VALUE", XMLStyleConstants.ATTRIBUTE_VALUE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("' start-offset", 16, scanner.getStartOffset());
		assertEquals("' end-offset", 17, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 17, scanner.getStartOffset());
		assertEquals("'>' end-offset", 18, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 18, scanner.getStartOffset());
		assertEquals("'>' end-offset", 18, scanner.getEndOffset());
	}

	public void testAttributeWithEntity() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test att='test&amp;test'>");
		PlainDocument doc = new PlainDocument(content);
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test end-offset", 5, scanner.getEndOffset());
		assertEquals("'test start-offset", 1, scanner.getStartOffset());

		scanner.scan();

		assertEquals("' ' = Whitespace", XMLStyleConstants.WHITESPACE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 5, scanner.getStartOffset());
		assertEquals("'att' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'att' = ATTRIBUTE-NAME", XMLStyleConstants.ATTRIBUTE_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 6, scanner.getStartOffset());
		assertEquals("'att' end-offset", 9, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'=' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'=' start-offset", 9, scanner.getStartOffset());
		assertEquals("'=' end-offset", 10, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'test = ATTRIBUTE_VALUE", XMLStyleConstants.ATTRIBUTE_VALUE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test start-offset", 10, scanner.getStartOffset());
		assertEquals("'test end-offset", 15, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'&amp;' = ATTRIBUTE_VALUE", XMLStyleConstants.ENTITY_REFERENCE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'&amp; start-offset", 15, scanner.getStartOffset());
		assertEquals("'&amp; end-offset", 20, scanner.getEndOffset());

		scanner.scan();

		assertEquals("test' = ATTRIBUTE_VALUE", XMLStyleConstants.ATTRIBUTE_VALUE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("test' start-offset", 20, scanner.getStartOffset());
		assertEquals("test' end-offset", 25, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 25, scanner.getStartOffset());
		assertEquals("'>' end-offset", 26, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 26, scanner.getStartOffset());
		assertEquals("'>' end-offset", 26, scanner.getEndOffset());
	}

	public void testAttributeWithCharacterReference() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test att='test&#45;test'>");
		PlainDocument doc = new PlainDocument(content);
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test end-offset", 5, scanner.getEndOffset());
		assertEquals("'test start-offset", 1, scanner.getStartOffset());

		scanner.scan();

		assertEquals("' ' = Whitespace", XMLStyleConstants.WHITESPACE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 5, scanner.getStartOffset());
		assertEquals("'att' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'att' = ATTRIBUTE-NAME", XMLStyleConstants.ATTRIBUTE_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 6, scanner.getStartOffset());
		assertEquals("'att' end-offset", 9, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'=' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'=' start-offset", 9, scanner.getStartOffset());
		assertEquals("'=' end-offset", 10, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'test = ATTRIBUTE_VALUE", XMLStyleConstants.ATTRIBUTE_VALUE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test start-offset", 10, scanner.getStartOffset());
		assertEquals("'test end-offset", 15, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'&amp;' = ATTRIBUTE_VALUE", XMLStyleConstants.ENTITY_REFERENCE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'&amp; start-offset", 15, scanner.getStartOffset());
		assertEquals("'&amp; end-offset", 20, scanner.getEndOffset());

		scanner.scan();

		assertEquals("test' = ATTRIBUTE_VALUE", XMLStyleConstants.ATTRIBUTE_VALUE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("test' start-offset", 20, scanner.getStartOffset());
		assertEquals("test' end-offset", 25, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 25, scanner.getStartOffset());
		assertEquals("'>' end-offset", 26, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 26, scanner.getStartOffset());
		assertEquals("'>' end-offset", 26, scanner.getEndOffset());
	}

	public void testAttributeWithIllegalEntity() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test att='test&a mp;test'>");
		PlainDocument doc = new PlainDocument(content);
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 5, scanner.getEndOffset());
		assertEquals("'test' start-offset", 1, scanner.getStartOffset());

		scanner.scan();

		assertEquals("' ' = Whitespace", XMLStyleConstants.WHITESPACE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 5, scanner.getStartOffset());
		assertEquals("'att' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'att' = ATTRIBUTE-NAME", XMLStyleConstants.ATTRIBUTE_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 6, scanner.getStartOffset());
		assertEquals("'att' end-offset", 9, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'=' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'=' start-offset", 9, scanner.getStartOffset());
		assertEquals("'=' end-offset", 10, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'test = ATTRIBUTE_VALUE", XMLStyleConstants.ATTRIBUTE_VALUE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test start-offset", 10, scanner.getStartOffset());
		assertEquals("'test end-offset", 15, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'&a ' = ENTITY_REFERENCE", XMLStyleConstants.ENTITY_REFERENCE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'&a ' start-offset", 15, scanner.getStartOffset());
		assertEquals("'&a mp;' end-offset", 21, scanner.getEndOffset());
		assertTrue("illegal entity reference", scanner.isError());

		scanner.scan();

		assertEquals("mp;test' = ENTITY_REFERENCE", XMLStyleConstants.ATTRIBUTE_VALUE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("test' start-offset", 21, scanner.getStartOffset());
		assertEquals("test' end-offset", 26, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 26, scanner.getStartOffset());
		assertEquals("'>' end-offset", 27, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 27, scanner.getStartOffset());
		assertEquals("'>' end-offset", 27, scanner.getEndOffset());
	}

	public void testAttributePrefix() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test pre:att='test'>");
		PlainDocument doc = new PlainDocument(content);
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 5, scanner.getEndOffset());
		assertEquals("'test' start-offset", 1, scanner.getStartOffset());

		scanner.scan();

		assertEquals("' ' = Whitespace", XMLStyleConstants.WHITESPACE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 5, scanner.getStartOffset());
		assertEquals("'att' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'pre' = ATTRIBUTE-PREFIX", XMLStyleConstants.ATTRIBUTE_PREFIX, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'pre' start-offset", 6, scanner.getStartOffset());
		assertEquals("'pre' end-offset", 9, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'=' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'=' start-offset", 9, scanner.getStartOffset());
		assertEquals("'=' end-offset", 10, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'att' = ATTRIBUTE-NAME", XMLStyleConstants.ATTRIBUTE_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 10, scanner.getStartOffset());
		assertEquals("'att' end-offset", 13, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'=' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'=' start-offset", 13, scanner.getStartOffset());
		assertEquals("'=' end-offset", 14, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'test' = ATTRIBUTE_VALUE", XMLStyleConstants.ATTRIBUTE_VALUE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'\"test\"' start-offset", 14, scanner.getStartOffset());
		assertEquals("'\"test\"' end-offset", 20, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 20, scanner.getStartOffset());
		assertEquals("'>' end-offset", 21, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 21, scanner.getStartOffset());
		assertEquals("'>' end-offset", 21, scanner.getEndOffset());
	}

	public void testNamespace() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test xmlns:pre='test'>");
		PlainDocument doc = new PlainDocument(content);
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 5, scanner.getEndOffset());
		assertEquals("'test' start-offset", 1, scanner.getStartOffset());

		scanner.scan();

		assertEquals("' ' = Whitespace", XMLStyleConstants.WHITESPACE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 5, scanner.getStartOffset());
		assertEquals("'att' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'xmlns' = NAMESPACE-NAME", XMLStyleConstants.NAMESPACE_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'xmlns' start-offset", 6, scanner.getStartOffset());
		assertEquals("'xmlns' end-offset", 11, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'=' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'=' start-offset", 11, scanner.getStartOffset());
		assertEquals("'=' end-offset", 12, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'pre' = NAMESPACE-PREFIX", XMLStyleConstants.NAMESPACE_PREFIX, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'pre' start-offset", 12, scanner.getStartOffset());
		assertEquals("'att' end-offset", 15, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'=' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'=' start-offset", 15, scanner.getStartOffset());
		assertEquals("'=' end-offset", 16, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'test' = NAMESPACE_VALUE", XMLStyleConstants.NAMESPACE_VALUE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'\"test\"' start-offset", 16, scanner.getStartOffset());
		assertEquals("'\"test\"' end-offset", 22, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 22, scanner.getStartOffset());
		assertEquals("'>' end-offset", 23, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 23, scanner.getStartOffset());
		assertEquals("'>' end-offset", 23, scanner.getEndOffset());
	}

	public void testEmptyElement() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test/>");
		PlainDocument doc = new PlainDocument(content);
		assertEquals("document text", "<test/>", doc.getText(0, 7));
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' start-offset", 1, scanner.getStartOffset());
		assertEquals("'test' end-offset", 5, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'/>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'/>' start-offset", 5, scanner.getStartOffset());
		assertEquals("'/>' end-offset", 7, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 7, scanner.getStartOffset());
		assertEquals("'>' end-offset", 7, scanner.getEndOffset());
	}

	public void testEmptyElementPrefix() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<pre:test/>");
		PlainDocument doc = new PlainDocument(content);
		assertEquals("document text", "<pre:test/>", doc.getText(0, 11));
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_PREFIX", XMLStyleConstants.ELEMENT_PREFIX, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 4, scanner.getEndOffset());
		assertEquals("'test' start-offset", 1, scanner.getStartOffset());

		scanner.scan();

		assertEquals("':' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("':' start-offset", 4, scanner.getStartOffset());
		assertEquals("':' end-offset", 5, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 9, scanner.getEndOffset());
		assertEquals("'test' start-offset", 5, scanner.getStartOffset());

		scanner.scan();

		assertEquals("'/>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'/>' start-offset", 9, scanner.getStartOffset());
		assertEquals("'/>' end-offset", 11, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 11, scanner.getStartOffset());
		assertEquals("'>' end-offset", 11, scanner.getEndOffset());
	}

	public void testEmptyElementWithAttribute() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test att=\"test\"/>");
		PlainDocument doc = new PlainDocument(content);
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' start-offset", 1, scanner.getStartOffset());
		assertEquals("'test' end-offset", 5, scanner.getEndOffset());

		scanner.scan();

		assertEquals("' ' = Whitespace", XMLStyleConstants.WHITESPACE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 5, scanner.getStartOffset());
		assertEquals("'att' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'att' = ATTRIBUTE-NAME", XMLStyleConstants.ATTRIBUTE_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 6, scanner.getStartOffset());
		assertEquals("'att' end-offset", 9, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'=' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'=' start-offset", 9, scanner.getStartOffset());
		assertEquals("'=' end-offset", 10, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'\"test\"' = ATTRIBUTE_VALUE", XMLStyleConstants.ATTRIBUTE_VALUE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'\"test\"' start-offset", 10, scanner.getStartOffset());
		assertEquals("'\"test\"' end-offset", 16, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'/>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'/>' start-offset", 16, scanner.getStartOffset());
		assertEquals("'/>' end-offset", 18, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 18, scanner.getStartOffset());
		assertEquals("'>' end-offset", 18, scanner.getEndOffset());
	}

	public void testEmptyElementWithAttributes() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test att=\'test\' att2=\"test\"/>");
		PlainDocument doc = new PlainDocument(content);
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' start-offset", 1, scanner.getStartOffset());
		assertEquals("'test' end-offset", 5, scanner.getEndOffset());

		scanner.scan();

		assertEquals("' ' = Whitespace", XMLStyleConstants.WHITESPACE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 5, scanner.getStartOffset());
		assertEquals("'att' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'att' = ATTRIBUTE-NAME", XMLStyleConstants.ATTRIBUTE_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 6, scanner.getStartOffset());
		assertEquals("'att' end-offset", 9, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'=' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'=' start-offset", 9, scanner.getStartOffset());
		assertEquals("'=' end-offset", 10, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'\"test\"' = ATTRIBUTE_VALUE", XMLStyleConstants.ATTRIBUTE_VALUE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'\"test\"' start-offset", 10, scanner.getStartOffset());
		assertEquals("'\"test\"' end-offset", 16, scanner.getEndOffset());

		scanner.scan();

		assertEquals("' ' = Whitespace", XMLStyleConstants.WHITESPACE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 16, scanner.getStartOffset());
		assertEquals("'att' end-offset", 17, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'att2' = ATTRIBUTE-NAME", XMLStyleConstants.ATTRIBUTE_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att2' start-offset", 17, scanner.getStartOffset());
		assertEquals("'att2' end-offset", 21, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'=' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'=' start-offset", 21, scanner.getStartOffset());
		assertEquals("'=' end-offset", 22, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'\"test\"' = SPECIAL", XMLStyleConstants.ATTRIBUTE_VALUE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'\"test\"' start-offset", 22, scanner.getStartOffset());
		assertEquals("'\"test\"' end-offset", 28, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'/>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'/>' start-offset", 28, scanner.getStartOffset());
		assertEquals("'/>' end-offset", 30, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 30, scanner.getStartOffset());
		assertEquals("'>' end-offset", 30, scanner.getEndOffset());
	}

	public void testEmptyElementWithSpaces() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test />");
		PlainDocument doc = new PlainDocument(content);
		assertEquals("document text", "<test />", doc.getText(0, 8));
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' start-offset", 1, scanner.getStartOffset());
		assertEquals("'test' end-offset", 5, scanner.getEndOffset());

		scanner.scan();

		assertEquals("' ' = Whitespace", XMLStyleConstants.WHITESPACE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 5, scanner.getStartOffset());
		assertEquals("'att' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'/>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'/>' start-offset", 6, scanner.getStartOffset());
		assertEquals("'/>' end-offset", 8, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 8, scanner.getStartOffset());
		assertEquals("'>' end-offset", 8, scanner.getEndOffset());
	}

	public void testEmptyElementWithAttributeWithSpaces() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test att  =  \"test\"  />");
		PlainDocument doc = new PlainDocument(content);
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' start-offset", 1, scanner.getStartOffset());
		assertEquals("'test' end-offset", 5, scanner.getEndOffset());

		scanner.scan();

		assertEquals("' ' = Whitespace", XMLStyleConstants.WHITESPACE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("' ' start-offset", 5, scanner.getStartOffset());
		assertEquals("' ' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'att' = ATTRIBUTE-NAME", XMLStyleConstants.ATTRIBUTE_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 6, scanner.getStartOffset());
		assertEquals("'att' end-offset", 9, scanner.getEndOffset());

		scanner.scan();

		assertEquals("' ' = Whitespace", XMLStyleConstants.WHITESPACE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("' ' start-offset", 9, scanner.getStartOffset());
		assertEquals("' ' end-offset", 11, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'=' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'=' start-offset", 11, scanner.getStartOffset());
		assertEquals("'=' end-offset", 12, scanner.getEndOffset());

		scanner.scan();

		assertEquals("' ' = Whitespace", XMLStyleConstants.WHITESPACE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("' ' start-offset", 12, scanner.getStartOffset());
		assertEquals("' ' end-offset", 14, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'\"test\"' = SPECIAL", XMLStyleConstants.ATTRIBUTE_VALUE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'\"test\"' start-offset", 14, scanner.getStartOffset());
		assertEquals("'\"test\"' end-offset", 20, scanner.getEndOffset());

		scanner.scan();

		assertEquals("' ' = Whitespace", XMLStyleConstants.WHITESPACE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("' ' start-offset", 20, scanner.getStartOffset());
		assertEquals("' ' end-offset", 22, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'/>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'/>' start-offset", 22, scanner.getStartOffset());
		assertEquals("'/>' end-offset", 24, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 24, scanner.getStartOffset());
		assertEquals("'>' end-offset", 24, scanner.getEndOffset());
	}

	public void testEndTag() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "</test>");
		PlainDocument doc = new PlainDocument(content);
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 2, scanner.getEndOffset());
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'</' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 6, scanner.getEndOffset());
		assertEquals("'test' start-offset", 2, scanner.getStartOffset());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 6, scanner.getStartOffset());
		assertEquals("'>' end-offset", 7, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("EOF start-offset", 7, scanner.getStartOffset());
		assertEquals("EOF end-offset", 7, scanner.getEndOffset());
	}
	
	public void testBadEndTag() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "</test<");
		PlainDocument doc = new PlainDocument(content);

		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 2, scanner.getEndOffset());
		assertEquals("'</' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 6, scanner.getEndOffset());
		assertEquals("'test' start-offset", 2, scanner.getStartOffset());
		assertTrue("error", scanner.isError());

		scanner.scan();

		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'<' start-offset", 6, scanner.getStartOffset());
		assertEquals("'<' end-offset", 7, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("EOF start-offset", 7, scanner.getStartOffset());
		assertEquals("EOF end-offset", 7, scanner.getEndOffset());
		assertTrue("EOF error", scanner.isError());
	}

	public void testBadEndTagName() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "</1test>");
		PlainDocument doc = new PlainDocument(content);
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'</' start-offset", 0, scanner.getStartOffset());
		assertEquals("'</' scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'</' end-offset", 2, scanner.getEndOffset());
		assertEquals("'</' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'1test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'1test' start-offset", 2, scanner.getStartOffset());
		assertEquals("'1test' end-offset", 7, scanner.getEndOffset());
		assertTrue("'1test' error", scanner.isError());

		scanner.scan();

		assertEquals(">", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("> start-offset", 7, scanner.getStartOffset());
		assertEquals("> end-offset", 8, scanner.getEndOffset());
		assertFalse("> error", scanner.isError());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 8, scanner.getStartOffset());
		assertEquals("'>' end-offset", 8, scanner.getEndOffset());
	}

	public void testEndTagPrefix() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "</pre:test>");
		PlainDocument doc = new PlainDocument(content);
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 2, scanner.getEndOffset());
		assertEquals("'</' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'pre' = ELEMENT_PREFIX", XMLStyleConstants.ELEMENT_PREFIX, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'pre' end-offset", 5, scanner.getEndOffset());
		assertEquals("'pre' start-offset", 2, scanner.getStartOffset());

		scanner.scan();

		assertEquals("':' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("':' start-offset", 5, scanner.getStartOffset());
		assertEquals("':' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 10, scanner.getEndOffset());
		assertEquals("'test' start-offset", 6, scanner.getStartOffset());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 10, scanner.getStartOffset());
		assertEquals("'>' end-offset", 11, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 11, scanner.getStartOffset());
		assertEquals("'>' end-offset", 11, scanner.getEndOffset());
	}


	public void testEmptyElement2() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test></test>");
		PlainDocument doc = new PlainDocument(content);
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 5, scanner.getEndOffset());
		assertEquals("'test' start-offset", 1, scanner.getStartOffset());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 5, scanner.getStartOffset());
		assertEquals("'>' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 6, scanner.getStartOffset());
		assertEquals("end-offset", 8, scanner.getEndOffset());
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'</' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 12, scanner.getEndOffset());
		assertEquals("'test' start-offset", 8, scanner.getStartOffset());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 12, scanner.getStartOffset());
		assertEquals("'>' end-offset", 13, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("EOF start-offset", 13, scanner.getStartOffset());
		assertEquals("EOF end-offset", 13, scanner.getEndOffset());
	}

	public void testContent() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test>this \" is test > / content > </test>");
		PlainDocument doc = new PlainDocument(content);
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 5, scanner.getEndOffset());
		assertEquals("'test' start-offset", 1, scanner.getStartOffset());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 5, scanner.getStartOffset());
		assertEquals("'>' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 6, scanner.getStartOffset());
		assertEquals("end-offset", 35, scanner.getEndOffset());
		assertEquals("scanner-type", XMLEvent.CHARACTERS, scanner.getEventType());
		assertEquals("'</' = SPECIAL", XMLStyleConstants.ELEMENT_VALUE, scanner.token);

		scanner.scan();

		assertEquals("start-offset", 35, scanner.getStartOffset());
		assertEquals("end-offset", 37, scanner.getEndOffset());
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'</' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 41, scanner.getEndOffset());
		assertEquals("'test' start-offset", 37, scanner.getStartOffset());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 41, scanner.getStartOffset());
		assertEquals("'>' end-offset", 42, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("EOF start-offset", 42, scanner.getStartOffset());
		assertEquals("EOF end-offset", 42, scanner.getEndOffset());
	}

	public void testContentWithEntity() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test>test&amp;test</test>");
		PlainDocument doc = new PlainDocument(content);
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 5, scanner.getEndOffset());
		assertEquals("'test' start-offset", 1, scanner.getStartOffset());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 5, scanner.getStartOffset());
		assertEquals("'>' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("test start-offset", 6, scanner.getStartOffset());
		assertEquals("test end-offset", 10, scanner.getEndOffset());
		assertEquals("test scanner-type", XMLEvent.CHARACTERS, scanner.getEventType());
		assertEquals("test", XMLStyleConstants.ELEMENT_VALUE, scanner.token);

		scanner.scan();

		assertEquals("start-offset", 10, scanner.getStartOffset());
		assertEquals("end-offset", 15, scanner.getEndOffset());
		assertEquals("scanner-type", XMLEvent.CHARACTERS, scanner.getEventType());
		assertEquals("'</' = SPECIAL", XMLStyleConstants.ENTITY_REFERENCE, scanner.token);

		scanner.scan();

		assertEquals("test start-offset", 15, scanner.getStartOffset());
		assertEquals("test end-offset", 19, scanner.getEndOffset());
		assertEquals("test scanner-type", XMLEvent.CHARACTERS, scanner.getEventType());
		assertEquals("test", XMLStyleConstants.ELEMENT_VALUE, scanner.token);

		scanner.scan();

		assertEquals("start-offset", 19, scanner.getStartOffset());
		assertEquals("end-offset", 21, scanner.getEndOffset());
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'</' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 25, scanner.getEndOffset());
		assertEquals("'test' start-offset", 21, scanner.getStartOffset());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 25, scanner.getStartOffset());
		assertEquals("'>' end-offset", 26, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("EOF start-offset", 26, scanner.getStartOffset());
		assertEquals("EOF end-offset", 26, scanner.getEndOffset());
	}

	public void testContentWithEntityRefAtStart() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test>&amp;test</test>");
		PlainDocument doc = new PlainDocument(content);
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 5, scanner.getEndOffset());
		assertEquals("'test' start-offset", 1, scanner.getStartOffset());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 5, scanner.getStartOffset());
		assertEquals("'>' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 6, scanner.getStartOffset());
		assertEquals("end-offset", 11, scanner.getEndOffset());
		assertEquals("scanner-type", XMLEvent.CHARACTERS, scanner.getEventType());
		assertEquals("'</' = SPECIAL", XMLStyleConstants.ENTITY_REFERENCE, scanner.token);

		scanner.scan();

		assertEquals("test start-offset", 11, scanner.getStartOffset());
		assertEquals("test end-offset", 15, scanner.getEndOffset());
		assertEquals("test scanner-type", XMLEvent.CHARACTERS, scanner.getEventType());
		assertEquals("test", XMLStyleConstants.ELEMENT_VALUE, scanner.token);

		scanner.scan();

		assertEquals("start-offset", 15, scanner.getStartOffset());
		assertEquals("end-offset", 17, scanner.getEndOffset());
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'</' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 21, scanner.getEndOffset());
		assertEquals("'test' start-offset", 17, scanner.getStartOffset());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 21, scanner.getStartOffset());
		assertEquals("'>' end-offset", 22, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("EOF start-offset", 22, scanner.getStartOffset());
		assertEquals("EOF end-offset", 22, scanner.getEndOffset());
	}

	public void testContentWithEntityDeclAtStart() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test>%test</test>");
		PlainDocument doc = new PlainDocument(content);
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 5, scanner.getEndOffset());
		assertEquals("'test' start-offset", 1, scanner.getStartOffset());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 5, scanner.getStartOffset());
		assertEquals("'>' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 6, scanner.getStartOffset());
		assertEquals("end-offset", 11, scanner.getEndOffset());
		assertEquals("scanner-type", XMLEvent.CHARACTERS, scanner.getEventType());
		assertEquals("'</' = SPECIAL", XMLStyleConstants.ELEMENT_VALUE, scanner.token);

		scanner.scan();

		assertEquals("start-offset", 11, scanner.getStartOffset());
		assertEquals("end-offset", 13, scanner.getEndOffset());
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'</' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 17, scanner.getEndOffset());
		assertEquals("'test' start-offset", 13, scanner.getStartOffset());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 17, scanner.getStartOffset());
		assertEquals("'>' end-offset", 18, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("EOF start-offset", 18, scanner.getStartOffset());
		assertEquals("EOF end-offset", 18, scanner.getEndOffset());
	}

	public void testStartTagPrefixIllegalName() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<pre:1test>");
		PlainDocument doc = new PlainDocument(content);
		assertEquals("document text", "<pre:1test>", doc.getText(0, 11));
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'pre' = ELEMENT_PREFIX", XMLStyleConstants.ELEMENT_PREFIX, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'pre' end-offset", 4, scanner.getEndOffset());
		assertEquals("'pre' start-offset", 1, scanner.getStartOffset());

		scanner.scan();

		assertEquals("':' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("':' start-offset", 4, scanner.getStartOffset());
		assertEquals("':' end-offset", 5, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'1test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'1test' end-offset", 10, scanner.getEndOffset());
		assertEquals("'1test' start-offset", 5, scanner.getStartOffset());
		assertTrue("'1test' error", scanner.isError());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 10, scanner.getStartOffset());
		assertEquals("'>' end-offset", 11, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 11, scanner.getStartOffset());
		assertEquals("'>' end-offset", 11, scanner.getEndOffset());
	}

	public void testStartTagPrefixIllegalName2() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<pre::test>");
		PlainDocument doc = new PlainDocument(content);
		assertEquals("document text", "<pre::test>", doc.getText(0, 11));
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'pre' = ELEMENT_PREFIX", XMLStyleConstants.ELEMENT_PREFIX, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'pre' end-offset", 4, scanner.getEndOffset());
		assertEquals("'pre' start-offset", 1, scanner.getStartOffset());

		scanner.scan();

		assertEquals("':' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("':' start-offset", 4, scanner.getStartOffset());
		assertEquals("':' end-offset", 5, scanner.getEndOffset());

		scanner.scan();

		assertEquals("':test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("':test' end-offset", 10, scanner.getEndOffset());
		assertEquals("':test' start-offset", 5, scanner.getStartOffset());
		assertTrue("':test' error", scanner.isError());

		scanner.scan();

		assertEquals("'>' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 10, scanner.getStartOffset());
		assertEquals("'>' end-offset", 11, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("'>' start-offset", 11, scanner.getStartOffset());
		assertEquals("'>' end-offset", 11, scanner.getEndOffset());
	}

	public void testIllegalAttribute1() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test att 'test'>");
		PlainDocument doc = new PlainDocument(content);
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 5, scanner.getEndOffset());
		assertEquals("'test' start-offset", 1, scanner.getStartOffset());

		scanner.scan();

		assertEquals("' ' = WHITESPACE", XMLStyleConstants.WHITESPACE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 5, scanner.getStartOffset());
		assertEquals("'att' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'att' = ATTRIBUTE-NAME", XMLStyleConstants.ATTRIBUTE_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 6, scanner.getStartOffset());
		assertEquals("'att' end-offset", 9, scanner.getEndOffset());

		scanner.scan();

		assertEquals("' ' = WHITESPACE", XMLStyleConstants.WHITESPACE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("' ' start-offset", 9, scanner.getStartOffset());
		assertEquals("' ' end-offset", 10, scanner.getEndOffset());
		assertTrue("'test' error", scanner.isError());

		scanner.scan();

		assertEquals("'test' = ATTRIBUTE_VALUE", XMLStyleConstants.ATTRIBUTE_VALUE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'test' start-offset", 10, scanner.getStartOffset());
		assertEquals("'test' end-offset", 16, scanner.getEndOffset());

		scanner.scan();

		assertEquals(">'", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals(">' start-offset", 16, scanner.getStartOffset());
		assertEquals(">' end-offset", 17, scanner.getEndOffset());
		assertFalse("'>' error", scanner.isError());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("EOF start-offset", 17, scanner.getStartOffset());
		assertEquals("EOF end-offset", 17, scanner.getEndOffset());
	}

	public void testIllegalAttribute2() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test att=atesta>");
		PlainDocument doc = new PlainDocument(content);
		
		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 1, scanner.getEndOffset());
		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("test = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("test end-offset", 5, scanner.getEndOffset());
		assertEquals("'testa start-offset", 1, scanner.getStartOffset());

		scanner.scan();

		assertEquals("' ' = WHITESPACE", XMLStyleConstants.WHITESPACE, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 5, scanner.getStartOffset());
		assertEquals("'att' end-offset", 6, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'att' = ATTRIBUTE-NAME", XMLStyleConstants.ATTRIBUTE_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'att' start-offset", 6, scanner.getStartOffset());
		assertEquals("'att' end-offset", 9, scanner.getEndOffset());

		scanner.scan();

		assertEquals("'=' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'=' start-offset", 9, scanner.getStartOffset());
		assertEquals("'=' end-offset", 10, scanner.getEndOffset());

		scanner.scan();

		assertEquals("atesta = ATTRIBUTE_NAME", XMLStyleConstants.ATTRIBUTE_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("atesta start-offset", 10, scanner.getStartOffset());
		assertEquals("atesta end-offset", 17, scanner.getEndOffset());
		assertTrue("atesta error", scanner.isError());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("EOF start-offset", 17, scanner.getStartOffset());
		assertEquals("EOF end-offset", 17, scanner.getEndOffset());
	}

	public void testBadEndTag2() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "</test<<");
		PlainDocument doc = new PlainDocument(content);

		XMLScanner scanner = new XMLScanner(doc);

		assertEquals("token selected", null, scanner.token);
		assertEquals("unknown", XMLEvent.START_DOCUMENT, scanner.getEventType());
		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("end-offset", 0, scanner.getEndOffset());

		scanner.scan();

		assertEquals("start-offset", 0, scanner.getStartOffset());
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("end-offset", 2, scanner.getEndOffset());
		assertEquals("'</' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);

		scanner.scan();

		assertEquals("'test' = ELEMENT_NAME", XMLStyleConstants.ELEMENT_NAME, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_ELEMENT, scanner.getEventType());
		assertEquals("'test' end-offset", 6, scanner.getEndOffset());
		assertEquals("'test' start-offset", 2, scanner.getStartOffset());
		assertTrue("error", scanner.isError());

		scanner.scan();

		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'<' start-offset", 6, scanner.getStartOffset());
		assertEquals("'<' end-offset", 7, scanner.getEndOffset());
		assertTrue("error", scanner.isError());

		scanner.scan();

		assertEquals("'<' = SPECIAL", XMLStyleConstants.SPECIAL, scanner.token);
		assertEquals("scanner-type", XMLEvent.START_ELEMENT, scanner.getEventType());
		assertEquals("'<' start-offset", 7, scanner.getStartOffset());
		assertEquals("'<' end-offset", 8, scanner.getEndOffset());

		scanner.scan();

		assertEquals("EOF", null, scanner.token);
		assertEquals("scanner-type", XMLEvent.END_DOCUMENT, scanner.getEventType());
		assertEquals("EOF start-offset", 8, scanner.getStartOffset());
		assertEquals("EOF end-offset", 8, scanner.getEndOffset());
		assertTrue("EOF error", scanner.isError());
	}

	public void testNextTag() throws BadLocationException, IOException {
		Content content = new GapContent();
		content.insertString(0, "<test><test><test/> <test> <test/></test> </test></test>");
		PlainDocument doc = new PlainDocument(content);

		XMLScanner scanner = new XMLScanner(doc);
		
		assertEquals("start-element", XMLEvent.START_ELEMENT, scanner.getNextTag());
		assertEquals("start-element", XMLEvent.START_ELEMENT, scanner.getNextTag());
		assertEquals("start-element", XMLEvent.START_ELEMENT, scanner.getNextTag());
		assertEquals("end-element", XMLEvent.END_ELEMENT, scanner.getNextTag());
		assertEquals("start-element", XMLEvent.START_ELEMENT, scanner.getNextTag());
		assertEquals("start-element", XMLEvent.START_ELEMENT, scanner.getNextTag());
		assertEquals("end-element", XMLEvent.END_ELEMENT, scanner.getNextTag());
		assertEquals("end-element", XMLEvent.END_ELEMENT, scanner.getNextTag());
		assertEquals("end-element", XMLEvent.END_ELEMENT, scanner.getNextTag());
		assertEquals("end-element", XMLEvent.END_ELEMENT, scanner.getNextTag());
	}
}
