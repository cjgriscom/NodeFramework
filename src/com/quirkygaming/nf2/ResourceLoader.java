package com.quirkygaming.nf2;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServlet;

import com.quirkygaming.nf2.propertydb.PropertyDBListener;

public class ResourceLoader {
	
	private static String loadInternalDirect(HttpServlet servlet, String path) {
		return loadTextResource(servlet, "/WEB-INF/resources/internal/" + path);
	}
	
	public static String loadTextResource(HttpServlet servlet, String path, final boolean internal) {
		return loadTextResource(servlet, path, internal, false);
	}
	
	public static String loadTextResource(HttpServlet servlet, String path, boolean internal, boolean forceDirect) {
		if (internal && forceDirect) return loadInternalDirect(servlet, path);
		if (internal) {
			String sanitizedName = path.replaceAll("\\.", "_").replaceAll("/", "_");
			String initialValue = null;
			if (!PropertyDBListener.internal().propertyExists(sanitizedName)) {
				initialValue = loadInternalDirect(servlet, path);
			}
			if (PropertyDBListener.internal().isLoaded(sanitizedName)) {
				// Already loaded; we don't want to close it (someone is editing it)
				return (String) PropertyDBListener.internal().getLoadedProperty(sanitizedName).get();
			} else {
				// Load then unload; not being used elsewhere
				return PropertyDBListener.internal().getAndCloseLoadedProperty(sanitizedName, 1, initialValue);
			}
		} else {
			// Public
			return loadTextResource(servlet, "/" + path);
		}
	}
	
	public static String loadTextResource(HttpServlet servlet, String fullpath) {
		
		try {
			InputStream stream = servlet.getServletContext().getResourceAsStream(fullpath);
			
			java.util.Scanner s = new java.util.Scanner(stream);
			s.useDelimiter("\\A");

			String data = s.hasNext() ? s.next() : "";
			
			try {
				stream.close();
			} catch (IOException e) {}
			s.close();
		
			return data;
		
		} catch (NullPointerException e) {
			return invalidRequest(); 
		}
	}
	
	private static String invalidRequest() {
		return "Invalid file request. ";
	}


}
