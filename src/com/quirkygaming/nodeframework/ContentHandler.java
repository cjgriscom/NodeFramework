package com.quirkygaming.nodeframework;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.quirkygaming.nodeframework.extensions.Extension;

public abstract class ContentHandler {
	
	String content;
	
	void componentsPipe(String content) {
		this.content = content;
	}
	
	String getFinalContent() {
		return content;
	}
	
	protected abstract WebPage getWebPage();
	
	public String loadInternalFile(String filename) {
		return ResourceLoader.loadTextResource(getWebPage().servlet, filename, true);
	}
	
	public String loadPublicFile(String filename) {
		return ResourceLoader.loadTextResource(getWebPage().servlet, filename, false);
	}
	
	protected Extension getExtensionByName(String name) {
		return getWebPage().getExtensionByName(name);
	}
	
	protected void register(WebPage owner, Extension self) {
		owner.registerExtension(self);
	}
	
	protected boolean nodeExists(String node) {
		return ContentHandler.nodeExists(content, node);
	}
	
	protected void appendNodeToNode(String existingNode, String appendedNode) {
		content = ContentHandler.appendNodeToNode(content, existingNode, appendedNode);
	}
	
	protected void unlockNode(String nodeName, boolean unlockAllNodes) {
		content = ContentHandler.unlockNode(content, nodeName, unlockAllNodes);
	}
	
	protected void replaceNode(String nodeName, String data) {
		content = ContentHandler.replaceNode(content, nodeName, data);
	}
	
	protected void replaceAllNodes(String nodeName, String data) {
		content = ContentHandler.replaceNode(content, nodeName, data, true);
	}
	
	protected static boolean nodeExists(String content, String node) {
		Matcher matcher = Pattern.compile("%" + node + "%").matcher(content);
		return matcher.find();
	}
	
	protected static String appendNodeToNode(String content, String existingNode, String appendedNode) {
		existingNode = "%"+existingNode+"%";
		appendedNode = "%"+appendedNode+"%";
		return content.replaceAll(
				existingNode, 
				existingNode + "\n" + getWhitespace(content, existingNode) + appendedNode);
	}
	
	protected static String unlockNode(String content, String nodeName, boolean unlockAllNodes) {
		// For example:
		// will change %JAVASCRIPT_TAG<script language="javascript" type="text/javascript">%
		//          to <script language="javascript" type="text/javascript">
		// to avoid deletion
		
		String nodePrefix = "%"+nodeName;
		String nodeSuffix = "%";
		
		Pattern pattern = Pattern.compile(nodePrefix + ".*?" + nodeSuffix);
		
		Matcher matcher;
		
		while ((matcher = pattern.matcher(content)).find()) { // New matcher for each time b/c content changes
			String match = matcher.group();
			content = content.substring(0, matcher.start()) + 
					match.substring(nodePrefix.length(), match.length() - nodeSuffix.length()) +
					content.substring(matcher.end());
			if (!unlockAllNodes) return content;
		}
		return content;
	}
	
	protected static String replaceNode(String content, String nodeName, String data) {
		return replaceNode(content, nodeName, data, false);
	}
	
	protected static String replaceNode(String content, String nodeName, String data, boolean allNodes) {
		// To replace %TITLE% with Home, for example, call replaceNode("TITLE", "Home").
		
		Pattern pattern = Pattern.compile("%"+nodeName+"%");
		Matcher matcher;
		
		if (!data.contains("%PREFORMAT%")) { // Add the %PREFORMAT% node to prevent whitespacing
			data = data.replaceAll("\n", "\n" + getWhitespace(content, "%"+nodeName+"%"));
			//Format with whitespace added
		}
		
		while ((matcher = pattern.matcher(content)).find()) { // New matcher for each time b/c content changes
			content = content.substring(0, matcher.start()) + 
					data +
					content.substring(matcher.end());
			
			if (!allNodes) return content;
		}
		return content;
	}
	
	private static String getWhitespace(String content, String node) {
		// Figure out the contents of the whitespace in front of a line
		int end = content.indexOf(node);
		if (end < 0) return "";
		int start = content.substring(0, end).lastIndexOf("\n") + 1;
		
		String whitespace = content.substring(start, end);
				
		Matcher matcher = Pattern.compile("^[ \t]+").matcher(whitespace);
		return matcher.find() ? matcher.group() : "";
	}

}
