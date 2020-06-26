package com.kakaopay.spread.repository;

import com.kakaopay.spread.entity.Spread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpreadRepository extends JpaRepository<Spread, Long> {

    Optional<Spread> findByRoomIdAndToken(String roomId, String token);

}
