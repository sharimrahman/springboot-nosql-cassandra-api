package com.t_mobile.cassandra_crud.controller;

import com.t_mobile.cassandra_crud.entity.Post;
import com.t_mobile.cassandra_crud.repository.PostRepository;
import com.t_mobile.cassandra_crud.service.PostService;
import com.t_mobile.cassandra_crud.service.UserService;
import jakarta.validation.Valid;
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

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Integer id) {
        return postService.getPostById(id);
    }

    @PostMapping
    public Post create(@Valid @RequestBody Post post) {
        return postService.createPost(post);
    }

    @GetMapping("/load")
    public String loadPosts() {
        postService.loadPosts();
        return "Posts loaded";
    }

    @GetMapping
    public List<Post> getAll() {
        return repo.findAll();
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        postService.deletePost(id);
        return "Post deleted successfully";
    }
}