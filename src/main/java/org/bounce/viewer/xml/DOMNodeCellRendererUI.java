/*
 * $Id: DOMNodeCellRendererUI.java,v 1.1 2008/04/15 20:59:50 edankert Exp $
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
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalLabelUI;

/**
 * Renderers the XML information in the Element Node.
 *
 * @author Edwin Dankert <edankert@gmail.com>
 */
public class DOMNodeCellRendererUI extends MetalLabelUI {
    protected static DOMNodeCellRendererUI labelUI = new DOMNodeCellRendererUI();
	private static final Color SELECTION_BACKGROUND = new Color( 204, 204, 204);
	private static final Color SELECTION_FOREGROUND = Color.black;

    public static ComponentUI createUI( JComponent c) {
        return labelUI;
    }

    /**
     * Paint clippedText at textX, textY with the labels foreground color.
     */
    protected void paintLine(JLabel l, Graphics g, Line line, int x, int y) {
		List<StyledString> strings = line.getStyledStrings();
		
		if (((DOMNodeCellRenderer)l).isSelected()) {
			g.setColor(SELECTION_BACKGROUND);
		    FontMetrics fm = g.getFontMetrics();
		    g.fillRect(x, (y - fm.getAscent()), fm.stringWidth(line.getText()) - 1, fm.getHeight());
		}

		for (StyledString string : strings) {
			if (((DOMNodeCellRenderer)l).isSelected()) {
				g.setColor(SELECTION_FOREGROUND);
			} else {
				g.setColor(string.getColor());
			}

			g.drawString(string.getText(), x, y);
			x = x + (int)g.getFontMetrics().getStringBounds(string.getText(), g).getWidth();
		}		
    }

    /** 
     * Paint the label text in the foreground color, if the label
     * is opaque then paint the entire background with the background
     * color.  The Label text is drawn by paintEnabledText() or
     * paintDisabledText().  The locations of the label parts are computed
     * by layoutCL.
     */
    public void paint(Graphics g, JComponent c) {
        DOMNodeCellRenderer renderer = (DOMNodeCellRenderer)c;
        FontMetrics fm = g.getFontMetrics();
		int y = fm.getAscent();
	
		for (Line line : renderer.getLines()) {
    		paintLine(renderer, g, line, 0, y);
			y = y + fm.getHeight();
		}
    }

    public Dimension getPreferredSize(JComponent c) {
        DOMNodeCellRenderer renderer = (DOMNodeCellRenderer)c;
		List<Line> lines = renderer.getLines();
		Graphics gc = renderer.getGraphics();

		int height = 0;
		int width = 0;
		
		if (gc != null) {
			FontMetrics fm = gc.getFontMetrics();

			if (lines.size() > 0) {
				for (Line line : lines) {
					width = Math.max(width, fm.stringWidth(line.getText()));
				}
				
				height = fm.getHeight() * lines.size();
			}
		}
		
        return new Dimension(width, height);
    }


    /**
     * @return getPreferredSize(c)
     */
    public Dimension getMinimumSize(JComponent c) {
        return getPreferredSize(c);
    }

    /**
     * @return getPreferredSize(c)
     */
    public Dimension getMaximumSize(JComponent c) {
        return getPreferredSize(c);
    }
}
