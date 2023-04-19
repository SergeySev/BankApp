package com.example.telproject;

import com.example.telproject.entity.Client;
import com.example.telproject.entity.Manager;
import com.example.telproject.entity.enums.ManagerStatus;
import com.example.telproject.repository.ClientRepository;
import com.example.telproject.repository.ManagerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Configuration
public class UserConfig {
    @Bean
    CommandLineRunner commandLineRunner(ManagerRepository managerRepository, ClientRepository clientRepository) {
        return args -> {
            Manager john = new Manager("First",
                    "Manager",
                    ManagerStatus.ACTIVE,
                    Timestamp.valueOf("1993-02-16 00:00:00"),
                    "1manager@gmail.com",
                    "+49131245623");
//            managerRepository.saveAll(List.of(john));

//            Client firstClient = new Client(managerRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Manager not found")),
            Client firstClient = new Client(john,
                    "FirstClient",
                    "Test",
                    "client@gmail.com",
                    Timestamp.valueOf("1993-02-16 00:00:00"),
                    "123");
//            clientRepository.saveAll(List.of(firstClient));
            john.setClients(new HashSet<>());
            john.getClients().add(firstClient);
            managerRepository.saveAll(List.of(john));

            clientRepository.save(firstClient);
        };
    }
}
