package com.t_mobile.cassandra_crud.service;

import com.t_mobile.cassandra_crud.config.ApiConfig;
import com.t_mobile.cassandra_crud.entity.Address;
import com.t_mobile.cassandra_crud.entity.Company;
import com.t_mobile.cassandra_crud.entity.Geo;
import com.t_mobile.cassandra_crud.entity.User;
import com.t_mobile.cassandra_crud.exception.DataConflictException;
import com.t_mobile.cassandra_crud.exception.ResourceNotFoundException;
import com.t_mobile.cassandra_crud.repository.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private ApiConfig apiConfig;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RestClient restClient;

    public User getUserById(Integer id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User createUser(User user) {

        if (userRepo.existsById(user.getId())) {
            throw new DataConflictException("User already exists with id: " + user.getId());
        }

        return userRepo.save(user);
    }

    public void deleteUser(Integer id) {

        if (!userRepo.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        userRepo.deleteById(id);
    }

    public void loadUsers() {

        List<User> users = restClient.get()
                .uri(apiConfig.getUsers())
                .retrieve()
                .body(new ParameterizedTypeReference<List<User>>() {});

        //Validate API response
        if (users == null || users.isEmpty()) {
            throw new ResourceNotFoundException("No users received from external API");
        }

        List<User> list = new ArrayList<>();

        for (User dto : users) {



            User user = new User();

            user.setId(dto.getId());
            user.setName(dto.getName());
            user.setUsername(dto.getUsername());
            user.setEmail(dto.getEmail());
            user.setPhone(dto.getPhone());
            user.setWebsite(dto.getWebsite());

            //Address mapping
            if (dto.getAddress() != null) {

                Address address = new Address();
                address.setStreet(dto.getAddress().getStreet());
                address.setSuite(dto.getAddress().getSuite());
                address.setCity(dto.getAddress().getCity());
                address.setZipcode(dto.getAddress().getZipcode());

                if (dto.getAddress().getGeo() != null) {
                    Geo geo = new Geo();
                    geo.setLat(dto.getAddress().getGeo().getLat());
                    geo.setLng(dto.getAddress().getGeo().getLng());
                    address.setGeo(geo);
                }

                user.setAddress(address);
            }

            //Company mapping
            if (dto.getCompany() != null) {

                Company company = new Company();
                company.setName(dto.getCompany().getName());
                company.setCatchPhrase(dto.getCompany().getCatchPhrase());
                company.setBs(dto.getCompany().getBs());

                user.setCompany(company);
            }

            list.add(user);
        }

        //Save to DB
        userRepo.saveAll(list);
    }
}