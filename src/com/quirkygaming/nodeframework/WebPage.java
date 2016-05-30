package com.quirkygaming.nodeframework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServlet;

import com.quirkygaming.nodeframework.extensions.DuplicateExtensionException;
import com.quirkygaming.nodeframework.extensions.Extension;
import com.quirkygaming.nodeframework.extensions.SingleInstanceExtension;

public abstract class WebPage extends ContentHandler {
	
	private String rawContent;
	
	protected HttpServlet servlet;
	
	protected boolean SHOW_REMAINING_NODES;
	
	private HashMap<String, Extension> extMap = new HashMap<String, Extension>();
	
	public WebPage(HttpServlet servlet) {
		this(servlet, false);
	}
	
	public WebPage(HttpServlet servlet, boolean showRemainingNodes) {
		this.servlet = servlet;
		this.SHOW_REMAINING_NODES = showRemainingNodes;
		
		rawContent = baseContent();
	}
	
	public abstract String getID(); // The page should have a recognizable ID for debugging, like a title.
	public abstract String baseContent();
	public abstract void preExtensionProcessing();
	public abstract void postExtensionProcessing();
	
	public WebPage getWebPage() {
		return this;
	}
	
	public Extension getExtensionByName(String name) {
		if (extMap.containsKey(name)) return extMap.get(name);
		return null;
	}
	
	protected void registerExtension(Extension ext) {
		// Extensions are added on construction through this method
		checkPlacement(ext);
		extMap.put(ext.getName(), ext);
	}
	
	public void removeExtension(String name) {
		extMap.remove(name);
	}
	
	public void removeExtension(Extension ext) {
		extMap.values().remove(ext);
		// According to JavaDoc this is safe; the HashMap is backed by the collection and vice-versa
	}
	
	public String getHtml() {
		// Perform all operations, remove remaining nodes, return
		
		content = rawContent;
		
		preExtensionProcessing();
		
		applyExtensions();
		
		postExtensionProcessing();
		
		if (!SHOW_REMAINING_NODES) content = content.replaceAll("%.*?%", ""); 
		
		return content; 
	}
	
	private void applyExtensions() {
		
		List<Extension> extensions = checkAndOrganizeExtensions();
		
		for (Extension e : extensions) {
			System.out.println(e.getName() + ": " + e.getPriority().getPower()); //TODO remove in the future
			ContentHandler c = e;				// Use content handler for visibility
			c.componentsPipe(content); 	// Add current working copy of content
			e.performOperations();
			content = c.getFinalContent();
		}
	}
	
	private List<Extension> checkAndOrganizeExtensions() {
		List<Extension> extensions = new ArrayList<Extension>(extMap.values());
		
		Collections.sort(extensions); // Sort by priority
		checkDuplicateExts(extensions);
		return extensions;
	}
	
	private void checkPlacement(Extension newExt) {
		List<Extension> extensions = new ArrayList<Extension>(extMap.values());
		extensions.add(newExt);
		
		checkDuplicateExts(extensions);
	}
	
	
	private void checkDuplicateExts(List<Extension> source) {
		Set<String> set = new HashSet<String>();
		
		for (Extension e : source) {
			if (!set.add(e.getName()) && e instanceof SingleInstanceExtension) { // If a duplicate...
				throw new DuplicateExtensionException(e.getClass().getSimpleName(), this);
			}
		}
	}

}
