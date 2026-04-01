package com.t_mobile.cassandra_crud.service;

import com.t_mobile.cassandra_crud.dto.UserDTO;
import com.t_mobile.cassandra_crud.dto.UserResponseDTO;
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


            user.setPhone(dto.getPhone());
            user.setWebsite(dto.getWebsite());


            if (dto.getCompany() != null) {
                user.setCompanyName(dto.getCompany().getName());
                user.setCatchPhrase(dto.getCompany().getCatchPhrase());
                user.setBs(dto.getCompany().getBs());
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

    public UserResponseDTO convertToResponse(User user) {

        UserResponseDTO dto = new UserResponseDTO();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setWebsite(user.getWebsite());


        UserResponseDTO.Address address = new UserResponseDTO.Address();
        address.setStreet(user.getStreet());
        address.setSuite(user.getSuite());
        address.setCity(user.getCity());
        address.setZipcode(user.getZipcode());

        UserResponseDTO.Geo geo = new UserResponseDTO.Geo();
        geo.setLat(user.getLat());
        geo.setLng(user.getLng());

        address.setGeo(geo);
        dto.setAddress(address);

        UserResponseDTO.Company company = new UserResponseDTO.Company();
        company.setName(user.getCompanyName());
        company.setCatchPhrase(user.getCatchPhrase());
        company.setBs(user.getBs());

        dto.setCompany(company);

        return dto;
    }
}