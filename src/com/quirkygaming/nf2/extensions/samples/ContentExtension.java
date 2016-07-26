package com.quirkygaming.nf2.extensions.samples;

import com.quirkygaming.nf2.WebPage;
import com.quirkygaming.nf2.extensions.ExtOrder;
import com.quirkygaming.nf2.extensions.Extension;

public class ContentExtension extends Extension implements Content {
	
	private String placementNode;
	private String content;
	
	public ContentExtension(String name, WebPage owner, String placementNode, String content) {
		super(owner, name, ExtOrder.VERY_LOW);
		
		this.placementNode = placementNode;
		this.content = content;
		
		
	}
	
	@Override
	public boolean performOperations() {
		this.replaceNode(placementNode, content);
		
		return true;
	}

	//@Override
	public String getPlacementNode() {
		return this.placementNode;
	}

	//@Override
	public void setPlacementNode(String placementNodeName) {
		this.placementNode = placementNodeName;
	}

}
