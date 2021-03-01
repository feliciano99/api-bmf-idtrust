package com.idrust.bmfapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.idrust.bmfapi.properties.QuandlAPIProperties;

@SpringBootApplication
@EnableConfigurationProperties(QuandlAPIProperties.class)
public class BmfApiApplication {

	public static void main(String[] args) {
		System.setProperty("file.encoding", "UTF-8");
		SpringApplication.run(BmfApiApplication.class, args);
	}

}
