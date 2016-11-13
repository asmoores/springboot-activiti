package com.ase.sping_boot_activiti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookOrderApp {
	public static void main(String[] args) {
		SpringApplication.run(BookOrderApp.class, args);
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
	InitializingBean usersAndGroupsInitializer(final IdentityService identityService) {

	    return new InitializingBean() {
	        public void afterPropertiesSet() throws Exception {

	            Group group = identityService.newGroup("user");
	            group.setName("users");
	            group.setType("security-role");
	            identityService.saveGroup(group);

	            User admin = identityService.newUser("admin");
	            admin.setPassword("admin");
	            identityService.saveUser(admin);
	            
	            identityService.createMembership("admin", "user");

	        }
	    };
	}
	
	@Bean
	CommandLineRunner init( final RepositoryService repositoryService,
	                              final RuntimeService runtimeService,
	                              final TaskService taskService,
	                              final IdentityService identityService) {

	    return new CommandLineRunner() {

	        public void run(String... strings) throws Exception {
	            Map<String, Object> variables = new HashMap<String, Object>();
	            variables.put("isbn", "1234");
	            
	            identityService.setAuthenticatedUserId("admin");
	            
	            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("bookOrder", variables);
	            
	            if (processInstance == null) {
	            	System.out.println("failed to start procxess instance");
	            }

	            // Doesn't get assigned to admin yet
	            
//	            List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("admin").list();
//	            
//	            System.out.println("Task 0: " + tasks.get(0).getName());
//	            
//	            taskService.complete(tasks.get(0).getId());
	            
	        }
	    };
	}
}
