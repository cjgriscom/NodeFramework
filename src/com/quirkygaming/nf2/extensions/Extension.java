package com.quirkygaming.nf2.extensions;

import com.quirkygaming.nf2.ContentHandler;
import com.quirkygaming.nf2.WebPage;
import com.quirkygaming.nf2.extensions.ExtOrder;
import com.quirkygaming.nf2.extensions.Extension;

public abstract class Extension extends ContentHandler implements Comparable<Extension> {
	
	protected ExtOrder priority;
	protected String name;
	private WebPage owner;
	
	public Extension(WebPage owner, String name, ExtOrder priority) {
		this.priority = priority;
		this.name = name;
		this.owner = owner;
		this.register(owner, this);
	}
	
	public WebPage getWebPage() {
		return owner;
	}
	
	public void unregister() {
		owner.removeExtension(this);
	}
	
	public abstract boolean performOperations();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ExtOrder getPriority() {
		return priority;
	}
	public void setPriority(ExtOrder priority) {
		this.priority = priority;
	}

	//@Override
	public int compareTo(Extension o) {
		return o.getPriority().getPower() - getPriority().getPower();
	}
}
