package com.kakaopay.spread.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpreadDetailRepositoryTest {

    @Autowired
    private SpreadDetailRepository spreadDetailRepository;

    @Test
    void testGetGaveTarget() {
        spreadDetailRepository.getReceiveTarget( 1L, 2000L);
    }

    @Test
    void testExistsByIdAndUserIdAndGaveTrue() {
        spreadDetailRepository.existsBySpreadIdAndUserIdAndReceiveTrue(1L, 2999L);
    }
}