package com.quirkygaming.nf2.propertydb;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class PropertyDBListener
 *
 */
@WebListener
public class ContextManager implements ServletContextListener {
	private static ServletContext context = null; 
	
	/**
	 * Default constructor.
	 */
	public ContextManager() {

	}
	
	public static boolean isContextInitialized() {
		return context != null;
	}
	
	public static URL getResource(String resource) throws MalformedURLException {
		if (context == null) {
			return null;
		} else {
			return context.getResource(resource);
		}
	}
	
	public static InputStream getResourceAsStream(String resource) {
		if (context == null) {
			return null;
		} else {
			return context.getResourceAsStream(resource);
		}
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		context = arg0.getServletContext();
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}
}
