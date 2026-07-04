package com.navesh.notifyx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class NotifyxApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotifyxApplication.class, args);
	}

}
