package com.cyberark.conjur.clientapp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.ResourceUtils;

public class ConjurPropertyLoaderUtil {

	private Properties conjurProps = new Properties();
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	Environment env;

	public void readPropertiesFromFile() {
		try {
			InputStream resourceStream = this.getClass().getClassLoader().getResourceAsStream("conjur.properties");
			conjurProps.load(resourceStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Set<Object> getKey()
	{
		return conjurProps.keySet();
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

	public void loadSystemEnvironmentParameter(Map<String, String> params) {
		FileInputStream reader = null;
        FileOutputStream writer = null;

        File file = new File("src/main/resources/conjur.properties");

        try {
            reader = new FileInputStream(file);
            writer = new FileOutputStream(file);

            Properties p = new Properties();
            p.load(reader);
            
          for(Map.Entry<String, String> kv : params.entrySet())
          {
        	  p.setProperty(kv.getKey(), kv.getValue());
          }
           
            p.store(writer,"write a file");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        	try {
				reader.close();
				writer.close();	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
       // MutuableProperty
    }
	

}
