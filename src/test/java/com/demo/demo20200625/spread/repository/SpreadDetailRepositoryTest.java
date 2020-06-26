package com.demo.demo20200625.spread.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpreadDetailRepositoryTest {

    @Autowired
    private SpreadDetailRepository spreadDetailRepository;

    @Test
    void getGaveTarget() {
        spreadDetailRepository.getGaveTarget( 1L, 2000L);
    }

}