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
	
	//public static SubDB<RuntimeException> userContent() {return subDBs.get("userContent");}
	public static SubDB<RuntimeException> cache() {return DB("cache", PropertyDBListener.class.getPackage());}
	public static SubDB<RuntimeException> internal() {return DB("internal", PropertyDBListener.class.getPackage());}
	public static SubDB<RuntimeException> DB(String name, Package owner) {return subDBs.get(dbname(name, owner));}
	public static SubDB<RuntimeException> DB(String qualifiedName) {return subDBs.get(qualifiedName);}
	
	private static String dbname(String name, Package owner) {
		return owner.getName().replaceAll("\\.", "_") + "_" + name;
	}
	
	public static boolean reserveDB(String name, Package owner) {
		name = dbname(name, owner);
		if (!subDBs.containsKey(name)) {
			subDBs.put(name, null);
			return true;
		}
		return false;
	}
	
	public static boolean registerDB(String name, Package owner) { // Reserve and initiate
		boolean created = reserveDB(name, owner);
		if (created) initReservedSubDBs();
		return created;
	}
	
	public static void initReservedSubDBs() {
		for (String key : subDBs.keySet()) {
			if (subDBs.get(key) == null) {
				SubDB<RuntimeException> subDB = new SubDB<>(key, new File("/srv/tomcat/pdb/"), pdb_handler);
				log.println("[" + new Date().toString() + 
						"] Opened "+key+" subdb: " + subDB.getPropertyList().size() + " properties.");
				subDBs.put(key, subDB);
			}
		}
	}
	
	public static Set<String> qualifiedDBNames() {
		return subDBs.keySet();
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
		reserveDB("cache", PropertyDBListener.class.getPackage());
		reserveDB("internal", PropertyDBListener.class.getPackage());
		initReservedSubDBs();
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		if (token != null) {
			PropertyDB.closeDatabase(token);
			log.println("[" + new Date().toString() + "] Closed PropertyDB");
			subDBs.clear();
		}
	}
	
	public static String getLog() {
		return logContent.toString();
	}
}
