package com.t_mobile.cassandra_crud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.t_mobile.cassandra_crud.entity.User;
import com.t_mobile.cassandra_crud.repository.UserRepository;
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

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepo;

    @Autowired
    private ObjectMapper objectMapper;

    // LOAD USERS
    @Test
    void loadUsers() throws Exception {
        doNothing().when(userService).loadUsers();

        mockMvc.perform(get("/users/load"))
                .andExpect(status().isOk())
                .andExpect(content().string("Users loaded"));
    }

    // GET ALL
    @Test
    void getAllUsers() throws Exception {
        User user = new User();
        user.setId(1);

        when(userRepo.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    // GET BY ID
    @Test
    void getUserById() throws Exception {
        User user = new User();
        user.setId(1);

        when(userService.getUserById(1)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    // CREATE
    @Test
    void createUser() throws Exception {
        User user = new User();
        user.setId(1);

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    // DELETE
    @Test
    void deleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }
}