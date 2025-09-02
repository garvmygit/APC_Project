package org.GG.apcProject;

import java.util.Set;

import org.GG.apcProject.model.User;
import org.GG.apcProject.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ApcProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApcProjectApplication.class, args);
    }

    // Initialize a default admin user
    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123")); // default password
                admin.setRoles(Set.of("ROLE_ADMIN")); // assign roles as a Set
                userRepository.save(admin);
                System.out.println("Default admin user created: username=admin, password=admin123");
            }
        };
    }
}
