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
	public  String dbUserName;
	
	@Value("${jenkins-app.dbPassword}")
	public String dbPassword;
	
	@Value("${jenkins-app.dbUrl}")
	public String dbUrl;
	
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
		System.out.println("Property >>>>>>>"+dbUserName);
		System.out.println("Property >>>>>>>"+dbPassword);
		System.out.println("Property >>>>>>>>"+dbUrl);
		
	}

	

}
