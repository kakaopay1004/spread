package com.kakaopay.spread.controller;

import com.kakaopay.spread.code.SpreadCode;
import com.kakaopay.spread.entity.Spread;
import com.kakaopay.spread.exception.HttpBadRequestException;
import com.kakaopay.spread.exception.HttpForbiddenException;
import com.kakaopay.spread.exception.HttpNotAccepTableException;
import com.kakaopay.spread.exception.HttpUnauthorizedException;
import com.kakaopay.spread.service.SpreadDetailService;
import com.kakaopay.spread.service.SpreadService;
import com.kakaopay.spread.vo.ReceiveResponseVO;
import com.kakaopay.spread.vo.ReceiveUserResponseVO;
import com.kakaopay.spread.vo.SpreadCreateRequestVO;
import com.kakaopay.spread.vo.SpreadCreateResponseVO;
import com.kakaopay.spread.vo.SpreadSearchResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
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
@Api(tags = "뿌리기 API")
public class SpreadController {

    private final SpreadService spreadService;
    private final SpreadDetailService spreadDetailService;

    @ApiOperation(value = "뿌리기 생성")
    @ApiImplicitParams({
            @ApiImplicitParam(name = SpreadCode.X_USER_ID, value = "회원 번호", required = true, dataType = "string", paramType = "header", defaultValue = "1004"),
            @ApiImplicitParam(name = SpreadCode.X_ROOM_ID, value = "방 번호", required = true, dataType = "string", paramType = "header", defaultValue = "room-1000-02")})
    @ApiResponses({
            @ApiResponse(code=200, message = "성공"),
            @ApiResponse(code=400, message = "잘못된 형식의 요청")
    })
    @PostMapping
    public SpreadCreateResponseVO spread(@RequestHeader(value = SpreadCode.X_USER_ID) Long userId,
                                         @RequestHeader(value = SpreadCode.X_ROOM_ID) String roomId,
                                         @RequestBody SpreadCreateRequestVO spreadCreateRequestVO) {

        Spread spread = Spread.builder()
                .userId(userId)
                .roomId(roomId)
                .count(spreadCreateRequestVO.getCount())
                .money(spreadCreateRequestVO.getMoney())
                .token(RandomStringUtils.randomAlphanumeric(3))
                .build();

        spreadService.create(spread);

        return SpreadCreateResponseVO.builder()
                .token(spread.getToken())
                .build();
    }

    @ApiOperation(value = "뿌리기 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = SpreadCode.X_USER_ID, value = "회원 번호", required = true, dataType = "string", paramType = "header", defaultValue = "1004"),
            @ApiImplicitParam(name = SpreadCode.X_ROOM_ID, value = "방 번호", required = true, dataType = "string", paramType = "header", defaultValue = "room-1000-02"),
            @ApiImplicitParam(name = "token", value = "spread token", required = true, dataType = "string", paramType = "path", defaultValue = "k3c")})
    @ApiResponses({
            @ApiResponse(code=200, message = "성공"),
            @ApiResponse(code=400, message = "잘못된 형식의 요청"),
            @ApiResponse(code=401, message = "권한 없음"),
            @ApiResponse(code=406, message = "조회 권한 없는 사용자의 요청")
    })
    @GetMapping("/{token}")
    public SpreadSearchResponseVO search(@RequestHeader(value = SpreadCode.X_USER_ID) Long userId,
                                         @RequestHeader(value = SpreadCode.X_ROOM_ID) String roomId,
                                         @PathVariable String token) {

        Spread spread = spreadService.findByRoomIdAndToken(roomId, token);

        if (spread.getUserId().compareTo(userId) != 0L) {
            throw new HttpNotAccepTableException();
        }

        LocalDateTime currentLocalDateTime = LocalDateTime.now();

        if (spread.getCreateDate().plusDays(7L).compareTo(currentLocalDateTime) < 0) {
            throw new HttpUnauthorizedException();
        }

        List<ReceiveUserResponseVO> receiveUsers = spreadDetailService.findReceiveUsers(spread.getId());
        int giveMoney = receiveUsers.stream().mapToInt(ReceiveUserResponseVO::getMoney).sum();
        String spreadDate = spread.getCreateDate().format(DateTimeFormatter.ofPattern(SpreadCode.DATETIME_FORMAT));

        return SpreadSearchResponseVO.builder()
                .spreadDate(spreadDate)
                .money(spread.getMoney())
                .giveMoney(giveMoney)
                .receivedUsers(receiveUsers)
                .build();
    }

    @ApiOperation(value = "뿌리기 받기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = SpreadCode.X_USER_ID, value = "회원 번호", required = true, dataType = "string", paramType = "header", defaultValue = "1004"),
            @ApiImplicitParam(name = SpreadCode.X_ROOM_ID, value = "방 번호", required = true, dataType = "string", paramType = "header", defaultValue = "room-1000-02"),
            @ApiImplicitParam(name = "token", value = "spread token", required = true, dataType = "string", paramType = "path", defaultValue = "e3y")})
    @ApiResponses({
            @ApiResponse(code=200, message = "성공"),
            @ApiResponse(code=400, message = "잘못된 형식의 요청"),
            @ApiResponse(code=401, message = "권한 없음"),
            @ApiResponse(code=403, message = "권한 부족 이미 받은 사용자"),
            @ApiResponse(code=409, message = "받기를 동시에 시행한 경우 동시성"),
            @ApiResponse(code=406, message = "헤더 또는 내용이 서버에서 받아들일 수 없는 요청")
    })
    @PutMapping("/{token}")
    public ReceiveResponseVO give(@RequestHeader(value = SpreadCode.X_USER_ID) Long userId,
                                  @RequestHeader(value = SpreadCode.X_ROOM_ID) String roomId,
                                  @PathVariable String token) {

        Spread spread = spreadService.findByRoomIdAndToken(roomId, token);

        if (spread.getUserId().equals(userId)) {
            throw new HttpBadRequestException();
        }

        if (spread.getCreateDate().plusMinutes(10).compareTo(LocalDateTime.now()) < 0) {
            throw new HttpUnauthorizedException();
        }

        Long spreadId = spread.getId();
        boolean receive = spreadDetailService.existsByIdAndUserIdReceiveTrue(spreadId, userId);

        if (receive) {
            throw new HttpForbiddenException();
        }

        int money = spreadDetailService.giveMoney(spreadId, userId);

        return ReceiveResponseVO
                .builder()
                .money(money)
                .build();
    }

}
