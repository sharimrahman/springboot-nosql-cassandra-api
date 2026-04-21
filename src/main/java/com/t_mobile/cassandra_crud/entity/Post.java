package com.t_mobile.cassandra_crud.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@JsonPropertyOrder({
        "userId",
        "id",
        "title",
        "body",
})
@Data
@Table("posts")


public class Post {
    @PrimaryKey
    private Integer id;
    private Integer userId;
    private String title;
    private String body;
}