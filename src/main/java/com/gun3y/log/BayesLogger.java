package com.gun3y.log;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class BayesLogger extends Logger {

	private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	
	protected BayesLogger(String name, String resourceBundleName) {
		super(name, resourceBundleName);
	}

	public static synchronized Logger getLogger(String name) {
		Logger logger = Logger.getLogger(name);

		logger.setLevel(Level.ALL);
		try {
			FileHandler fhandler = new FileHandler("log-"+dateFormat.format(new Date())+".txt");
			SimpleFormatter sformatter = new SimpleFormatter();
			fhandler.setFormatter(sformatter);
			logger.addHandler(fhandler);

		} catch (IOException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
		} catch (SecurityException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
		}
		
		return logger;

	}

}
