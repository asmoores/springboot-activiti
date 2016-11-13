package com.ase.sping_boot_activiti;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
	
	@Bean
	public DataSource database() {
	    return DataSourceBuilder.create()
	        .url("jdbc:postgresql://127.0.0.1:5432/activiti?characterEncoding=UTF-8")
	        .username("andrew")
	        .password("")
	        .driverClassName("org.postgresql.Driver")
	        .build();
	}
	
	@Bean
	CommandLineRunner init( final RepositoryService repositoryService,
	                              final RuntimeService runtimeService,
	                              final TaskService taskService) {

	    return new CommandLineRunner() {

	        public void run(String... strings) throws Exception {
	            Map<String, Object> variables = new HashMap<String, Object>();
	            variables.put("applicantName", "John Doe");
	            variables.put("email", "john.doe@activiti.com");
	            variables.put("phoneNumber", "123456789");
	            runtimeService.startProcessInstanceByKey("hireProcess", variables);
	        }
	    };
	}
}
