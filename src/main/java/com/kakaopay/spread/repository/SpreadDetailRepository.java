package com.kakaopay.spread.repository;

import com.kakaopay.spread.entity.SpreadDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpreadDetailRepository extends JpaRepository<SpreadDetail, Long> {

    @Query(value = "SELECT d.* FROM Spread s "
            + " JOIN Spread_Detail d "
            + "   ON s.id = d.spread_id"
            + " WHERE s.id = ?1 AND NVL(d.user_id, 0) <> ?2 "
            + "   AND d.receive = false "
            + " ORDER BY rand() LIMIT 1  ", nativeQuery = true)
    Optional<SpreadDetail> getReceiveTarget(Long spreadId, Long userId);

    List<SpreadDetail> findBySpreadIdAndReceiveTrue(Long spreadId);

    boolean existsBySpreadIdAndUserIdAndReceiveTrue(Long spreadId, Long userId);

}
