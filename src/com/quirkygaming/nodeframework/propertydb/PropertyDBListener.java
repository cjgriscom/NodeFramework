package com.quirkygaming.nodeframework.propertydb;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.quirkygaming.errorlib.ErrorHandler;
import com.quirkygaming.propertydb.InitializationToken;
import com.quirkygaming.propertydb.PropertyDB;
import com.quirkygaming.propertydb.sublayer.SubDB;

/**
 * Application Lifecycle Listener implementation class PropertyDBListener
 *
 */
@WebListener
public class PropertyDBListener implements ServletContextListener {
	private static int PERIOD = 10000;
	
	public static InitializationToken token = null;
	
	private static ByteArrayOutputStream logContent = new ByteArrayOutputStream();
	public static PrintStream log = new PrintStream(logContent);
	
	public static ErrorHandler<RuntimeException> pdb_handler = ErrorHandler.logAll(log, true);
	
	public static SubDB<RuntimeException> varcontent;
	
	/**
	 * Default constructor.
	 */
	public PropertyDBListener() {

	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		log.println("[" + new Date().toString() + 
				"] Initializing PropertyDB with a period of " + PERIOD + " milliseconds.");
		token = PropertyDB.initializeDB(PERIOD);
		varcontent = new SubDB<>("varcontent", new File("/srv/tomcat/pdb/"), pdb_handler);
		log.println("[" + new Date().toString() + 
				"] Opened varcontent subdb: " + varcontent.getPropertyList().size() + " properties.");
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		PropertyDB.closeDatabase(token);
		log.println("[" + new Date().toString() + "] Closed PropertyDB");
	}
	
	public static String getLog() {
		return logContent.toString();
	}
}
