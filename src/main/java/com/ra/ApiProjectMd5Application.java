package com.ra;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class ApiProjectMd5Application {

	public static void main(String[] args) {
		SpringApplication.run(ApiProjectMd5Application.class, args);
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	//Setup kho chứa trên firebase
	@Bean
	public Storage storage() throws IOException
	{
		InputStream inputStream = new ClassPathResource("firebase-config.json").getInputStream();
		return StorageOptions.newBuilder()
				.setCredentials(GoogleCredentials.fromStream(inputStream))
				.build()
				.getService();
	}
}
