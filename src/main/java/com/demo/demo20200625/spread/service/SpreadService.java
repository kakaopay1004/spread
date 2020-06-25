package com.demo.demo20200625.spread.service;

import com.demo.demo20200625.spread.domain.Spread;
import com.demo.demo20200625.spread.domain.SpreadDetail;
import com.demo.demo20200625.spread.repository.SpreadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpreadService {

    private final SpreadRepository spreadRepository;

    public void create(Spread spread) {
        spread.setToken("test");

        SpreadDetail spreadDetail = new SpreadDetail();
        spreadDetail.setMoney(100);
        spreadDetail.setSpread(spread);
        spread.getSpreadDetails().add(spreadDetail);

        spreadRepository.save(spread);

        log.info("spread getCreateDate : {}, getModifiedDate : {}", spread.getCreateDate(), spread.getModifiedDate());
    }

}
