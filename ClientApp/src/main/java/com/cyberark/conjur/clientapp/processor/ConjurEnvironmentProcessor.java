package com.cyberark.conjur.clientapp.processor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import com.cyberark.conjur.clientapp.core.ConjurPropertySource;

public class ConjurEnvironmentProcessor implements EnvironmentPostProcessor{
	
	Map<String, Object> propertySource = new HashMap<>();
    Map<String, Object> secretParams = new HashMap<String, Object>();
    
   // @Autowired
    ConjurPropertySource conjurPropertySource= new ConjurPropertySource();

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		// TODO Auto-generated method stub
		
	    secretParams = (Map<String, Object>) conjurPropertySource.getPropertyMethod();
	 // Create a custom property source with the highest precedence and add it to Spring Environment
	    for (Map.Entry<String, Object> map : secretParams.entrySet()) {
            String key = map.getKey();
            Object value = map.getValue();
            propertySource.put(key, value);
        }
        environment.getPropertySources().addFirst(new MapPropertySource("application", propertySource));


		
	}

}
