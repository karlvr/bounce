/*
 * $Id: DOMNodeTreeUI.java,v 1.1 2008/04/15 20:59:50 edankert Exp $
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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalTreeUI;
import javax.swing.tree.TreePath;

/**
 * The View part for the Xml Tree component.
 *
 * @author Edwin Dankert (edankert@gmail.com)
 */
public class DOMNodeTreeUI extends MetalTreeUI {

    protected static DOMNodeTreeUI treeUI = new DOMNodeTreeUI();

    public static ComponentUI createUI(JComponent c) {
	    return new DOMNodeTreeUI();
    }

    /**
     * Paints the expand (toggle) part of a row. The reciever should
     * NOT modify <code>clipBounds</code>, or <code>insets</code>.
     */
    protected void paintExpandControl(Graphics g,
				      Rectangle clipBounds, Insets insets,
				      Rectangle bounds, TreePath path,
				      int row, boolean isExpanded,
				      boolean hasBeenExpanded,
				      boolean isLeaf) {

		Object value = path.getLastPathComponent();

		// Draw icons if not a leaf and either hasn't been loaded,
		// or the model child count is > 0.
		if (!isLeaf && (!hasBeenExpanded || treeModel.getChildCount(value) > 0)) {
		    int x = bounds.x - (getRightChildIndent() - 1);
		    int y = bounds.y;
			
			Icon icon = null;

		    if ( isExpanded) {
				icon = getExpandedIcon();
		    } else {
				icon = getCollapsedIcon();
		    }

			// Draws the icon horizontally centered at (x,y)
			if (icon != null) {
				icon.paintIcon(tree, g, x - icon.getIconWidth() / 2, y);
			}
		}
    }
	
    protected void paintRow(Graphics g, Rectangle clipBounds,
    		    Insets insets, Rectangle bounds, TreePath path,
    		    int row, boolean isExpanded,
    		    boolean hasBeenExpanded, boolean isLeaf) {
	    // Don't paint the renderer if editing this row.
	    if (editingComponent != null && editingRow == row) {
	        return;
	    }
		
		Object object = path.getLastPathComponent();
		
		Component component = currentCellRenderer.getTreeCellRendererComponent(tree, 
								object, tree.isRowSelected(row), 
								isExpanded, isLeaf, row, false); // hasfocus???

		// don't indent the end-tag as far...
		if ( object instanceof ElementEndTreeNode) {
			int indent = getLeftChildIndent() + getRightChildIndent();
			rendererPane.paintComponent(g, component, tree, bounds.x-indent, bounds.y, bounds.width, bounds.height, true);	
		} else {
		    rendererPane.paintComponent(g, component, tree, bounds.x, bounds.y, bounds.width, bounds.height, true);	
		}
    }

    protected void installDefaults() {
		super.installDefaults();

		setLeftChildIndent(8);
		setRightChildIndent(8);
    }
}
