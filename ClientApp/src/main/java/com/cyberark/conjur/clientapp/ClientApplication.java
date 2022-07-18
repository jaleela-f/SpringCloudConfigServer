package com.cyberark.conjur.clientapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import com.cyberark.conjur.clientapp.config.ConjurConfig;
import com.cyberark.conjur.clientapp.conjurproperty.SecretProperty;

@SpringBootApplication

public class ClientApplication implements CommandLineRunner
{
	
	//@Autowired
	//public static SecretProperty property = new SecretProperty();
	
	@Autowired
	Environment env;
	
	@Value("${jenkins-app.dbUserName}")
	private  String userName;
	
	@Value("${jenkins-app.dbPassword}")
	private String password;
	
	@Value("${jenkins-app.dbUrl}")
	private String url;
	
	//@Value("${app.name}")
	//private String valuePath;

	
	/*
	 * public static String getDbUserName() { return dbUserName; }
	 * 
	 * @Value("${jenkins-app.dbUserName}") public void setDbUserName(String
	 * dbUserName) { ClientApplication.dbUserName = dbUserName; }
	 */


	public static void main(String[] args)
	{
	  SpringApplication.run(ClientApplication.class, args);
	 //ApplicationContext appContext = new AnnotationConfigApplicationContext(ClientApplication.class);
	  
	}



	@Override
	public void run(String... args) throws Exception {
		System.out.println("Property >>>>>>>"+userName);
		System.out.println("Property >>>>>>>"+password);
		System.out.println("Property >>>>>>>>"+url);
		//System.out.println("Property >>>>>>>>"+valuePath);
	
		System.out.println("Property >>>>>>>"+System.getProperty("jenkins-app.dbUserName"));
		System.out.println("Property >>>>>>>"+System.getProperty("jenkins-app.dbPassword"));
		System.out.println("Property >>>>>>>>"+System.getProperty("jenkins-app.dbUrl"));
		
	}

	

}
