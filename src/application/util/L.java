package application.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class L {
	private static final boolean DEBUG = true;
	private static Logger logger = Logger.getLogger(L.class.getSimpleName());

	public static void d(String msg,Object [] params){
		if(DEBUG){
			logger.log(Level.INFO, msg, params);
		}
	}

	public static void w(String msg,Object [] params){
		if(DEBUG){
			logger.log(Level.WARNING, msg, params);
		}
	}


	public static void e(String msg,Object [] params){
		if(DEBUG){
			logger.log(Level.SEVERE, msg, params);
		}
	}
}
