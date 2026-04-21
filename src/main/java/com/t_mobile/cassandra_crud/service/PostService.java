package com.t_mobile.cassandra_crud.service;

import com.t_mobile.cassandra_crud.config.ApiConfig;
import com.t_mobile.cassandra_crud.entity.Post;
import com.t_mobile.cassandra_crud.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private ApiConfig apiConfig;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private RestClient restClient;

    public void loadPosts() {

        List<Post> posts = restClient.get()
                .uri(apiConfig.getPosts())
                .retrieve()
                .body(new ParameterizedTypeReference<List<Post>>() {});

        if (posts == null) return;

        postRepo.saveAll(posts);
    }
}