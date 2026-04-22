package com.t_mobile.cassandra_crud.service;

import com.t_mobile.cassandra_crud.config.ApiConfig;
import com.t_mobile.cassandra_crud.entity.Post;
import com.t_mobile.cassandra_crud.exception.DataConflictException;
import com.t_mobile.cassandra_crud.exception.ResourceNotFoundException;
import com.t_mobile.cassandra_crud.repository.PostRepository;
import org.apache.coyote.BadRequestException;
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

    public Post getPostById(Integer id) {
        return postRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
    }

    public Post createPost(Post post) {

        if (postRepo.existsById(post.getId())) {
            throw new DataConflictException("Post already exists with id: " + post.getId());
        }

        return postRepo.save(post);
    }

    public void deletePost(Integer id) {

        if (!postRepo.existsById(id)) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }

        postRepo.deleteById(id);
    }

    public void loadPosts() {

        List<Post> posts = restClient.get()
                .uri(apiConfig.getPosts())
                .retrieve()
                .body(new ParameterizedTypeReference<List<Post>>() {});

        //Validate API response
        if (posts == null || posts.isEmpty()) {
            throw new ResourceNotFoundException("No posts received from external API");
        }


        //Save to DB
        postRepo.saveAll(posts);
    }
}