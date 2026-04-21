package com.t_mobile.cassandra_crud.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@JsonPropertyOrder({
        "lat",
        "lng"
})
@Data
@UserDefinedType("geo_type")
public class Geo {

    private String lat;
    private String lng;
}