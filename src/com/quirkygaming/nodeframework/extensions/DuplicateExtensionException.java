package com.quirkygaming.nodeframework.extensions;

import com.quirkygaming.nodeframework.WebPage;

public class DuplicateExtensionException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public DuplicateExtensionException(String classname, WebPage page) {
		super("A SingleInstanceExtension collision occured between two " 
				+ classname 
				+ " objects on a webpage with the title \"" 
				+ page.getID() 
				+ "\".");
	}

}
