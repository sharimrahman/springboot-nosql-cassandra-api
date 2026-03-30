package com.t_mobile.cassandra_crud.controller;

import com.t_mobile.cassandra_crud.entity.User;
import com.t_mobile.cassandra_crud.repository.UserRepository;
import com.t_mobile.cassandra_crud.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private DataService service;

    @GetMapping("/load")
    public String loadUsers() {
        service.loadUsers();
        return "Users loaded";
    }

    @GetMapping
    public List<User> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return repo.save(user);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        repo.deleteById(id);
        return "Deleted";
    }
}