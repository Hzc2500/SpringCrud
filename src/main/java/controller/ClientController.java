package controller;

import model.Client;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.ClientRepository;

import java.util.List;
import java.util.stream.LongStream;

@RestController
@RequestMapping({"/clients"})
public class ClientController {

    private ClientRepository repository;

    ClientController(ClientRepository clientRepository) {
        this.repository = clientRepository;
    }

    //methods to work with the repository (better done on a service independent class)

    //get all clients (select * from clients - sql)
    @GetMapping
    public List findAllClients() {
        return repository.findAll();
    }

    //get client by id (select * from clients where id = param - sql)
    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Client> findById(@PathVariable long id){
        return repository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    //create a new client
    @PostMapping
    public Client create(@RequestBody Client client) {
        return repository.save(client);
    }

    //update client by id
    @PutMapping(value="/{id}")
    public ResponseEntity<Client> update(@PathVariable("id") long id, @RequestBody Client client) {
        return repository.findById(id).map(record -> {
            record.setName(client.getName());
            record.setBirthdate(client.getBirthdate());
            record.setCpf(client.getCpf());
            record.setEmail(client.getEmail());
            Client updated = repository.save(record);
            return ResponseEntity.ok().body(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    //delete client by id
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return repository.findById(id)
        .map(record  -> {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }


    //forcing data to database
    @SpringBootApplication
    public static class SpringCloudMysqlApplication {

        public static void main(String[] args) {
            SpringApplication.run(SpringCloudMysqlApplication.class,args);
        }

        @Bean
        CommandLineRunner init (ClientRepository repository) {
            return args -> {
                repository.deleteAll();
                LongStream.range(1,11)
                        .mapToObj(i -> {
                            Client c = new Client();
                            c.setName("client" + i);
                            c.setEmail("client" + i +"@test.com");
                            c.setBirthdate(i+"/"+i+"/"+"199"+i);
                            c.setCpf(i+i+i+"."+i+i+i+"."+i+i+i+"-"+i+i);
                            return c;
                        })
                        .map(v -> repository.save(v))
                        .forEach(System.out::println);
            };
        }
    }



}

