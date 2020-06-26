package com.demo.demo20200625.spread.controller;

import com.demo.demo20200625.spread.code.SpreadCode;
import com.demo.demo20200625.spread.domain.Spread;
import com.demo.demo20200625.spread.exception.HttpUnauthorizedException;
import com.demo.demo20200625.spread.service.SpreadDetailService;
import com.demo.demo20200625.spread.service.SpreadService;
import com.demo.demo20200625.spread.vo.GaveUserVO;
import com.demo.demo20200625.spread.vo.ResponseVO;
import com.demo.demo20200625.spread.vo.SpreadCreateRequestVO;
import com.demo.demo20200625.spread.vo.SpreadCreateResponseVO;
import com.demo.demo20200625.spread.vo.SpreadSearchResponseVO;
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
    public SpreadSearchResponseVO 조회(@RequestHeader(value = SpreadCode.X_USER_ID) Long userId,
                                     @RequestHeader(value = SpreadCode.X_ROOM_ID) String roomId,
                                     @PathVariable String token) {

        Spread spread = getSpread(userId, roomId, token);

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

    private Spread getSpread(Long userId, String roomId, String token) {

        Spread spread = spreadService.findByRoomIdAndToken(roomId, token);
        checkValidSpread(userId, spread);

        return spread;
    }

    private void checkValidSpread(Long userId, Spread spread) {

        if (spread.getUserId().compareTo(userId) != 0L) {
            throw new HttpUnauthorizedException();
        }

        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        if (spread.getCreateDate().plusDays(7L).compareTo(currentLocalDateTime) < 0) {
            throw new HttpUnauthorizedException();
        }
    }

    @PostMapping
    public SpreadCreateResponseVO 뿌리기(@RequestHeader(value = SpreadCode.X_USER_ID) Long userId,
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

    @PutMapping
    public ResponseVO 받기() {
        return ResponseVO.builder().code("200").build();
    }

}
