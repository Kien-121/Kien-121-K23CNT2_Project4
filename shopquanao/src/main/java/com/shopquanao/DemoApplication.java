package com.shopquanao;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.shopquanao.Entity.N11NHK_NguoiDung;
import com.shopquanao.Repository.N11NHK_NguoiDungRepository;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner resetAdminPassword(N11NHK_NguoiDungRepository repo, PasswordEncoder encoder) {
        return args -> {
            N11NHK_NguoiDung admin = repo.findByUsername("admin").orElse(null);
            if (admin != null) {
                admin.setPassword(encoder.encode("123456"));
                repo.save(admin);
            }
        };
    }
}