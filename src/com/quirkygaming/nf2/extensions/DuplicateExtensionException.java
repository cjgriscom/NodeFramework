package com.quirkygaming.nf2.extensions;

import com.quirkygaming.nf2.WebPage;

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
