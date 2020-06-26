package com.demo.demo20200625.spread.service;

import com.demo.demo20200625.spread.domain.SpreadDetail;
import com.demo.demo20200625.spread.repository.SpreadDetailRepository;
import com.demo.demo20200625.spread.vo.GaveUserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            GaveUserVO gaveUserVO  = GaveUserVO.builder()
                    .userId(row.getUserId())
                    .money(row.getMoney())
                    .build();

            gaveUsers.add(gaveUserVO);
        });

        return gaveUsers;
    }
}
