package com.quirkygaming.nodeframework;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServlet;

public class ResourceLoader {
	
	public static String loadTextResource(HttpServlet servlet, String path, boolean internal) {
		
		String folder = internal ? "/WEB-INF/resources/internal/":"/";
		
		return loadTextResource(servlet, folder+path);
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
