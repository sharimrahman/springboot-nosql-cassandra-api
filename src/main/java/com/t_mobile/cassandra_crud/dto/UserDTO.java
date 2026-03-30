package com.t_mobile.cassandra_crud.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Integer id;
    private String name;
    private String username;
    private String email;
    private Address address;

    @Data
    public static class Address {
        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private Geo geo;
    }

    @Data
    public static class Geo {
        private String lat;
        private String lng;
    }
}