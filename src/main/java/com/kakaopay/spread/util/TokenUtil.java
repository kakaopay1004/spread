package com.kakaopay.spread.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class TokenUtil {

    public String createRandomToken() {
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

}
