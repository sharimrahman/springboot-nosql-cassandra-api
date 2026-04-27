package com.t_mobile.cassandra_crud.service;

import com.t_mobile.cassandra_crud.config.ApiConfig;
import com.t_mobile.cassandra_crud.entity.Post;
import com.t_mobile.cassandra_crud.exception.DataConflictException;
import com.t_mobile.cassandra_crud.exception.ResourceNotFoundException;
import com.t_mobile.cassandra_crud.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class PostService {

    private static final Logger log = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private ApiConfig apiConfig;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private RestClient restClient;

    public Post getPostById(Integer id) {

        log.info("Fetching post with id: {}", id);

        return postRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Post not found with id: {}", id);
                    return new ResourceNotFoundException("Post not found with id: " + id);
                });
    }

    public Post createPost(Post post) {

        log.info("Creating post with id: {}", post.getId());

        if (postRepo.existsById(post.getId())) {
            log.warn("Post already exists with id: {}", post.getId());
            throw new DataConflictException("Post already exists with id: " + post.getId());
        }

        Post savedPost = postRepo.save(post);

        log.info("Post created successfully with id: {}", post.getId());

        return savedPost;
    }

    public void deletePost(Integer id) {

        log.info("Deleting post with id: {}", id);

        if (!postRepo.existsById(id)) {
            log.error("Post not found for deletion with id: {}", id);
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }

        postRepo.deleteById(id);

        log.info("Post deleted successfully with id: {}", id);
    }

    public void loadPosts() {

        log.info("Starting post load from external API");

        List<Post> posts = restClient.get()
                .uri(apiConfig.getPosts())
                .retrieve()
                .body(new ParameterizedTypeReference<List<Post>>() {});

        // Validate API response
        if (posts == null || posts.isEmpty()) {
            log.error("No posts received from external API");
            throw new ResourceNotFoundException("No posts received from external API");
        }

        log.info("Received {} posts from external API", posts.size());

        // Save to DB
        postRepo.saveAll(posts);

        log.info("All posts saved successfully");
    }
}