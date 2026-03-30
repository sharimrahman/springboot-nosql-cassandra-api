package com.t_mobile.cassandra_crud.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table("users")
public class User {

    @PrimaryKey
    private Integer id;

    private String name;
    private String username;
    private String email;

    private String street;
    private String suite;
    private String city;
    private String zipcode;

    private String lat;
    private String lng;
}