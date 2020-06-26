package com.demo.demo20200625.spread.service;

import com.demo.demo20200625.spread.domain.SpreadDetail;
import com.demo.demo20200625.spread.repository.SpreadDetailRepository;
import com.demo.demo20200625.spread.vo.GaveUserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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

        spreadDetails.stream().forEach(row -> {
            GaveUserVO gaveUserVO = GaveUserVO.builder()
                    .userId(row.getUserId())
                    .money(row.getMoney())
                    .build();

            gaveUsers.add(gaveUserVO);
        });

        return gaveUsers;
    }

    public boolean gaveMoney(Long spreadId, Long userId) {
        SpreadDetail spreadDetail = spreadDetailRepository.getGaveTarget(spreadId, userId);

        if (ObjectUtils.isEmpty(spreadDetail)) {

            return false;
        }

        spreadDetail.setUserId(userId);
        spreadDetail.setGave(true);
        spreadDetail.setGaveDate(LocalDateTime.now());

        return true;
    }

    public boolean existsByIdAndUserIdGaveTrue(Long spreadId, Long userId) {
        return spreadDetailRepository.existsByIdAndUserIdAndGaveTrue(spreadId, userId);
    }
}
