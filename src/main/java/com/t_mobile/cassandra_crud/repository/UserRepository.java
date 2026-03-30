package com.t_mobile.cassandra_crud.repository;

import com.t_mobile.cassandra_crud.entity.User;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface UserRepository extends CassandraRepository<User, Integer> {
}