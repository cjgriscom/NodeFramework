package com.quirkygaming.nodeframework.extensions;

import com.quirkygaming.nodeframework.ContentHandler;
import com.quirkygaming.nodeframework.WebPage;

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
