package com.t_mobile.cassandra_crud.service;

import com.t_mobile.cassandra_crud.dto.UserDTO;
import com.t_mobile.cassandra_crud.entity.Post;
import com.t_mobile.cassandra_crud.entity.User;
import com.t_mobile.cassandra_crud.repository.PostRepository;
import com.t_mobile.cassandra_crud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DataService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PostRepository postRepo;

    private final RestTemplate restTemplate = new RestTemplate();

    public void loadUsers() {

        UserDTO[] users = restTemplate.getForObject(
                "https://jsonplaceholder.typicode.com/users",
                UserDTO[].class
        );

        List<User> list = new ArrayList<>();

        for (UserDTO dto : users) {

            User user = new User();

            user.setId(dto.getId());
            user.setName(dto.getName());
            user.setUsername(dto.getUsername());
            user.setEmail(dto.getEmail());

            if (dto.getAddress() != null) {
                user.setStreet(dto.getAddress().getStreet());
                user.setSuite(dto.getAddress().getSuite());
                user.setCity(dto.getAddress().getCity());
                user.setZipcode(dto.getAddress().getZipcode());

                if (dto.getAddress().getGeo() != null) {
                    user.setLat(dto.getAddress().getGeo().getLat());
                    user.setLng(dto.getAddress().getGeo().getLng());
                }
            }

            list.add(user);
        }

        userRepo.saveAll(list);
    }

    public void loadPosts() {
        Post[] posts = restTemplate.getForObject(
                "https://jsonplaceholder.typicode.com/posts",
                Post[].class
        );
        postRepo.saveAll(Arrays.asList(posts));
    }
}