package com.quirkygaming.nf2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;

import com.quirkygaming.nf2.extensions.Extension;
import com.quirkygaming.nf2.ResourceLoader;
import com.quirkygaming.nf2.WebPage;

public abstract class ContentHandler {
	static Pattern locks = Pattern.compile("<!--\\^.*?\\^-->", Pattern.DOTALL);
	
	String content;
	
	void componentsPipe(String content) {
		this.content = content;
	}
	
	String getFinalContent() {
		return content;
	}
	
	protected abstract WebPage getWebPage();
	

	public static String loadInternalFile(HttpServlet srv, String filename) {
		return ResourceLoader.loadTextResource(srv, filename, true);
	}
	
	public String loadPublicFile(HttpServlet srv, String filename) {
		return ResourceLoader.loadTextResource(srv, filename, false);
	}
	
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
	
	public static String removeNodesFromContent(String content, boolean debugNodes) {
		if (!debugNodes) {
			return locks.matcher(content.replaceAll("<!--%.*?%-->", "")).replaceAll("");
		}
		return content;
	}
	
	public boolean nodeExists(String node) {
		return ContentHandler.nodeExists(content, node);
	}
	
	public void appendNodeToNode(String existingNode, String appendedNode) {
		content = ContentHandler.appendNodeToNode(content, existingNode, appendedNode);
	}
	
	public void prependNodeToNode(String existingNode, String prependedNode) {
		content = ContentHandler.prependNodeToNode(content, existingNode, prependedNode);
	}
	
	public void deleteLockNodeAndContents(String nodeName, boolean deleteAllNodes) {
		content = ContentHandler.deleteLockNodeAndContents(content, nodeName, deleteAllNodes);
	}
	
	public void unlockNode(String nodeName, boolean unlockAllNodes) {
		content = ContentHandler.unlockNode(content, nodeName, unlockAllNodes);
	}
	
	public void replaceNode(String nodeName, String data) {
		content = ContentHandler.replaceNode(content, nodeName, data);
	}
	
	public void replaceAllNodes(String nodeName, String data) {
		content = ContentHandler.replaceNode(content, nodeName, data, true);
	}
	
	public void deleteNode(String nodeName) {
		content = ContentHandler.deleteNode(content, nodeName);
	}
	
	public void deleteAllNodes(String nodeName) {
		content = ContentHandler.deleteNode(content, nodeName, true);
	}
	
	public void renameNode(String nodeName, String newNode) {
		content = ContentHandler.renameNode(content, nodeName, newNode);
	}
	
	public void renameAllNodes(String nodeName, String newNode) {
		content = ContentHandler.renameNode(content, nodeName, newNode, true);
	}
	
	public static boolean nodeExists(String content, String node) {
		Matcher matcher = Pattern.compile("<!--%" + node + "%-->").matcher(content);
		return matcher.find();
	}
	
	public static String getNode(String nodeName) {
		return "<!--%" + nodeName + "%-->";
	}
	
	public static String appendNodeToNode(String content, String existingNode, String appendedNode) {
		existingNode = getNode(existingNode);
		appendedNode = getNode(appendedNode);
		return content.replaceAll(
				existingNode, 
				existingNode + "\n" + getWhitespace(content, existingNode) + appendedNode);
	}
	
	public static String prependNodeToNode(String content, String existingNode, String prependedNode) {
		existingNode = getNode(existingNode);
		prependedNode = getNode(prependedNode);
		return content.replaceAll(
				existingNode, 
				prependedNode + "\n" + getWhitespace(content, existingNode) + existingNode);
	}
	
	public static String deleteLockNodeAndContents(String content, String nodeName, boolean unlockAllNodes) {
		// For example:
		// will change %JAVASCRIPT_TAG<script language="javascript" type="text/javascript">%
		//          to <script language="javascript" type="text/javascript">
		// to avoid deletion
		
		String nodePrefix = "<!--\\^"+nodeName;
		String nodeSuffix = nodeName+"\\^-->";
		
		Pattern pattern = Pattern.compile(nodePrefix + ".*?" + nodeSuffix);
		
		Matcher matcher;
		
		while ((matcher = pattern.matcher(content)).find()) { // New matcher for each time b/c content changes
			content = content.substring(0, matcher.start()) + 
					content.substring(matcher.end());
			if (!unlockAllNodes) return content;
		}
		return content;
	}
	
	// Syntax:
	// <!--^NodeName-->lockedCONTENT<--NodeName^-->
	// OR
	// <!--^NodeName>lockedCONTENT<NodeName^-->
	public static String unlockNode(String content, String nodeName, boolean unlockAllNodes) {
		// For example:
		// will change %JAVASCRIPT_TAG<script language="javascript" type="text/javascript">%
		//          to <script language="javascript" type="text/javascript">
		// to avoid deletion
		
		return 	replaceNodeRAW(
				replaceNodeRAW(
				replaceNodeRAW(
				replaceNodeRAW(content, 
						"<!--\\^"+nodeName+">", getNode(nodeName), unlockAllNodes), 
						"<"+nodeName+"\\^-->", getNode(nodeName), unlockAllNodes),
						"<!--\\^"+nodeName+"-->", getNode(nodeName), unlockAllNodes), 
						"<!--"+nodeName+"\\^-->", getNode(nodeName), unlockAllNodes);
	}
	
	public static String replaceNode(String content, String nodeName, String data) {
		return replaceNode(content, nodeName, data, false);
	}
	
	public static String replaceNode(String content, String nodeName, String data, boolean allNodes) {
		// To replace %TITLE% with Home, for example, call replaceNode("TITLE", "Home").
		
		return replaceNodeRAW(content, getNode(nodeName), data, allNodes);
		
		/*Pattern pattern = Pattern.compile(getNode(nodeName));
		Matcher matcher;
		
		if (!data.contains(getNode("PREFORMAT"))) { // Add the %PREFORMAT% node to prevent whitespacing
			data = data.replaceAll("\n", "\n" + getWhitespace(content, getNode(nodeName)));
			//Format with whitespace added
		}
		
		while ((matcher = pattern.matcher(content)).find()) { // New matcher for each time b/c content changes
			content = content.substring(0, matcher.start()) + 
					data +
					content.substring(matcher.end());
			
			if (!allNodes) return content;
		}
		return content;*/ //TODO
	}
	
	public static String replaceNodeRAW(String content, String raw, String repl, boolean all) {
		Pattern pattern = Pattern.compile(raw);
		Matcher matcher;
		
		if (!repl.contains(getNode("PREFORMAT"))) { // Add the %PREFORMAT% node to prevent whitespacing
			repl = repl.replaceAll("\n", "\n" + getWhitespace(content, raw));
			//Format with whitespace added
		}
		
		while ((matcher = pattern.matcher(content)).find()) { // New matcher for each time b/c content changes
			content = content.substring(0, matcher.start()) + 
					repl +
					content.substring(matcher.end());
			
			if (!all) return content;
		}
		return content;
	}

	public static String deleteNode(String content, String nodeName) {
		return deleteNode(content, nodeName, false);
	}
	
	public static String deleteNode(String content, String nodeName, boolean allNodes) {
		return replaceNode(content, nodeName, "", allNodes);
	}

	public static String renameNode(String content, String nodeName, String newName) {
		return renameNode(content, nodeName, newName, false);
	}
	
	public static String renameNode(String content, String nodeName, String newName, boolean allNodes) {
		return replaceNode(content, nodeName, getNode(newName), allNodes);
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
