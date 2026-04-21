package com.t_mobile.cassandra_crud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "external.api")
public class ApiConfig {
    private String users;
    private String posts;
}