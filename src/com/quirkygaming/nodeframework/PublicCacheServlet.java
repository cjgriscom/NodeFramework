package com.quirkygaming.nodeframework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServlet;

public abstract class PublicCacheServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static HashMap<String, String> cache = new HashMap<String, String>();
	
	protected abstract String buildPage(String pageName);
	
	public void cachePage(String pageName, String content) {
		cache.put(pageName, content);
	}
	
	public String getPage(String pageName) {
		if (containsCachedPage(pageName)) {
			return getCachedPage(pageName);
		} else {
			String page = buildPage(pageName);
			cachePage(pageName, page);
			return page;
		}
	}
	
	public static String getCachedPage(String pageName) {
		return cache.get(pageName);
	}
	
	public static void removeCachedPage(String pageName) {
		cache.remove(pageName);
	}
	
	public static boolean containsCachedPage(String pageName) {
		return cache.containsKey(pageName);
	}
	
	public static List<String> getCacheList() {
		return new ArrayList<String>(cache.keySet());
	}
	
}
