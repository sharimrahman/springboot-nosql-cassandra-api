package com.t_mobile.cassandra_crud;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CassandraCrudApplicationTest {

    @Test
    void contextLoads() {
        // This will load full Spring context
    }

    @Test
    void mainMethodTest() {
        CassandraCrudApplication.main(new String[] {});
    }
}