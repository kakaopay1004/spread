package com.kakaopay.spread.service;

import com.kakaopay.spread.entity.SpreadDetail;
import com.kakaopay.spread.repository.SpreadDetailRepository;
import com.kakaopay.spread.vo.ReceiveUserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpreadDetailService {

    private final SpreadDetailRepository spreadDetailRepository;

    public List<ReceiveUserVO> findReceiveUsers(Long spreadId) {
        List<SpreadDetail> spreadDetails = spreadDetailRepository.findBySpreadIdAndReceiveTrue(spreadId);
        List<ReceiveUserVO> gaveUsers = new ArrayList<>();

        spreadDetails.forEach(row -> {

            ReceiveUserVO receiveUserVO = ReceiveUserVO.builder()
                    .userId(row.getUserId())
                    .money(row.getMoney())
                    .build();

            gaveUsers.add(receiveUserVO);

        });

        return gaveUsers;
    }

    public int giveMoney(Long spreadId, Long userId) {

        SpreadDetail spreadDetail = spreadDetailRepository.getReceiveTarget(spreadId, userId)
                .orElseThrow(EntityNotFoundException::new);

        spreadDetail.setUserId(userId);
        spreadDetail.setReceive(true);
        spreadDetail.setReceiveDate(LocalDateTime.now());

        spreadDetailRepository.save(spreadDetail);

        return spreadDetail.getMoney();
    }

    public boolean existsByIdAndUserIdReceiveTrue(Long spreadId, Long userId) {
        return spreadDetailRepository.existsBySpreadIdAndUserIdAndReceiveTrue(spreadId, userId);
    }
}
