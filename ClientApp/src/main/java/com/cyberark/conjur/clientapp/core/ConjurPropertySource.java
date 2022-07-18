package com.cyberark.conjur.clientapp.core;

import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import com.cyberark.conjur.api.Conjur;
import com.cyberark.conjur.clientapp.utils.ConjurPropertyLoaderUtil;

/**
 * 
 * This class resolves the secret value for give vault path at application load
 * time from the conjur vault.
 *
 */

public class ConjurPropertySource implements EnvironmentAware {

	private static Logger logger = LoggerFactory.getLogger(ConjurPropertySource.class);

	private static Conjur conjur;

	private static ConjurPropertyLoaderUtil propertyLoader = new ConjurPropertyLoaderUtil();
	Map<String, Object> secretParams = new HashMap<String, Object>();

	@Autowired
	Environment env;

	@Override
	public void setEnvironment(Environment environment) {
		this.env = environment;
	}

	public Environment getEnvironment() {
		return env;
	}

	public ConjurPropertySource() {

	}

	@Autowired
	public void getConjurConnection(@Value("${CONJUR.API_KEY}") String authApiKey,
			@Value("${CONJUR.ACCOUNT}") String account, @Value("${CONJUR.APPLIANCE_URL}") String url,
			@Value("${CONJUR.AUTHN_LOGIN}") String authLogin) throws UnsupportedEncodingException 
	{
		System.out.println("Inside getConjurConnection");
		Map<String, String> conjurParameters = new HashMap<String, String>();
		conjurParameters.put("CONJUR_AUTHN_API_KEY", authApiKey.trim());
		conjurParameters.put("CONJUR_ACCOUNT", account);
		conjurParameters.put("CONJUR_APPLIANCE_URL", url);
		conjurParameters.put("CONJUR_AUTHN_LOGIN", authLogin);

		System.out.println("AuthAPIKEY >>>>" + authApiKey);

		try {
			propertyLoader.loadEnvironmentParameters(conjurParameters);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		//Object secret = getPropertyMethod();
		//System.out.println("Inside getConnection" + secret);

		
	}

	/**
	 * Sets the external environment variable.
	 * 
	 * @param newenv - setting for API_KEY
	 * @throws NoSuchFieldException     -- class doesn't have a field of a specified
	 *                                  name
	 * @throws SecurityException        --indicate a security violation.
	 * @throws IllegalArgumentException -- a method has been passed an illegal or
	 *                                  inappropriate argument.
	 * @throws IllegalAccessException   -- excuting method does not have access to
	 *                                  the definition of the specified class,
	 *                                  field, method or constructor.
	 */

	/**
	 * Method which resolves @value annotation queries and return result in the form
	 * of byte array.
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws CertificateException
	 */

	public Object getPropertyMethod() {

		ConjurConnectionManager.getInstance();
		Map<String, Object> myMap = new HashMap<String, Object>();
		if (null == conjur) {
			conjur = new Conjur();
		}
		String result = null;
		// System.out.println("in there ");

		// System.out.println("print1 -- " +
		// conjur.variables().retrieveSecret("jenkins-app/dbUserName"));
		try {
			// System.out.println("Inside try catch");
			// result = conjur.variables().retrieveSecret("jenkins-app/dbUserName") != null
			// ? conjur.variables().retrieveSecret("jenkins-app/dbUserName").getBytes()
			// : null;
			propertyLoader.readPropertiesFromFile();

			Set<Object> keySet = propertyLoader.getKey();
			Iterator iter = keySet.iterator();
			String[] keys = new String[keySet.size()];
			String value1;
			String key;
			// ConjurPropertySource source = new ConjurPropertySource();
			while (iter.hasNext()) {
				key = (String) iter.next();
				result = conjur.variables().retrieveSecret(key.replace(".", "/"));

				myMap.put(key, result);
				//System.out.println("VAlue " + result);
				// setConjurSecret(myMap);

			}

			// System.out.println("print2 -- " +
			// conjur.variables().retrieveSecret("jenkins-app/dbUserName"));
			//result = conjur.variables().retrieveSecret(key.replace(".", "/"));
			//System.out.println("secrets are -- " + result);
		} catch (Exception e) {
			e.getMessage();
		}
		return myMap;

	}

	private void getVaultkey() {
		propertyLoader.readPropertiesFromFile();

	}

}