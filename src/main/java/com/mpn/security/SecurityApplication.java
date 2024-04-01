package com.mpn.security;

import com.mpn.security.auth.AuthenticationService;
import com.mpn.security.auth.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.mpn.security.user.Role.ADMIN;
import static com.mpn.security.user.Role.MANAGER;

@SpringBootApplication
public class SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	){
       return args -> {
		   var admin = RegisterRequest.builder()
				   .firstName("admin")
				   .lastName("")
				   .email("admin@gmail.com")
				   .password("password")
				   .role(ADMIN)
				   .build();

		   System.out.println("ADMIN token: "+  service.register(admin).getAccessToken());

		   var manager = RegisterRequest.builder()
				   .firstName("manager")
				   .lastName("")
				   .email("manager@gmail.com")
				   .password("password")
				   .role(MANAGER)
				   .build();

		   System.out.println("Manager token: "+  service.register(manager).getAccessToken());
	   };
	}

}
