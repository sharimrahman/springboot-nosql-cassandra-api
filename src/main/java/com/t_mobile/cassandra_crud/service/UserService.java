package com.t_mobile.cassandra_crud.service;

import com.t_mobile.cassandra_crud.config.ApiConfig;
import com.t_mobile.cassandra_crud.entity.Address;
import com.t_mobile.cassandra_crud.entity.Company;
import com.t_mobile.cassandra_crud.entity.Geo;
import com.t_mobile.cassandra_crud.entity.User;
import com.t_mobile.cassandra_crud.exception.DataConflictException;
import com.t_mobile.cassandra_crud.exception.ResourceNotFoundException;
import com.t_mobile.cassandra_crud.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private ApiConfig apiConfig;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RestClient restClient;

    public User getUserById(Integer id) {

        log.info("Fetching user with id: {}", id);

        return userRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });
    }

    public User createUser(User user) {

        log.info("Creating user with id: {}", user.getId());

        if (userRepo.existsById(user.getId())) {
            log.warn("User already exists with id: {}", user.getId());
            throw new DataConflictException("User already exists with id: " + user.getId());
        }

        User savedUser = userRepo.save(user);

        log.info("User created successfully with id: {}", user.getId());

        return savedUser;
    }

    public void deleteUser(Integer id) {

        log.info("Deleting user with id: {}", id);

        if (!userRepo.existsById(id)) {
            log.error("User not found for deletion with id: {}", id);
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        userRepo.deleteById(id);

        log.info("User deleted successfully with id: {}", id);
    }

    public void loadUsers() {

        log.info("Starting user load from external API");

        List<User> users = restClient.get()
                .uri(apiConfig.getUsers())
                .retrieve()
                .body(new ParameterizedTypeReference<List<User>>() {});

        // Validate API response
        if (users == null || users.isEmpty()) {
            log.error("No users received from external API");
            throw new ResourceNotFoundException("No users received from external API");
        }

        log.info("Received {} users from external API", users.size());

        List<User> list = new ArrayList<>();

        for (User dto : users) {

            User user = new User();

            user.setId(dto.getId());
            user.setName(dto.getName());
            user.setUsername(dto.getUsername());
            user.setEmail(dto.getEmail());
            user.setPhone(dto.getPhone());
            user.setWebsite(dto.getWebsite());

            // Address mapping
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

            // Company mapping
            if (dto.getCompany() != null) {

                Company company = new Company();
                company.setName(dto.getCompany().getName());
                company.setCatchPhrase(dto.getCompany().getCatchPhrase());
                company.setBs(dto.getCompany().getBs());

                user.setCompany(company);
            }

            list.add(user);
        }

        log.info("Saving {} users to Cassandra", list.size());

        userRepo.saveAll(list);

        log.info("All users saved successfully");
    }
}