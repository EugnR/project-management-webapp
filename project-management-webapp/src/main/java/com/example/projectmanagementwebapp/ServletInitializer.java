package com.example.projectmanagementwebapp;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {
	//для того, чтобы ваш Spring Boot приложение могло быть развернуто как традиционное веб-приложение на сервлете контейнере, таком как Apache Tomcat.
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ProjectManagementWebappApplication.class);
	}

}
