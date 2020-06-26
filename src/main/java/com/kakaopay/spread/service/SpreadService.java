package com.kakaopay.spread.service;

import com.kakaopay.spread.entity.Spread;
import com.kakaopay.spread.entity.SpreadDetail;
import com.kakaopay.spread.repository.SpreadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpreadService {

    private final SpreadRepository spreadRepository;

    public void create(Spread spread) {

        double spreadMoney = spread.getMoney();
        List<SpreadDetail> spreadDetails = spread.getSpreadDetails();

        for (int i = 0; i < spread.getCount(); i++) {
            SpreadDetail spreadDetail = new SpreadDetail();

            if (spreadMoney != 0) {

                int money = (int) Math.ceil(spreadMoney / 2);
                spreadMoney = spreadMoney - money;
                spreadDetail.setMoney(money);

            }

            spreadDetail.setSpread(spread);
            spreadDetails.add(spreadDetail);
        }

        spreadRepository.save(spread);

    }

    public Spread findByRoomIdAndToken(String roomId, String token) {

        return spreadRepository.findByRoomIdAndToken(roomId, token)
                .orElseThrow(EntityNotFoundException::new);

    }

}
