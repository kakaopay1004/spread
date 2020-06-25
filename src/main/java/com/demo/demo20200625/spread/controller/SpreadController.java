package com.demo.demo20200625.spread.controller;

import com.demo.demo20200625.spread.code.SpreadCode;
import com.demo.demo20200625.spread.domain.Spread;
import com.demo.demo20200625.spread.service.SpreadService;
import com.demo.demo20200625.spread.vo.ResponseVO;
import com.demo.demo20200625.spread.vo.SpreadCreateRequestVO;
import com.demo.demo20200625.spread.vo.SpreadCreateResponseVO;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/kakaopay/spread")
public class SpreadController {

    private final SpreadService spreadService;

    @GetMapping("/{token}")
    public ResponseVO 조회(@RequestHeader(value = SpreadCode.X_USER_ID) String userId,
                         @RequestHeader(value = SpreadCode.X_ROOM_ID) String roomId,
                         @PathVariable String token) {
        log.info("test {}", 1);
        spreadService.find(roomId, token);
        return ResponseVO.builder().code("200").build();
    }

    @PostMapping
    public SpreadCreateResponseVO 뿌리기(@RequestHeader(value = SpreadCode.X_USER_ID) String userId,
                                      @RequestHeader(value = SpreadCode.X_ROOM_ID) String roomId,
                                      @RequestBody SpreadCreateRequestVO spreadCreateRequestVO) {

        Spread spread = Spread.builder()
                .userId(userId)
                .roomId(roomId)
                .count(spreadCreateRequestVO.getCount())
                .money(spreadCreateRequestVO.getMoney())
                .build();

        spreadService.create(spread);

        log.info("token : {}", spread.getToken());

        return SpreadCreateResponseVO.builder()
                .token(spread.getToken())
                .build();
    }

    @PutMapping
    public ResponseVO 받기() {
        return ResponseVO.builder().code("200").build();
    }

}
