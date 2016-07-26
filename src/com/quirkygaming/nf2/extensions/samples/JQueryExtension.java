package com.quirkygaming.nf2.extensions.samples;

import com.quirkygaming.nf2.WebPage;
import com.quirkygaming.nf2.extensions.ExtOrder;
import com.quirkygaming.nf2.extensions.Extension;

public class JQueryExtension extends Extension {
	
	static String jq = "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js\"></script>";
	static int id = 0;
	
	public JQueryExtension(WebPage owner) {
		super(owner, "jQuery"+(id++), ExtOrder.NORMAL);
	}

	@Override
	public boolean performOperations() {
		if (!nodeExists("JQUERY_EXT")) {
			this.appendNodeToNode("METADATA", "JQUERY");
			
			this.replaceNode("JQUERY", jq);
			
			this.appendNodeToNode("METADATA", "JQUERY_EXT"); // Add existence notifier in case of duplicates
		}
		return true;
	}

}
