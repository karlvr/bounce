/*
 * $Id: XMLViewUtilities.java,v 1.5 2009/01/22 22:14:59 edankert Exp $
 *
 * Copyright (c) 2002 - 2008, Edwin Dankert, Evgeniy Smelik 
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

package org.bounce.text.xml;

import javax.swing.text.*;
import java.awt.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The XML View uses the XML scanner to determine the style (font, color) of the
 * text that it renders.
 * <p>
 * <b>Note: </b> The XML Editor package is based on the JavaEditorKit example as
 * described in the article <i>'Customizing a Text Editor' </i> by <b>Timothy
 * Prinzing </b>. See:
 * http://java.sun.com/products/jfc/tsc/articles/text/editor_kit/
 * </p>
 *
 * @author Edwin Dankert <edankert@gmail.com>, Evgeniy Smelik <sever@yandex.ru>
 * @version $Revision: 1.5 $, $Date: 2009/01/22 22:14:59 $
 */
class XMLViewUtilities {
    private static Segment lineBuffer = null;
    private static final Pattern xmlTag = Pattern.compile("(<[.+]*[^<>]*>)");

    /**
     * Renders the given range in the model as normal unselected text. This will
     * paint the text according to the styles..
     *
     * @param view    XML document view
     * @param scanner XML scanner
     * @param context XML context
     * @param g       the graphics context
     * @param x       the starting X coordinate
     * @param y       the starting Y coordinate
     * @param start   the beginning position in the model
     * @param end     the ending position in the model
     * @return the location of the end of the range
     * @throws BadLocationException if the range is invalid
     */
    @SuppressWarnings({"unchecked"})
    static int drawUnselectedText(View view, XMLScanner scanner, XMLContext context, Graphics g, int x, int y, int start, int end) throws BadLocationException {
        Document doc = view.getDocument();
        Style lastStyle = null;
        int mark = start;

        while (start < end) {
            updateScanner(scanner, doc, start);

            int p = Math.min(scanner.getEndOffset(), end);
            p = (p <= start) ? end : p;

            Style style = context.getStyle(scanner.token);

            // If the style changes, do paint...
            if (style != lastStyle && lastStyle != null) {
                // color change, flush what we have
                g.setColor(context.getForeground(lastStyle));
                g.setFont(g.getFont().deriveFont(context.getFontStyle(lastStyle)));

                Segment text = getLineBuffer();
                doc.getText(mark, start - mark, text);

                x = Utilities.drawTabbedText(text, x, y, g, (TabExpander) view, mark);

                mark = start;
            }

            lastStyle = style;
            start = p;
        }

        // flush remaining
        g.setColor(context.getForeground(lastStyle));
        g.setFont(g.getFont().deriveFont(context.getFontStyle(lastStyle)));
        Segment text = getLineBuffer();
        doc.getText(mark, end - mark, text);

        x = Utilities.drawTabbedText(text, x, y, g, (TabExpander) view, mark);
        
        return x;
    }

