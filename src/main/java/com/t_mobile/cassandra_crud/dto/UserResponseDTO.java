package com.t_mobile.cassandra_crud.dto;

import lombok.Data;

@Data
public class UserResponseDTO {

    private Integer id;
    private String name;
    private String username;
    private String email;

    private Address address;
    private String phone;
    private String website;
    private Company company;

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

    @Data
    public static class Company {
        private String name;
        private String catchPhrase;
        private String bs;
    }
}