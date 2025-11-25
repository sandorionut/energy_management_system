package com.ds2025.authservice.config;

import com.ds2025.authservice.entities.Role;
import com.ds2025.authservice.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder {
    private final RoleRepository repo;

    public DataSeeder(RoleRepository repo) {
        this.repo = repo;
    }

    @PostConstruct
    public void seed() {
        if (repo.findByName("ADMIN").isEmpty()) {
            Role admin = new Role();
            admin.setName("ADMIN");
            repo.save(admin);
        }
        if (repo.findByName("USER").isEmpty()) {
            Role user = new Role();
            user.setName("USER");
            repo.save(user);
        }
        System.out.println("Roles seeded successfully");
    }
}