    /**
     * Renders the given range in the model as selected text. This will paint
     * the text according to the font as found in the styles..
     *
     * @param view    XML document view
     * @param scanner XML scanner
     * @param context XML context
     * @param g       the graphics context
     * @param x       the starting X coordinate
     * @param y       the starting Y coordinate
     * @param start   the beginning position in the model
     * @param end     the ending position in the model
     * @return the location of the end of the range
     * @throws BadLocationException if the range is invalid
     */
    @SuppressWarnings({"unchecked"})
    static int drawSelectedText(View view, XMLScanner scanner, XMLContext context, Graphics g, int x, int y, int start, int end) throws BadLocationException {
        Document doc = view.getDocument();
        Style lastStyle = null;
        int mark = start;

        while (start < end) {
            updateScanner(scanner, doc, start);

            int p = Math.min(scanner.getEndOffset(), end);
            p = (p <= start) ? end : p;

            Style style = context.getStyle(scanner.token);

            // If the style changes, do paint...
            if (style != lastStyle && lastStyle != null) {
                // color change, flush what we have
                g.setFont(g.getFont().deriveFont(context.getFontStyle(lastStyle)));

                Segment text = getLineBuffer();
                doc.getText(mark, start - mark, text);

                x = Utilities.drawTabbedText(text, x, y, g, (TabExpander) view, mark);

                mark = start;
            }

            lastStyle = style;
            start = p;
        }

        // flush remaining
        g.setFont(g.getFont().deriveFont(context.getFontStyle(lastStyle)));
        Segment text = getLineBuffer();
        doc.getText(mark, end - mark, text);

        x = Utilities.drawTabbedText(text, x, y, g, (TabExpander) view, mark);

        return x;
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public static void drawBorder(View view, Graphics g, Shape shape, Document document) {
        Map<Integer, TagInfo> tagsMap = (Map<Integer, TagInfo>) document.getProperty(XMLDocument.FOLDING_MARKING);

        if (tagsMap != null) {
            tagsMap.clear();

            //clear border
            int height = shape.getBounds().height;
            Color color = g.getColor();
            g.setColor(Color.WHITE);
            g.drawPolygon(new int[] {0, 15, 15, 0}, new int[] {0, 0, height, height}, 4);
            g.setColor(color);

            //prepare info for the folding markers
            try {
                String visibleTextFromBegin = document.getText(0, document.getLength());
                Matcher matcher = xmlTag.matcher(visibleTextFromBegin);
                String curGroup, curTag;
                Rectangle curRectangle;
                int mark;
                boolean xmlTagStart, xmlTagSingle;
                while (matcher.find()) {
                    curGroup = matcher.group();
                    if (curGroup.startsWith("<!--") || curGroup.startsWith("<?")) continue;
                    xmlTagStart = !curGroup.startsWith("</");
                    xmlTagSingle = curGroup.endsWith("/>");
                    int spaceIndex = curGroup.indexOf(" "), tagCloseIndex = curGroup.indexOf(">");
                    if (spaceIndex > 0 && spaceIndex < tagCloseIndex) tagCloseIndex = spaceIndex;
                    curTag = curGroup.substring(xmlTagStart ? 1 : 2, tagCloseIndex);

                    mark = matcher.start();
                    TagInfo tagInfo = tagsMap.get(matcher.start());
                    if (tagInfo == null) {
                        TagInfo curTagInfo, twinTagInfo = null;
                        for (Integer key : tagsMap.keySet()) {
                            curTagInfo = tagsMap.get(key);
                            if (curTagInfo.getTag().equals(curTag) && !curTagInfo.isFilled()) twinTagInfo = curTagInfo;
                        }
                        if (twinTagInfo != null) tagInfo = twinTagInfo;

                        if (tagInfo == null) {
                            tagInfo = new TagInfo();
                            tagInfo.setTag(curTag);
                        }
                        if (tagsMap.get(mark) == null) tagsMap.put(mark, tagInfo);

                        curRectangle = (Rectangle) view.modelToView(mark, shape, Position.Bias.Forward);

                        if (xmlTagSingle) {
                            tagInfo.setFirstOrdinate(curRectangle.y);
                            tagInfo.setSecondOrdinate(curRectangle.y);
                            tagInfo.setFirstMark(mark);
                            int secondMark = mark + curTag.length() + 9;
                            tagInfo.setSecondMark(secondMark);
                            tagsMap.put(secondMark, tagInfo);
                        } else {
                            if (xmlTagStart) {
                                tagInfo.setFirstOrdinate(curRectangle.y);
                                tagInfo.setFirstMark(mark);
                            } else {
                                tagInfo.setSecondOrdinate(curRectangle.y);
                                tagInfo.setSecondMark(mark);
                            }
                        }

                        tagInfo.setFolded(curGroup.endsWith("{...}/>"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            int skipMark = -1;
            TagInfo tagInfo;
            //rendering folding trace lines
            for (Integer key : tagsMap.keySet()) {
                tagInfo = tagsMap.get(key);
                if (skipMark != -1 && skipMark != key) {
                    continue;
                } else {
                    if (skipMark == -1 && tagInfo.isFolded()) {
                        skipMark = tagInfo.getSecondMark();
                    } else {
                        skipMark = -1;
                    }
                }

                if (skipMark == -1 && !tagInfo.isFolded())
                    drawTraceLines(g, tagInfo, shape);
            }

            skipMark = -1;
            //rendering folding markers
            for (Integer key : tagsMap.keySet()) {
                tagInfo = tagsMap.get(key);
                if (skipMark != -1 && skipMark != key) {
                    continue;
                } else {
                    if (skipMark == -1 && tagInfo.isFolded()) {
                        skipMark = tagInfo.getSecondMark();
                    } else {
                        skipMark = -1;
                    }
                }

                if ((skipMark == -1 || tagInfo.isFolded()) && !tagInfo.isProcessed()) {
                    drawFoldingMarkers(g, tagInfo);
                    tagInfo.setProcessed(true);
                } else {
                    tagInfo.setProcessed(false);
                }
            }
        }
    }

    private static void drawTraceLines(Graphics g, TagInfo tagInfo, Shape shape) {
        if (!tagInfo.isOrdinatesEquals()) {
            int fontHeight = g.getFont().getSize();

            int firstOrdinate = tagInfo.getFirstOrdinate(), secondOrdinate = tagInfo.getSecondOrdinate();

            if (firstOrdinate == -1) firstOrdinate = 0;
            if (secondOrdinate == -1) secondOrdinate = shape.getBounds().height;

            drawTraceLine(g, firstOrdinate + fontHeight, secondOrdinate, true);
        }
    }

    private static void drawFoldingMarkers(Graphics g, TagInfo tagInfo) {
        if (!tagInfo.isOrdinatesEquals() || tagInfo.isFolded()) {
            int firstOrdinate = tagInfo.getFirstOrdinate(), secondOrdinate = tagInfo.getSecondOrdinate();

            if (tagInfo.isFolded()) {
                drawClosedMarker(g, firstOrdinate, tagInfo);
            } else {
                if (firstOrdinate != -1) drawFirstMarker(g, firstOrdinate, tagInfo);
                if (secondOrdinate != -1) drawSecondMarker(g, secondOrdinate, tagInfo);
            }
        }
    }

    private static void drawTraceLine(Graphics g, int begin, int end, boolean dotted) {
        Color color = g.getColor();
        if (dotted) {
            for (int i = begin; i <= end; i = i + 2) {
                g.setColor(Color.GRAY);
                g.drawLine(5, i, 5, i);
                g.setColor(Color.WHITE);
                g.drawLine(5, i + 1, 5, i + 1);
            }
        } else {
            g.setColor(Color.GRAY);
            g.drawLine(5, begin, 5, end);
        }
        g.setColor(color);
    }

    private static void drawFirstMarker(Graphics g, int y, TagInfo tagInfo) {
        Color color = g.getColor();
        g.setColor(Color.GRAY);

        Polygon polygon = new Polygon();
        polygon.addPoint(1, 1);
        polygon.addPoint(9, 1);
        polygon.addPoint(9, 7);
        polygon.addPoint(5, 11);
        polygon.addPoint(1, 7);
        polygon.translate(0, y);
        tagInfo.setFirstPolygon(polygon);

        g.setColor(Color.WHITE);
        g.fillPolygon(polygon);

        g.setColor(Color.GRAY);
        g.drawPolygon(polygon);

        g.drawLine(3, y + 5, 7, y + 5);

        g.setColor(color);
    }

    private static void drawSecondMarker(Graphics g, int y, TagInfo tagInfo) {
        Color color = g.getColor();
        g.setColor(Color.GRAY);

        Polygon polygon = new Polygon();
        polygon.addPoint(9, 11);
        polygon.addPoint(1, 11);
        polygon.addPoint(1, 5);
        polygon.addPoint(5, 1);
        polygon.addPoint(9, 5);
        polygon.translate(0, y);
        tagInfo.setSecondPolygon(polygon);

        g.setColor(Color.WHITE);
        g.fillPolygon(polygon);

        g.setColor(Color.GRAY);
        g.drawPolygon(polygon);

        g.drawLine(3, y + 7, 7, y + 7);

        g.setColor(color);
    }

    private static void drawClosedMarker(Graphics g, int y, TagInfo tagInfo) {
        Color color = g.getColor();
        g.setColor(Color.GRAY);

        Polygon polygon = new Polygon();
        polygon.addPoint(1, 1);
        polygon.addPoint(9, 1);
        polygon.addPoint(9, 9);
        polygon.addPoint(1, 9);
        polygon.translate(0, y);
        tagInfo.setFirstPolygon(polygon);

        g.setColor(Color.WHITE);
        g.fillPolygon(polygon);

        g.setColor(Color.GRAY);
        g.drawPolygon(polygon);

        g.drawLine(3, y + 5, 7, y + 5);
        g.drawLine(5, y + 3, 5, y + 7);

        g.setColor(color);
    }

    // Update the scanner to point to the '<' begin token.
    private static void updateScanner(XMLScanner scanner, Document doc, int p) {
        try {
            if (!scanner.isValid()) {
                scanner.setRange(getTagEnd(doc, p), doc.getLength());
                scanner.setValid(true);
            }

            while (scanner.getEndOffset() <= p) {
                scanner.scan();
            }
        } catch (Throwable e) {
            // can't adjust scanner... calling logic
            // will simply render the remaining text.
            e.printStackTrace();
        }
    }

    // Return the end position of the current tag.
    private static int getTagEnd(Document doc, int p) {
        int elementEnd = 0;

        if (p > 0) {
            try {
                int index;

                String s = doc.getText(0, p);
                int cdataStart = s.lastIndexOf("<![CDATA[");
                int cdataEnd = s.lastIndexOf("]]>");
                int commentStart = s.lastIndexOf("<!--");
                int commentEnd = s.lastIndexOf("-->");

                if (cdataStart > 0 && cdataStart > cdataEnd) {
                    index = s.lastIndexOf(">", cdataStart);
                } else if (commentStart > 0 && commentStart > commentEnd) {
                    index = s.lastIndexOf(">", commentStart);
                } else {
                    index = s.lastIndexOf(">");
                }

                if (index != -1) elementEnd = index;
            } catch (BadLocationException bl) {
                //empty
            }
        }

        return elementEnd;
    }

    private static Segment getLineBuffer() {
        if (lineBuffer == null) lineBuffer = new Segment();
        return lineBuffer;
    }

    public static class TagInfo {
        private String tag;
        private int firstMark, secondMark;
        private int firstOrdinate = -1, secondOrdinate = -1;
        private Polygon firstPolygon, secondPolygon;
        private boolean folded = false, processed = false;

        public TagInfo() {
        }

        public boolean isOrdinatesEquals() {
            return firstOrdinate == secondOrdinate;
        }

        public boolean isFilled() {
            return firstMark != 0 && secondMark != 0;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getFirstMark() {
            return firstMark;
        }

        public void setFirstMark(int firstMark) {
            this.firstMark = firstMark;
        }

        public int getSecondMark() {
            return secondMark;
        }

        public void setSecondMark(int secondMark) {
            this.secondMark = secondMark;
        }

        public int getFirstOrdinate() {
            return firstOrdinate;
        }

        public void setFirstOrdinate(int firstOrdinate) {
            this.firstOrdinate = firstOrdinate;
        }

        public int getSecondOrdinate() {
            return secondOrdinate;
        }

        public void setSecondOrdinate(int secondOrdinate) {
            this.secondOrdinate = secondOrdinate;
        }

        public Polygon getFirstPolygon() {
            return firstPolygon;
        }

        public void setFirstPolygon(Polygon firstPolygon) {
            this.firstPolygon = firstPolygon;
        }

        public Polygon getSecondPolygon() {
            return secondPolygon;
        }

        public void setSecondPolygon(Polygon secondPolygon) {
            this.secondPolygon = secondPolygon;
        }

        public boolean isFolded() {
            return folded;
        }

        public void setFolded(boolean folded) {
            this.folded = folded;
        }

        public boolean isProcessed() {
            return processed;
        }

        public void setProcessed(boolean processed) {
            this.processed = processed;
        }

        public String toString() {
            return tag;
        }
    }
}