package com.quirkygaming.nf2.propertydb;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

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
	
	private static HashMap<String, SubDB<RuntimeException>> subDBs = new HashMap<>();
	static {
		subDBs.put("userContent", null);
		subDBs.put("cache", null);
		subDBs.put("internal", null);
	}
	
	public static SubDB<RuntimeException> userContent() {return subDBs.get("userContent");}
	public static SubDB<RuntimeException> cache() {return subDBs.get("cache");}
	public static SubDB<RuntimeException> internal() {return subDBs.get("internal");}
	public static SubDB<RuntimeException> DB(String name) {return subDBs.get(name);}
	
	public static void initSubDBs() {
		for (String key : subDBs.keySet()) {
			if (subDBs.get(key) == null) {
				SubDB<RuntimeException> subDB = new SubDB<>("com_quirkygaming_custompuzzles_"+key, new File("/srv/tomcat/pdb/"), pdb_handler);
				log.println("[" + new Date().toString() + 
						"] Opened "+key+" subdb: " + subDB.getPropertyList().size() + " properties.");
				subDBs.put(key, subDB);
			}
		}
	}
	
	/**
	 * Default constructor.
	 */
	public PropertyDBListener() {

	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		if (PropertyDB.initialized()) {
			log.println("[" + new Date().toString() + 
					"] PropertyDB is already initialized!!.");
		} else {
			log.println("[" + new Date().toString() + 
					"] Initializing PropertyDB with a period of " + PERIOD + " milliseconds.");
			token = PropertyDB.initializeDB(PERIOD);
		}
		
		initSubDBs();
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		if (token != null) {
			PropertyDB.closeDatabase(token);
			log.println("[" + new Date().toString() + "] Closed PropertyDB");
		}
	}
	
	public static String getLog() {
		return logContent.toString();
	}
	public static Set<String> DBs() {
		return subDBs.keySet();
	}
}
