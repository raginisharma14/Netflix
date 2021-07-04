package com.netflix.CompatibleStreamService;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
/*
 * 
 * Main Class. Run this class to start the service
 */
@SpringBootApplication
@ComponentScan({"com.netflix.client", "com.netflix.controller", "com.netflix.model", "com.netflix.requestmapper", 
	"com.netflix.responsemapper", "com.netflix.handler"})
@EnableAutoConfiguration
public class CompatibleStreamServiceApplication {

   
	public static void main(String[] args) {
		SpringApplication.run(CompatibleStreamServiceApplication.class, args);
	}

}
