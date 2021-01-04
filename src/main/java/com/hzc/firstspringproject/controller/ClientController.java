package com.hzc.firstspringproject.controller;

import com.hzc.firstspringproject.model.Client;
import com.hzc.firstspringproject.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping({"/clients"})
public class ClientController {

    private final ClientRepository repository;

    //methods to work with the com.hzc.firstspringproject.repository (better done on a service independent class)

    //get all clients (select * from clients - sql)
    @GetMapping
    public List findAllClients() {
        return repository.findAll();
    }

    //get client by id (select * from clients where id = param - sql)
    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Client> findById(@PathVariable long id) {
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
    @PutMapping(value = "/{id}")
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
                .map(record -> {
                    repository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

}

