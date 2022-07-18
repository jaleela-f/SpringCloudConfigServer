package com.cyberark.conjur.cloudconfig.configclient;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.loader.JarLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * 
 * This class resolves the secret value for give vault path at application load
 * time from the conjur vault.
 *
 */

public class CloudConfigClient extends JarLauncher {

	private static Logger logger = LoggerFactory.getLogger(CloudConfigClient.class);



	public CloudConfigClient() 
	{
		System.out.println("Cloud Config Client Constructor called");
	}

	@EventListener(ApplicationReadyEvent.class)
	public void getConjurConnection(@Value("${CONJUR.API_KEY}") String authApiKey,
			@Value("${CONJUR.ACCOUNT}") String account, @Value("${CONJUR.APPLIANCE_URL}") String url,
			@Value("${CONJUR.AUTHN_LOGIN}") String authLogin) throws UnsupportedEncodingException

	{
		
		System.out.println("Value from Config server>>"+ authApiKey+","+account+","+url+","+authLogin);
		Map<String, String> conjurParameters = new HashMap<String, String>();
		conjurParameters.put("CONJUR_AUTHN_API_KEY", authApiKey);
		conjurParameters.put("CONJUR_ACCOUNT", account);
		conjurParameters.put("CONJUR_APPLIANCE_URL", url);
		conjurParameters.put("CONJUR_AUTHN_LOGIN", authLogin);

		try {
			loadEnvironmentParameters(conjurParameters);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
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
		System.out.println("calling inside vault" + newenv);

		@SuppressWarnings("rawtypes")
		Class[] classes = Collections.class.getDeclaredClasses();
		Map<String, String> env = System.getenv();
		for (@SuppressWarnings("rawtypes") Class cl : classes) {
			if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
				Field field = cl.getDeclaredField("m");
				field.setAccessible(true);
				Object obj = field.get(env);
				@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) obj;
				map.putAll(newenv);

			}
		}
	}

	 public static void main(String[] args)  {
	        new CloudConfigClient().launch(args);
	    }

}