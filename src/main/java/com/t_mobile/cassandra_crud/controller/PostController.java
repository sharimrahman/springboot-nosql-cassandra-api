package com.t_mobile.cassandra_crud.controller;

import com.t_mobile.cassandra_crud.entity.Post;
import com.t_mobile.cassandra_crud.repository.PostRepository;
import com.t_mobile.cassandra_crud.service.PostService;
import com.t_mobile.cassandra_crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository repo;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @GetMapping("/load")
    public String loadPosts() {
        postService.loadPosts();
        return "Posts loaded";
    }

    @GetMapping
    public List<Post> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public Post create(@RequestBody Post post) {
        return repo.save(post);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        repo.deleteById(id);
        return "Deleted";
    }
}