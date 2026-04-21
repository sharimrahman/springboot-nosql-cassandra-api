package com.t_mobile.cassandra_crud.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.*;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "id",
        "name",
        "username",
        "email",
        "address",
        "phone",
        "website",
        "company"
})
@Data
@Table("users")
public class User {

    @PrimaryKey
    private Integer id;

    private String name;
    private String username;
    private String email;

    private Address address;
    private String phone;
    private String website;
    private Company company;
}