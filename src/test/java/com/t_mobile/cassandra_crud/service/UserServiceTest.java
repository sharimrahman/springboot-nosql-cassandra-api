package com.t_mobile.cassandra_crud.service;

import com.t_mobile.cassandra_crud.config.ApiConfig;
import com.t_mobile.cassandra_crud.entity.*;
import com.t_mobile.cassandra_crud.exception.DataConflictException;
import com.t_mobile.cassandra_crud.exception.ResourceNotFoundException;
import com.t_mobile.cassandra_crud.repository.UserRepository;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private ApiConfig apiConfig;

    @Mock
    private RestClient restClient;

    @InjectMocks
    private UserService userService;

    // ---------------- GET ----------------
    @Test
    void getUserById_success() {
        User user = new User();
        user.setId(1);

        when(userRepo.findById(1)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1);

        assertEquals(1, result.getId());
    }

    @Test
    void getUserById_notFound() {
        when(userRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(1));
    }

    // ---------------- CREATE ----------------
    @Test
    void createUser_success() {
        User user = new User();
        user.setId(1);

        when(userRepo.existsById(1)).thenReturn(false);
        when(userRepo.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertEquals(1, result.getId());
    }

    @Test
    void createUser_conflict() {
        User user = new User();
        user.setId(1);

        when(userRepo.existsById(1)).thenReturn(true);

        assertThrows(DataConflictException.class,
                () -> userService.createUser(user));
    }

    // ---------------- DELETE ----------------
    @Test
    void deleteUser_success() {
        when(userRepo.existsById(1)).thenReturn(true);

        userService.deleteUser(1);

        verify(userRepo).deleteById(1);
    }

    @Test
    void deleteUser_notFound() {
        when(userRepo.existsById(1)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(1));
    }

    // ---------------- LOAD USERS (MAIN LOGIC) ----------------
    @Test
    void loadUsers_success_fullMapping() {

        // Prepare nested objects
        Geo geo = new Geo();
        geo.setLat("12");
        geo.setLng("77");

        Address address = new Address();
        address.setStreet("Street");
        address.setGeo(geo);

        Company company = new Company();
        company.setName("ABC");

        User dto = new User();
        dto.setId(1);
        dto.setAddress(address);
        dto.setCompany(company);

        // MOCK chain
        RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(apiConfig.getUsers()).thenReturn("url");
        when(restClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class)))
                .thenReturn(List.of(dto));

        userService.loadUsers();

        verify(userRepo).saveAll(anyList());
    }

    @Test
    void loadUsers_empty() {

        RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(apiConfig.getUsers()).thenReturn("url");
        when(restClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class)))
                .thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.loadUsers());
    }
}