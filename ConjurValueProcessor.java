package com.cyberark.conjur.clientapp.processor;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeansException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;


import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;


import com.cyberark.conjur.clientapp.core.ConjurPropertySource;
import com.cyberark.conjur.clientapp.utils.ConjurPropertyLoaderUtil;

@Configuration
public class ConjurValueProcessor
		implements BeanPostProcessor, InitializingBean, ApplicationContextAware, EnvironmentAware {

	@Autowired
	private ConjurPropertySource propertySource;

	private ConjurPropertyLoaderUtil propertyLoader = new ConjurPropertyLoaderUtil();

	private String newKey = "";

	private static Map<String, Object> secretParams = new HashMap<String, Object>();

	private static Map<String, Object> myMap = new HashMap<String, Object>();

	private ConfigurableEnvironment environment;

	private final String propertySourceName = "classpath:/conjur.properties";

	private ApplicationContext applicationContext;
	private BeanDefinitionRegistry registry;
	private Map<String, Object> systemConfigMap = new HashMap<>();
	private Map<String, Object> newMap = new HashMap<String, Object>();

	@Autowired
	Environment env;

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		// System.out.println("Post Processer bean called"+bean.getClass().getName());
		/*
		 * propertyLoader.readPropertiesFromFile();
		 * 
		 * if (bean.getClass().getName().contains(ClassUtils.CGLIB_CLASS_SEPARATOR) &&
		 * bean.getClass().getSuperclass().getAnnotation(Configuration.class) == null) {
		 * System.out.println("Bean Name>>>" + bean.getClass().getName()); Class<?>
		 * clazz = ClassUtils.getUserClass(bean.getClass());
		 * 
		 * Field[] fields = clazz.getDeclaredFields();
		 * 
		 * int i = 0; Field[] field = new Field[fields.length]; for (Field f : fields) {
		 * field[i] = f; // System.out.println("Field Key"+f+"\n"); i++; } for (Field f1
		 * : field) { f1.setAccessible(true); try {
		 * 
		 * if (f1.get(bean).equals("")) { //secretParams = (Map<String, Object>)
		 * propertySource.getPropertyMethod();
		 * //System.out.println("Secrets from Conjur" + f1.getName());
		 * 
		 * 
		 * 
		 * } } catch (IllegalArgumentException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IllegalAccessException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * 
		 * }
		 * 
		 * }
		 */
		// bind(ConfigurationPropertiesBean.get(this.applicationContext, bean,
		// beanName));
		secretParams = (Map<String, Object>) propertySource.getPropertyMethod();
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

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		environment.getPropertySources().addLast(new MapPropertySource(propertySourceName, systemConfigMap));
		secretParams = (Map<String, Object>) propertySource.getPropertyMethod();
		for (Map.Entry<String, Object> map : secretParams.entrySet()) {
			String key = map.getKey();
			System.out.println("Key>>>" + key);

			Object value = map.getValue();
			System.out.println("VAlue >>>>" + value);
			systemConfigMap.put(key, value);
		}
		System.out.println("Secrets >>>" + systemConfigMap);
		// this.registry = (BeanDefinitionRegistry)
		// this.applicationContext.getAutowireCapableBeanFactory();

		boolean flag = environment.getPropertySources().contains("conjur");
		MutablePropertySources source = environment.getPropertySources();
		for (PropertySource<?> bean : source) {

			String value = (String) bean.getProperty("jenkins-app.dbUserName");
			
			String beanName = bean.getName();
			// System.out.println("Property Source>>>"+beanName);
			if ((beanName.contains("bootstrapProperties-file")) || (beanName.contains("systemProperties")) || (beanName.contains("systemEnvironment")) ||
					(beanName.contains("random")) ||  (beanName.contains("springCloudClientHostInfo")) || (beanName.contains("applicationConfig"))
					|| (beanName.contains("springCloudDefaultProperties")) || (beanName.contains("cachedrandom")) )
			{
				System.out.println("Property Source1>>>" + beanName);
				System.out.println("Property Source>>>" + value);
				if(value !=null && !value.isBlank())
				{
				//newMap.put("jenkins-app.dbUserName", bean.getProperty("jenkins-app.dbUserName"));
				//newMap.put("jenkins-app.dbPassword", bean.getProperty("jenkins-app.dbPassword"));
					propertyLoader.readPropertiesFromFile();

					Set<Object> keySet = propertyLoader.getKey();
					Iterator iter = keySet.iterator();
					String key;
					while (iter.hasNext()) {
						key = (String) iter.next();
						newMap.put(key, bean.getProperty(key));
					}
				
				System.out.println("New MAp value"+newMap);
				environment.getPropertySources().addFirst(new MapPropertySource(bean.getName(), newMap));
				break;
				}

			} else {
				System.out.println("Property Source2>>>" + beanName);
				System.out.println("Property Source>>>" + value);

				environment.getPropertySources().addFirst(new MapPropertySource(propertySourceName, systemConfigMap));

			}
		}
		// PropertySource<?> source =
		// environment.getPropertySources().get("jenkins-app.dbUserName");

		// Object value = source.getProperty("jenkins-app.dbUserName");

		// System.out.println("Value inside afterProperties"+value);

	}

	@Override
	public void setEnvironment(Environment environment) {
		// TODO Auto-generated method stub
		if (environment instanceof ConfigurableEnvironment) {
			System.out.println("Environment>>" + environment);
			this.environment = (ConfigurableEnvironment) environment;
		}

	}

	/*
	 * public BeanMap setConjurSecretes(Map<String, Object> result) {
	 * 
	 * BeanMap map = (BeanMap) result; for (Map.Entry<String, Object> kv :
	 * result.entrySet()) { map.put(kv.getKey(), kv.getValue());
	 * System.out.println("Key>>>>"+kv.getKey() + "Value >>>>"+kv.getValue()); } //
	 * myMap.put("jenkins-app.dbUserName", "myValue"); return map; }
	 */

	/*
	 * @Override public void onApplicationEvent(ApplicationEnvironmentPreparedEvent
	 * event) {
	 * 
	 * System.out.println("Inside ApplicationEnvironmentpreparevenet");
	 * ConfigurableEnvironment environment = event.getEnvironment();
	 * MutablePropertySources propertySources = environment.getPropertySources();
	 * //Map<String,Object> myMap = callConjur();
	 * 
	 * for(Map.Entry<String,Object> kv : secretParams.entrySet()) {
	 * myMap.put(kv.getKey(),kv.getValue()); } //myMap.put("jenkins-app.dbUserName",
	 * "myValue"); //myMap.put("jenkins-app.dbPassword", "Password");
	 * //myMap.put("jenkins-app.dbUrl", "url");
	 * System.out.println(" Secret value insid Listener"+ myMap);
	 * propertySources.addFirst(new MapPropertySource("conjur", myMap));
	 * 
	 * }
	 */
}
