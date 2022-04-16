package com.ados.xbook;

import com.ados.xbook.domain.entity.User;
import com.ados.xbook.repository.UserRepo;
import com.ados.xbook.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class XbookServerApplication implements CommandLineRunner {

	@Autowired
	private CustomUserDetailsService service;

	@Autowired
	private UserRepo repo;

    public static void main(String[] args) {
        SpringApplication.run(XbookServerApplication.class, args);
    }

	@Override
	public void run(String... args) {
		if (repo.findFirstByUsername("admin") == null) {
			User user = new User();
			user.setUsername("admin");
			user.setPassword("admin");
			service.createAdmin(user);
		}
	}

}
