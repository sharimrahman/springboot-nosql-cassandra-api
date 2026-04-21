package com.t_mobile.cassandra_crud.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@JsonPropertyOrder({
        "street",
        "suite",
        "city",
        "zipcode",
        "geo"
})
@Data
@UserDefinedType("address_type")
public class Address {

    private String street;
    private String suite;
    private String city;
    private String zipcode;

    //@CassandraType(type = CassandraType.Name.UDT, userTypeName = "geo_type")
    private Geo geo;
}