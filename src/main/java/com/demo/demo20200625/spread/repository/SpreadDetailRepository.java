package com.demo.demo20200625.spread.repository;

import com.demo.demo20200625.spread.domain.SpreadDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpreadDetailRepository extends JpaRepository<SpreadDetail, Long> {

    List<SpreadDetail> findBySpreadIdAndGaveTrue(Long spreadId);

    @Query(value = "SELECT d.* FROM Spread s "
            + " JOIN Spread_Detail d "
            + "   ON s.id = d.spread_id"
            + " WHERE s.id = ?1 AND NVL(d.user_id, 0) <> ?2 "
            + "   AND d.gave = false "
            + " ORDER BY rand() LIMIT 1  ", nativeQuery = true)
    SpreadDetail getGaveTarget(Long spreadId, Long userId);

    boolean existsByIdAndUserIdAndGaveTrue(Long spreadId, Long userId);
}
