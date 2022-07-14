package com.cyber.conjur.configserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cyber.conjur.configserver.core.ConjurPropertySource;



public class ConjurPropertyConfig {
	
	
	@Bean
	public ConjurPropertySource conjurPropertySource()
	{
		return new ConjurPropertySource();
	}

}
