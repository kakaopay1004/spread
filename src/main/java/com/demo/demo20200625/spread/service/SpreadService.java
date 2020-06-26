package com.demo.demo20200625.spread.service;

import com.demo.demo20200625.spread.domain.Spread;
import com.demo.demo20200625.spread.domain.SpreadDetail;
import com.demo.demo20200625.spread.repository.SpreadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpreadService {

    private final SpreadRepository spreadRepository;

    public void create(Spread spread) {
        spread.setToken(createRandomToken());

        addSpreadDetails(spread);

        spreadRepository.save(spread);

        log.info("spread getCreateDate : {}, getModifiedDate : {}", spread.getCreateDate(), spread.getModifiedDate());
        log.info("spread token : {}", spread.getToken());
    }

    private void addSpreadDetails(Spread spread) {
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

            log.info("money : {}, price : {}", spreadDetail.getMoney(), spreadMoney);
            spreadDetail.setSpread(spread);
            spread.getSpreadDetails().add(spreadDetail);
        }

    }

    private String createRandomToken() {
        StringBuilder stringbuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            int randomInt = random.nextInt(3);
            switch (randomInt) {
                case 0:
                    // a-z
                    stringbuilder.append((char) ((int) (Math.random() * 26) + 97));
                    break;
                case 1:
                    // A-Z
                    stringbuilder.append((char) ((int) (Math.random() * 26) + 65));
                    break;
                case 2:
                    // 0-9
                    stringbuilder.append(random.nextInt(10));
                    break;
            }
        }
        return stringbuilder.toString();
    }

    public Spread find(String roomId, String token) {
        return spreadRepository.findByRoomIdAndToken(roomId, token);
    }
}
