package com.quirkygaming.nodeframework.extensions;

import com.quirkygaming.nodeframework.WebPage;

public abstract class SingleInstanceExtension extends Extension {
	
	// Only use when an extension MUST only be used once.
	
	public SingleInstanceExtension(WebPage owner, ExtOrder priority) {
		super(owner, "", priority);
	}

	public abstract String getStaticName(); // A name that can/does not change from extension to extension.
	                                        // For single instance verification purposes.
	@Override
	public void setName(String name) {
		return;
	}
	
	@Override
	public String getName() {
		return getStaticName();
	}
	
}
