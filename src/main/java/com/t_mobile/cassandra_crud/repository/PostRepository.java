package com.t_mobile.cassandra_crud.repository;

import com.t_mobile.cassandra_crud.entity.Post;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface PostRepository extends CassandraRepository<Post, Integer> {
}