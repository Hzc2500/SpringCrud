package com.hzc.firstspringproject;

import com.hzc.firstspringproject.model.Client;
import com.hzc.firstspringproject.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.stream.LongStream;

@AllArgsConstructor
@SpringBootApplication
public class FirstSpringProjectApplication implements ApplicationRunner {

    private final ClientRepository clientRepository;

    public static void main(String[] args) {
        SpringApplication.run(FirstSpringProjectApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        clientRepository.deleteAll();
        LongStream.range(1, 11)
                .mapToObj(i -> {
                    Client c = new Client();
                    c.setName("client" + i);
                    c.setEmail("client" + i + "@test.com");
                    c.setBirthdate(i + "/" + i + "/" + "199" + i);
                    c.setCpf(i + i + i + "." + i + i + i + "." + i + i + i + "-" + i + i);
                    return c;
                })
                .map(clientRepository::save)
                .forEach(System.out::println);
    }
}
