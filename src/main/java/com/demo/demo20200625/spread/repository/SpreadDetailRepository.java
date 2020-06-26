package com.demo.demo20200625.spread.repository;

import com.demo.demo20200625.spread.domain.SpreadDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpreadDetailRepository extends JpaRepository<SpreadDetail, String> {

    List<SpreadDetail> findBySpreadIdAndGaveTrue(Long spreadId);
}
