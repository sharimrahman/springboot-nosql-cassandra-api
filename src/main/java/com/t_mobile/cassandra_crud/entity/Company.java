package com.t_mobile.cassandra_crud.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;
@JsonPropertyOrder({
        "name",
        "catchPhrase",
        "bs"
})
@Data
@UserDefinedType("company_type")
public class Company {

    private String name;
    private String catchPhrase;
    private String bs;
}