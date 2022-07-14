package com.cyberark.conjur.clientapp.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

import com.cyberark.conjur.api.Conjur;
import com.cyberark.conjur.clientapp.processor.ConjurValueProcessor;
import com.cyberark.conjur.clientapp.utils.ConjurPropertyLoaderUtil;

/**
 * 
 * This class resolves the secret value for give vault path at application load
 * time from the conjur vault.
 *
 */

public class ConjurPropertySource{

	private static Logger logger = LoggerFactory.getLogger(ConjurPropertySource.class);

	private Conjur conjur;
	
	
	private ConjurPropertyLoaderUtil propertyLoader = new ConjurPropertyLoaderUtil();

	public ConjurPropertySource() {
		
		System.out.println("Conjur Property Source Called");

	}

	@Autowired
	public void getConjurConnection(@Value("${CONJUR.API_KEY}") String authApiKey,
			@Value("${CONJUR.ACCOUNT}") String account, @Value("${CONJUR.APPLIANCE_URL}") String url,
			@Value("${CONJUR.AUTHN_LOGIN}") String authLogin) throws UnsupportedEncodingException

	{
		Map<String, String> conjurParameters = new HashMap<String, String>();
		conjurParameters.put("CONJUR_AUTHN_API_KEY", authApiKey.trim());
		conjurParameters.put("CONJUR_ACCOUNT", account);
		conjurParameters.put("CONJUR_APPLIANCE_URL", url);
		conjurParameters.put("CONJUR_AUTHN_LOGIN", authLogin);

		System.out.println("AuthAPIKEY >>>>"+authApiKey);
		
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

	public Object getPropertyMethod(String key) {

		ConjurConnectionManager.getInstance();
		if (null == conjur) {
			conjur = new Conjur();
		}
		String result = null;
		//System.out.println("in there ");

		//System.out.println("print1  -- " + conjur.variables().retrieveSecret("jenkins-app/dbUserName"));
		try {
			//System.out.println("Inside try catch");
			// result = conjur.variables().retrieveSecret("jenkins-app/dbUserName") != null
			// ? conjur.variables().retrieveSecret("jenkins-app/dbUserName").getBytes()
			// : null;
			propertyLoader.readPropertiesFromFile();
			result = conjur.variables().retrieveSecret(key);

			//System.out.println("print2  -- " + conjur.variables().retrieveSecret("jenkins-app/dbUserName"));
			System.out.println("secrets are -- " + result);
		} catch (Exception e) {
			e.getMessage();
		}
		return result;

	}

	

}