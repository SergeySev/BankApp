package com.example.telproject;

import com.example.telproject.entity.*;
import com.example.telproject.entity.enums.AccountProductStatus;
import com.example.telproject.entity.enums.CurrencyType;
import com.example.telproject.entity.enums.ManagerStatus;
import com.example.telproject.entity.enums.ProductStatus;
import com.example.telproject.repository.AccountRepository;
import com.example.telproject.repository.ClientRepository;
import com.example.telproject.repository.ManagerRepository;
import com.example.telproject.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;

@Configuration
public class DataConfig {
    @Bean
    CommandLineRunner commandLineRunner(ManagerRepository managerRepository,
                                        ClientRepository clientRepository,
                                        ProductRepository productRepository,
                                        AccountRepository accountRepository) {
        return args -> {
            Manager john = new Manager("First",
                    "Manager",
                    ManagerStatus.ACTIVE,
                    Timestamp.valueOf("1993-02-16 00:00:00"),
                    "1manager@gmail.com",
                    "+49131245623");

            Client firstClient = new Client(john,
                    "FirstClient",
                    "Test",
                    "client@gmail.com",
                    Timestamp.valueOf("1993-02-16 00:00:00"),
                    "123");

            Client secondClient = new Client(john,
                    "FirstClient",
                    "Test",
                    "secondclient@gmail.com",
                    Timestamp.valueOf("1993-02-16 00:00:00"),
                    "123");

            Product credit_product = new Product(john,
                    "Credit",
                    ProductStatus.ACTIVE,
                    CurrencyType.EUR,
                    50);

            Product mortgage_product = new Product(john,
                    "Mortgage",
                    ProductStatus.ACTIVE,
                    CurrencyType.UAH,
                    50);

            Account account_credit = new Account(firstClient,
                    CurrencyType.EUR);
            Account account_mortgage = new Account(secondClient,
                    CurrencyType.UAH);

            Agreement agreement_credit = new Agreement(account_credit,
                    credit_product,
                    AccountProductStatus.ACTIVE,
                    BigDecimal.valueOf(100.23));

            Agreement agreement_mortgage = new Agreement(account_mortgage,
                    mortgage_product,
                    AccountProductStatus.ACTIVE,
                    BigDecimal.valueOf(200.23));

            // Set all relations between entities Manager and Client
            john.setClients(new HashSet<>());
            john.getClients().addAll(List.of(firstClient, secondClient));

            // Set all relations between entities Manager and Product
            john.setProducts(new HashSet<>());
            john.getProducts().addAll(List.of(credit_product, mortgage_product));

            // Set all relations between entities Client and Account
            firstClient.setAccountList(new HashSet<>());
            firstClient.getAccountList().add(account_credit);

            secondClient.setAccountList(new HashSet<>());
            secondClient.getAccountList().add(account_mortgage);

            // Set all relations between entities Product and Agreements
            credit_product.setAgreements(new HashSet<>());
            credit_product.getAgreements().add(agreement_credit);

            mortgage_product.setAgreements(new HashSet<>());
            mortgage_product.getAgreements().add(agreement_mortgage);



            productRepository.saveAll(List.of(credit_product, mortgage_product));
            managerRepository.saveAll(List.of(john));
            clientRepository.saveAll(List.of(firstClient, secondClient));
            accountRepository.saveAll(List.of(account_credit, account_mortgage));
        };
    }
}
