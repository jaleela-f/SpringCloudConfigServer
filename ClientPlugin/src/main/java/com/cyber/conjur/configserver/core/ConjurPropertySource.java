package com.cyber.conjur.configserver.core;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.EnumerablePropertySource;

import com.cyberark.conjur.api.Conjur;

/**
 * 
 * This class resolves the secret value for give vault path at application load
 * time from the conjur vault.
 *
 */


public class ConjurPropertySource {

	private static Logger logger = LoggerFactory.getLogger(ConjurPropertySource.class);

	private Conjur conjur;
	private String vaultInfo = "";
	private String vaultPath = "";


	public ConjurPropertySource() 
	{

	}

	@Autowired
	public void getConjurConnection(@Value("${CONJUR.ACCOUNT}") String account,@Value("${CONJUR.APPLIANCE_URL}") String url,
			@Value("${CONJUR.AUTHN_LOGIN}") String authLogin,
			@Value("${CONJUR.API_KEY}") String authApiKey
			) throws UnsupportedEncodingException

	{
		Map<String, String> conjurParameters = new HashMap<String, String>();
		conjurParameters.put("CONJUR_ACCOUNT", account);
		conjurParameters.put("CONJUR_APPLIANCE_URL", url);
		conjurParameters.put("CONJUR_AUTHN_LOGIN", authLogin);
		conjurParameters.put("CONJUR_AUTHN_API_KEY", authApiKey.trim());
		
		System.out.println("Auth API Key value"+authApiKey);
		System.out.println("Account>>>"+account);
		System.out.println("URl>>>"+url);
		System.out.println("Login>>>"+authLogin);
		
		try {
			loadEnvironmentParameters(conjurParameters);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		Object secret=getPropertyMethod();
		System.out.println("Inside getConnection"+secret);
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
	public static void loadEnvironmentParameters(Map<String, String> newenv)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		// System.out.println("calling inside vault" + newenv);

		Class[] classes = Collections.class.getDeclaredClasses();
		Map<String, String> env = System.getenv();
		for (Class cl : classes) {
			if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
				Field field = cl.getDeclaredField("m");
				field.setAccessible(true);
				Object obj = field.get(env);
				Map<String, String> map = (Map<String, String>) obj;
				map.putAll(newenv);

			}
		}
	}

	/**
	 * Method which resolves @value annotation queries and return result in the form
	 * of byte array.
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws CertificateException
	 */
	
	public Object getPropertyMethod() {
		
		
		ConjurConnectionManager.getInstance();
		if (null == conjur) {
			conjur = new Conjur();
		}
		String result = null;
		System.out.println("in there ");
		
		System.out.println("print1  -- "+conjur.variables().retrieveSecret("jenkins-app/dbUserName"));
		try {
			System.out.println("Inside try catch");
			//result = conjur.variables().retrieveSecret("jenkins-app/dbUserName") != null
			//		? conjur.variables().retrieveSecret("jenkins-app/dbUserName").getBytes()
			//		: null;
			
			result = conjur.variables().retrieveSecret("jenkins-app/dbUserName");
			
			System.out.println("print2  -- "+conjur.variables().retrieveSecret("jenkins-app/dbUserName"));
			System.out.println("secrets are -- "+result);
		} catch (Exception e) {
			e.getMessage();
		}
		return result;
		
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(conjur, vaultInfo, vaultPath);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConjurPropertySource other = (ConjurPropertySource) obj;
		return Objects.equals(conjur, other.conjur) && Objects.equals(vaultInfo, other.vaultInfo)
				&& Objects.equals(vaultPath, other.vaultPath);
	}

}