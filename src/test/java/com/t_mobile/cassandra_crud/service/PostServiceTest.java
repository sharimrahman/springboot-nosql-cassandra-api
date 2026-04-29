package com.t_mobile.cassandra_crud.service;

import com.t_mobile.cassandra_crud.config.ApiConfig;
import com.t_mobile.cassandra_crud.entity.Post;
import com.t_mobile.cassandra_crud.exception.DataConflictException;
import com.t_mobile.cassandra_crud.exception.ResourceNotFoundException;
import com.t_mobile.cassandra_crud.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepo;

    @Mock
    private ApiConfig apiConfig;

    @Mock
    private RestClient restClient;

    @InjectMocks
    private PostService postService;

    // ---------------- GET ----------------
    @Test
    void getPostById_success() {
        Post post = new Post();
        post.setId(1);

        when(postRepo.findById(1)).thenReturn(Optional.of(post));

        Post result = postService.getPostById(1);

        assertEquals(1, result.getId());
    }

    @Test
    void getPostById_notFound() {
        when(postRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> postService.getPostById(1));
    }

    // ---------------- CREATE ----------------
    @Test
    void createPost_success() {
        Post post = new Post();
        post.setId(1);

        when(postRepo.existsById(1)).thenReturn(false);
        when(postRepo.save(post)).thenReturn(post);

        Post result = postService.createPost(post);

        assertEquals(1, result.getId());
    }

    @Test
    void createPost_conflict() {
        Post post = new Post();
        post.setId(1);

        when(postRepo.existsById(1)).thenReturn(true);

        assertThrows(DataConflictException.class,
                () -> postService.createPost(post));
    }

    // ---------------- DELETE ----------------
    @Test
    void deletePost_success() {
        when(postRepo.existsById(1)).thenReturn(true);

        postService.deletePost(1);

        verify(postRepo).deleteById(1);
    }

    @Test
    void deletePost_notFound() {
        when(postRepo.existsById(1)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> postService.deletePost(1));
    }

    // ---------------- LOAD POSTS ----------------
    @Test
    void loadPosts_success() {
        Post post = new Post();
        post.setId(1);

        // MOCK chain
        RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(apiConfig.getPosts()).thenReturn("url");
        when(restClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class)))
                .thenReturn(List.of(post));

        postService.loadPosts();

        verify(postRepo).saveAll(anyList());
    }

    @Test
    void loadPosts_empty() {
        RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(apiConfig.getPosts()).thenReturn("url");
        when(restClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class)))
                .thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class,
                () -> postService.loadPosts());
    }
}