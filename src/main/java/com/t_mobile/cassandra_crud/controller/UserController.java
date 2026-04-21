package com.t_mobile.cassandra_crud.controller;

import com.t_mobile.cassandra_crud.entity.User;
import com.t_mobile.cassandra_crud.repository.UserRepository;
import com.t_mobile.cassandra_crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    @GetMapping("/load")
    public String loadUsers() {
        userService.loadUsers();
        return "Users loaded";
    }

    @GetMapping
    public List<User> getAll() {
        return userRepo.findAll();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userRepo.save(user);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        userRepo.deleteById(id);
        return "Deleted";
    }
}