package com.cyberark.conjur.clientapp.processor;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import com.cyberark.conjur.clientapp.core.ConjurPropertySource;
import com.cyberark.conjur.clientapp.utils.ConjurPropertyLoaderUtil;

public class ConjurValueProcessor implements BeanPostProcessor {

	@Autowired
	Environment env;

	private ConjurPropertyLoaderUtil propertyLoader = new ConjurPropertyLoaderUtil();
	private String newKey = "";

	@Value("${app.name}")
	private String valuePath;

	@Autowired
	private ConjurPropertySource conjurPropertySource;

	private ConjurPorpertyListener propertyListener = new ConjurPorpertyListener();

	Map<String, String> secretParams = new HashMap<String, String>();

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

		String value = "";
		String fieldKey;
		String propValue;

		propertyLoader.readPropertiesFromFile();
		
		if (bean.getClass().getName().equals("com.cyberark.conjur.clientapp.conjurproperty.SecretProperty"))
		{
			Field[] fields = bean.getClass().getDeclaredFields();
			for (Field field : fields) {

				if (field.getAnnotation(Value.class) != null) {

					fieldKey = field.getAnnotation(Value.class).value();

					newKey = getKeyAfterRemoveChar(fieldKey);
					value = checkForPlaceHolderValue(newKey);

					if (value == null || value.isEmpty()) {

						if (propertyLoader.containsKey(newKey)) {
							propValue = propertyLoader.getProperty(newKey).toString();
							//System.out.println("Key >>>>" + newKey);
							value = (String) conjurPropertySource.getPropertyMethod(propValue);

							if (value != null && !value.isEmpty() && !value.isBlank()) {

								secretParams.put(newKey, value);

							}

						}

					}
				}
			}

			//propertyLoader.loadSystemEnvironmentParameter(secretParams);
			try {
				ConfigurableEnvironment configEnv =(ConfigurableEnvironment) this.env;
				String propKey;
				Set<String> envKey= new HashSet(configEnv.getSystemEnvironment().keySet());
				//Set modifiableSet = Collections.unmodifiableSet(envKey);
			
				
				for(Map.Entry<String,String> entry:secretParams.entrySet())
				{
					System.out.println("Key="+entry.getKey()+","+"Value="+entry.getValue());
					propKey= entry.getKey();
					if(envKey.contains(propKey))
					{
						envKey.add(entry.getValue());
					}
					
					//if(propKey.equals)
					
					
					
				}
				
				
				//System.out.println(configEnv.getSystemEnvironment().put(fieldKey, propValue));
				
				propertyLoader.loadEnvironmentParameters(secretParams);
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return bean;
	}

	private String getKeyAfterRemoveChar(String key) {
		newKey = key.replaceAll("[^a-zA-Z0-9\\-\\.\\s+]", "");
		return newKey;
	}

	private String checkForPlaceHolderValue(String key) {

		String isKey = "";

		Map<String, String> properties = System.getenv();
		isKey = properties.get(key);

		return isKey;
	}

}
