package com.cyberark.conjur.clientapp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cyberark.conjur.api.Conjur;


/**
 * 
 * This is the connection creation singleton class with conjur vault by using
 * the conjur java sdk.
 *
 */

public final class ConjurConnectionManager {

	private static ConjurConnectionManager conjurConnectionInstance = null;

	private static Logger logger = LoggerFactory.getLogger(ConjurConnectionManager.class);

	// For Getting Connection with conjur vault using cyberark sdk
	private ConjurConnectionManager()
	{

		getConnection();

	}

	private void getConnection() {

		System.out.println("Inside Connection Manager get Connection");
		

		try {
			
			Conjur conjur = new Conjur();

			logger.info("Connection with Conjur is successful");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * method to create instance of class and checking for multiple threads.
	 * 
	 * @return unique instance of class.
	 * @throws CertificateException
	 */
	public static ConjurConnectionManager getInstance() {
		if (conjurConnectionInstance == null) {
			synchronized (ConjurConnectionManager.class) {
				if (conjurConnectionInstance == null) {
					conjurConnectionInstance = new ConjurConnectionManager();
				}
			}
		}
		return conjurConnectionInstance;
	}
}
