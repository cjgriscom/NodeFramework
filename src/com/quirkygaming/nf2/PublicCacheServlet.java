package com.quirkygaming.nf2;

import static com.quirkygaming.nf2.propertydb.PropertyDBListener.cache;

import java.util.List;

import javax.servlet.http.HttpServlet;

public abstract class PublicCacheServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	protected abstract String buildPage(String pageName);
	
	public void cachePage(String pageName, String content) {
		cache().getOrInitiateProperty(pageName, 1, content).set(content);
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
		return cache().<String>getLoadedProperty(pageName).get();
	}
	
	public static void removeCachedPage(String pageName) {
		cache().deleteProperty(pageName);
	}
	
	public static boolean containsCachedPage(String pageName) {
		return cache().isLoaded(pageName);
	}
	
	public static List<String> getCacheList() {
		return cache().getPropertyList();
	}
	
}
