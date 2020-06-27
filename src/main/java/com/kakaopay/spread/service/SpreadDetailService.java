package com.kakaopay.spread.service;

import com.kakaopay.spread.entity.SpreadDetail;
import com.kakaopay.spread.repository.SpreadDetailRepository;
import com.kakaopay.spread.vo.ReceiveUserResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpreadDetailService {

    private final SpreadDetailRepository spreadDetailRepository;

    public List<ReceiveUserResponseVO> findReceiveUsers(Long spreadId) {
        List<SpreadDetail> spreadDetails = spreadDetailRepository.findBySpreadIdAndReceiveTrue(spreadId);
        List<ReceiveUserResponseVO> receiveUsers = spreadDetails.stream().map(row -> ReceiveUserResponseVO.builder()
                .userId(row.getUserId())
                .money(row.getMoney())
                .build()).collect(Collectors.toList());

        return receiveUsers;
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
