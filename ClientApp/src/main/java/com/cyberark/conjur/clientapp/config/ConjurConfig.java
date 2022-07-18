package com.cyberark.conjur.clientapp.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import com.cyberark.conjur.clientapp.conjurproperty.SecretProperty;
import com.cyberark.conjur.clientapp.core.ConjurPropertySource;

import com.cyberark.conjur.clientapp.processor.ConjurValueProcessor;

@Configuration

public class ConjurConfig {

	@Bean
	@Primary
	public ConjurPropertySource getPropertySource() {
		return new ConjurPropertySource();
	}

	// @Bean
	// public ConjurValueProcessor getConjurProcessor() {
	// return new ConjurValueProcessor();
	// }

	@Bean
	public PropertySourcesPlaceholderConfigurer getPropertyplaceHolderconfigureer() {

		PropertySourcesPlaceholderConfigurer cfg = new PropertySourcesPlaceholderConfigurer();
		cfg.setLocation(new ClassPathResource("conjur.properties"));
		
		return cfg;
	}

}
