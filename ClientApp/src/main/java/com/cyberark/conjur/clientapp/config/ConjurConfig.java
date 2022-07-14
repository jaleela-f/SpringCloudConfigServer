package com.cyberark.conjur.clientapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cyberark.conjur.clientapp.conjurproperty.SecretProperty;
import com.cyberark.conjur.clientapp.core.ConjurPropertySource;

import com.cyberark.conjur.clientapp.processor.ConjurValueProcessor;

@Configuration
//@Import(ConjurValueProcessor.class)
public class ConjurConfig {

	@Bean
	public ConjurPropertySource getPropertySource() {
		return new ConjurPropertySource();
	}

	@Bean
	public ConjurValueProcessor getConjurProcessor() {
		return new ConjurValueProcessor();
	}

	@Bean
	public SecretProperty getSecretProperty() {
		return new SecretProperty();
	}

}
