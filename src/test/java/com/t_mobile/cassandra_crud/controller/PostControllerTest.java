package com.t_mobile.cassandra_crud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.t_mobile.cassandra_crud.entity.Post;
import com.t_mobile.cassandra_crud.repository.PostRepository;
import com.t_mobile.cassandra_crud.service.PostService;
import com.t_mobile.cassandra_crud.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private UserService userService;

    @MockBean
    private PostRepository repo;

    @Autowired
    private ObjectMapper objectMapper;

    // GET by ID
    @Test
    void getPostById() throws Exception {
        Post post = new Post();
        post.setId(1);

        when(postService.getPostById(1)).thenReturn(post);

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    // CREATE
    @Test
    void createPost() throws Exception {
        Post post = new Post();
        post.setId(1);

        when(postService.createPost(any(Post.class))).thenReturn(post);

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    // LOAD
    @Test
    void loadPosts() throws Exception {
        doNothing().when(postService).loadPosts();

        mockMvc.perform(get("/posts/load"))
                .andExpect(status().isOk())
                .andExpect(content().string("Posts loaded"));
    }

    // GET ALL
    @Test
    void getAllPosts() throws Exception {
        Post post = new Post();
        post.setId(1);

        when(repo.findAll()).thenReturn(List.of(post));

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    // DELETE
    @Test
    void deletePost() throws Exception {
        doNothing().when(postService).deletePost(1);

        mockMvc.perform(delete("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Post deleted successfully"));
    }
}