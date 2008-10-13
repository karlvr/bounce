/*
 * $Id: DOMNodeTree.java,v 1.2 2008/05/20 20:19:20 edankert Exp $
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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.bounce.QTree;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

/**
 * A tree representing DOM Nodes.
 *
 * @author Edwin Dankert <edankert@gmail.com>
 */
public class DOMNodeTree extends QTree {
	private static final long serialVersionUID = 5210155085492412876L;
    
	/**
	 * Constructs a tree for a DOM node.
	 * 
	 * @param node the DOM node.
	 */
    public DOMNodeTree(Node node) {
		super(new DefaultTreeModel(new RootTreeNode(node)));
		
		putClientProperty("JTree.lineStyle", "None");
		setCellRenderer(new DOMNodeCellRenderer());
	}
    
    /**
     * The tree-path for the W3C DOM node.
     * 
     * @param node the w3c DOM Node
     * @return the tree-path.
     */
    public TreePath getPathForNode(Node node) {
//    	System.out.println("getPathForNode("+node+")");
    	// create the path ..
    	List<Node> path = new ArrayList<Node>();
    	while (node != null) {
    		path.add(0, node);

    		if (node instanceof Attr) {
    			node = ((Attr)node).getOwnerElement();
    		} else {
    			node = node.getParentNode();
    		}
    	}

    	NodeTreeNode lastNode = (NodeTreeNode)getModel().getRoot();
    	
    	for (Node element : path) {
    		NodeTreeNode treeNode = getNode(lastNode, element);
    		
    		if (treeNode != null) {
        		lastNode = treeNode;
    		}
    	}
		
    	return new TreePath(lastNode.getPath());
    }
    
	public void setRoot(Node node) {
		((DefaultTreeModel)getModel()).setRoot(new RootTreeNode(node));
	}

	public void update() {
		((RootTreeNode)getModel().getRoot()).format();
		((DefaultTreeModel)getModel()).nodeStructureChanged((RootTreeNode)getModel().getRoot());
	}

	/**
	 * Sets the look and feel to the XML Tree UI look and feel.
	 * Override this method to install a different UI.
	 */
	public void updateUI() {
	    setUI(DOMNodeTreeUI.createUI(this));
	}

	@SuppressWarnings("unchecked")
	private NodeTreeNode getNode(NodeTreeNode parent, Node child) {
//    	System.out.println("getNode(("+parent+", "+child+")");
		Enumeration<NodeTreeNode> children = parent.children();
    	
    	while (children.hasMoreElements()) {
    		NodeTreeNode node = children.nextElement();
    		if (node.getNode() == child) {
    			return node;
    		}
    	}
    	
    	return null;
    }
} 
