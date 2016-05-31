package com.quirkygaming.nodeframework.extensions.samples;

import com.quirkygaming.nodeframework.WebPage;
import com.quirkygaming.nodeframework.extensions.ExtOrder;
import com.quirkygaming.nodeframework.extensions.Extension;

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
