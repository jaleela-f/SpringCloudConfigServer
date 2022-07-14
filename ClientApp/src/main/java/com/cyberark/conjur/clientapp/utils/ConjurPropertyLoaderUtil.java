package com.cyberark.conjur.clientapp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ConjurPropertyLoaderUtil {

	private final Properties conjurProps = new Properties();

	public void readPropertiesFromFile() {
		try {
			InputStream resourceStream = this.getClass().getClassLoader().getResourceAsStream("conjur.properties");
			conjurProps.load(resourceStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getProperty(String key) {
		return conjurProps.getProperty(key);
	}

	public boolean containsKey(String key) {
		return conjurProps.containsKey(key);
	}

	public List<String> getAllProperties() {
		List<String> propertyList = new ArrayList<String>();
		for (Object key : conjurProps.keySet()) {
			System.out.println(key + ": " + conjurProps.getProperty(key.toString()));
			propertyList.add(key.toString());
		}
		return propertyList;
	}

	public void loadEnvironmentParameters(Map<String, String> newenv)
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

	public void loadSystemEnvironmentParameter(Map<String, String> params) {
		final Map<String, String> updatedParam = new HashMap<String, String>();
		Properties props = new Properties();
	

		for (Map.Entry<String, String> entry : params.entrySet()) {
			//System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			
			props.setProperty(entry.getKey(), entry.getValue());

		}
		System.setProperties(props);
		
	}

}
