package com.kakaopay.spread.controller;

import com.kakaopay.spread.code.SpreadCode;
import com.kakaopay.spread.entity.Spread;
import com.kakaopay.spread.exception.HttpForbiddenException;
import com.kakaopay.spread.exception.HttpUnauthorizedException;
import com.kakaopay.spread.service.SpreadDetailService;
import com.kakaopay.spread.service.SpreadService;
import com.kakaopay.spread.vo.GaveResponseVO;
import com.kakaopay.spread.vo.GaveUserVO;
import com.kakaopay.spread.vo.SpreadCreateRequestVO;
import com.kakaopay.spread.vo.SpreadCreateResponseVO;
import com.kakaopay.spread.vo.SpreadSearchResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/kakaopay/spread")
public class SpreadController {

    private final SpreadService spreadService;
    private final SpreadDetailService spreadDetailService;

    @GetMapping("/{token}")
    public SpreadSearchResponseVO search(@RequestHeader(value = SpreadCode.X_USER_ID) Long userId,
                                         @RequestHeader(value = SpreadCode.X_ROOM_ID) String roomId,
                                         @PathVariable String token) {

        Spread spread = spreadService.findByRoomIdAndToken(roomId, token);

        if (spread.getUserId().compareTo(userId) != 0L) {
            throw new HttpUnauthorizedException();
        }

        LocalDateTime currentLocalDateTime = LocalDateTime.now();

        if (spread.getCreateDate().plusDays(7L).compareTo(currentLocalDateTime) < 0) {
            throw new HttpUnauthorizedException();
        }

        List<GaveUserVO> list = spreadDetailService.findGaveUsers(spread.getId());
        int giveMoney = list.stream().mapToInt(GaveUserVO::getMoney).sum();
        String spreadDate = spread.getCreateDate().format(DateTimeFormatter.ofPattern(SpreadCode.DATETIME_FORMAT));

        return SpreadSearchResponseVO.builder()
                .spreadDate(spreadDate)
                .money(spread.getMoney())
                .giveMoney(giveMoney)
                .receivedUsers(list)
                .build();
    }

    @PostMapping
    public SpreadCreateResponseVO spread(@RequestHeader(value = SpreadCode.X_USER_ID) Long userId,
                                         @RequestHeader(value = SpreadCode.X_ROOM_ID) String roomId,
                                         @RequestBody SpreadCreateRequestVO spreadCreateRequestVO) {

        Spread spread = Spread.builder()
                .userId(userId)
                .roomId(roomId)
                .count(spreadCreateRequestVO.getCount())
                .money(spreadCreateRequestVO.getMoney())
                .build();

        spreadService.create(spread);

        return SpreadCreateResponseVO.builder()
                .token(spread.getToken())
                .build();
    }

    @PutMapping("/{token}")
    public GaveResponseVO gave(@RequestHeader(value = SpreadCode.X_USER_ID) Long userId,
                               @RequestHeader(value = SpreadCode.X_ROOM_ID) String roomId,
                               @PathVariable String token) {

        Spread spread = spreadService.findByRoomIdAndToken(roomId, token);

        if (spread.getCreateDate().plusMinutes(10).compareTo(LocalDateTime.now()) < 0) {
            throw new HttpUnauthorizedException();
        }

        Long spreadId = spread.getId();
        boolean gave = spreadDetailService.existsByIdAndUserIdGaveTrue(spreadId, userId);

        if (gave) {
            throw new HttpForbiddenException();
        }

        int money = spreadDetailService.gaveMoney(spreadId, userId);

        return GaveResponseVO
                .builder()
                .money(money)
                .build();
    }

}
