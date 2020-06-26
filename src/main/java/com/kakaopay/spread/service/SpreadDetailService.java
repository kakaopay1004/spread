package com.kakaopay.spread.service;

import com.kakaopay.spread.entity.SpreadDetail;
import com.kakaopay.spread.exception.HttpNotFoundException;
import com.kakaopay.spread.repository.SpreadDetailRepository;
import com.kakaopay.spread.vo.GaveUserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpreadDetailService {

    private final SpreadDetailRepository spreadDetailRepository;

    public List<GaveUserVO> findGaveUsers(Long spreadId) {
        List<SpreadDetail> spreadDetails = spreadDetailRepository.findBySpreadIdAndGaveTrue(spreadId);
        List<GaveUserVO> gaveUsers = new ArrayList<>();

        spreadDetails.forEach(row -> {

            GaveUserVO gaveUserVO = GaveUserVO.builder()
                    .userId(row.getUserId())
                    .money(row.getMoney())
                    .build();

            gaveUsers.add(gaveUserVO);

        });

        return gaveUsers;
    }

    public int gaveMoney(Long spreadId, Long userId) {

        SpreadDetail spreadDetail = spreadDetailRepository.getGaveTarget(spreadId, userId)
                .orElseThrow(HttpNotFoundException::new);

        spreadDetail.setUserId(userId);
        spreadDetail.setGave(true);
        spreadDetail.setGaveDate(LocalDateTime.now());

        spreadDetailRepository.save(spreadDetail);

        return spreadDetail.getMoney();
    }

    public boolean existsByIdAndUserIdGaveTrue(Long spreadId, Long userId) {
        return spreadDetailRepository.existsBySpreadIdAndUserIdAndGaveTrue(spreadId, userId);
    }
}
