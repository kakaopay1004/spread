package com.kakaopay.spread.service;

import com.kakaopay.spread.entity.Spread;
import com.kakaopay.spread.entity.SpreadDetail;
import com.kakaopay.spread.exception.HttpNotFoundException;
import com.kakaopay.spread.repository.SpreadRepository;
import com.kakaopay.spread.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpreadService {

    private final SpreadRepository spreadRepository;
    private final TokenUtil tokenUtil;

    public void create(Spread spread) {
        spread.setToken(tokenUtil.createRandomToken());

        double spreadMoney = spread.getMoney();

        for (int i = 0; i < spread.getCount(); i++) {
            SpreadDetail spreadDetail = new SpreadDetail();

            if (spreadMoney == 0) {
                spreadDetail.setMoney(0);
            } else {
                int money = (int) Math.ceil(spreadMoney / 2);
                spreadMoney = spreadMoney - money;
                spreadDetail.setMoney(money);
            }

            spreadDetail.setSpread(spread);
            spread.getSpreadDetails().add(spreadDetail);
        }

        spreadRepository.save(spread);

    }

    public Spread findByRoomIdAndToken(String roomId, String token) {

        return spreadRepository.findByRoomIdAndToken(roomId, token)
                .orElseThrow(HttpNotFoundException::new);

    }

}
