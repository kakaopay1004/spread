package com.kakaopay.spread.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.spread.code.SpreadCode;
import com.kakaopay.spread.entity.Spread;
import com.kakaopay.spread.service.SpreadService;
import com.kakaopay.spread.vo.ReceiveResponseVO;
import com.kakaopay.spread.vo.SpreadCreateRequestVO;
import com.kakaopay.spread.vo.SpreadCreateResponseVO;
import com.kakaopay.spread.vo.SpreadSearchResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Rollback
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class SpreadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpreadService spreadService;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 뿌린 사람 자신만 조회를 할 수 있습니다.
     */
    @Test
    void 조회() throws Exception {
        Spread spread = createSpread();

        MvcResult result = mockMvc.perform(get(String.format("/kakaopay/spread/%s", spread.getToken()))
                .header(SpreadCode.X_USER_ID, spread.getUserId())
                .header(SpreadCode.X_ROOM_ID, spread.getRoomId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        SpreadSearchResponseVO responseVO = objectMapper.readValue(result.getResponse().getContentAsString(), SpreadSearchResponseVO.class);
        assertTrue(responseVO.getReceivedUsers().isEmpty());
    }

    /**
     * 뿌린건 에 대한 조회는 7일 동안 할 수 있습니다.
     */
    @Test
    void 조회_7일지난경우() throws Exception {

        mockMvc.perform(get(String.format("/kakaopay/spread/%s", "DT7"))
                .header(SpreadCode.X_USER_ID, 1004)
                .header(SpreadCode.X_ROOM_ID, "room-1000-02")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();

    }

    @Test
    void 조회_결과가없는경우() throws Exception {

        mockMvc.perform(get("/kakaopay/spread/aaa")
                .header(SpreadCode.X_USER_ID, "1004")
                .header(SpreadCode.X_ROOM_ID, "room-486-02")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

    }

    @Test
    void 조회_작성자가_아닌경우() throws Exception {
        Spread spread = createSpread();

        mockMvc.perform(get(String.format("/kakaopay/spread/%s", spread.getToken()))
                .header(SpreadCode.X_USER_ID, 9999)
                .header(SpreadCode.X_ROOM_ID, spread.getRoomId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andReturn();

    }

    /**
     * token은 3자리 문자열로 구성되며 예측이 불가능해야 합니다.
     */
    @Test
    void 뿌리기_성공() throws Exception {

        SpreadCreateRequestVO spreadCreateRequestVO = SpreadCreateRequestVO.builder()
                .count(10)
                .money(1000)
                .build();

        String reqeustBody = objectMapper.writeValueAsString(spreadCreateRequestVO);

        MvcResult result = mockMvc.perform(post("/kakaopay/spread")
                .header(SpreadCode.X_USER_ID, "1004")
                .header(SpreadCode.X_ROOM_ID, "room1004")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqeustBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        SpreadCreateResponseVO responseVO = objectMapper.readValue(result.getResponse().getContentAsString(), SpreadCreateResponseVO.class);
        assertEquals(StringUtils.length(responseVO.getToken()), 3);

    }

    @Test
    void 뿌리기_유저아이디가숫자가인경우() throws Exception {

        SpreadCreateRequestVO spreadCreateRequestVO = SpreadCreateRequestVO.builder()
                .count(10)
                .money(1000)
                .build();

        String reqeustBody = objectMapper.writeValueAsString(spreadCreateRequestVO);

        mockMvc.perform(post("/kakaopay/spread")
                .header(SpreadCode.X_USER_ID, "dwfefe")
                .header(SpreadCode.X_ROOM_ID, "room1004")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqeustBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void 받기() throws Exception {

        Spread spread = createSpread();

        String token = spread.getToken();
        MvcResult result = mockMvc.perform(put("/kakaopay/spread/" + token)
                .header(SpreadCode.X_USER_ID, "1002")
                .header(SpreadCode.X_ROOM_ID, "room-486-02")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        ReceiveResponseVO responseVO = objectMapper.readValue(result.getResponse().getContentAsString(), ReceiveResponseVO.class);
        assertNotNull(responseVO.getMoney());
    }

    private Spread createSpread() {
        Spread spread = Spread.builder()
                .count(10)
                .money(1000)
                .roomId("room-486-02")
                .userId(1004L)
                .token("1id")
                .build();

        spreadService.create(spread);
        return spread;
    }

    @Test
    public void 받기_정상() throws Exception {

        Spread spread = createSpread();

        String token = spread.getToken();
        MvcResult result = mockMvc.perform(put("/kakaopay/spread/" + token)
                .header(SpreadCode.X_USER_ID, "1002")
                .header(SpreadCode.X_ROOM_ID, "room-486-02")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        ReceiveResponseVO responseVO = objectMapper.readValue(result.getResponse().getContentAsString(), ReceiveResponseVO.class);
        assertNotNull(responseVO.getMoney());
    }

    /**
     * 뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기
     * 실패 응답이 내려가야 합니다.
     */
    @Test
    public void 받기_10분지난경우() throws Exception {

        mockMvc.perform(put(String.format("/kakaopay/spread/%s", "J63"))
                .header(SpreadCode.X_USER_ID, 9999)
                .header(SpreadCode.X_ROOM_ID, "room-1000-02")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();

    }

    /**
     * 뿌리기 당한 사용자는 한번 만 받을 수 있습니다.
     */
    @Test
    public void 받기_1회_가능여부() throws Exception {

        Spread spread = createSpread();

        String token = spread.getToken();
        String roomId = spread.getRoomId();

        mockMvc.perform(put("/kakaopay/spread/" + token)
                .header(SpreadCode.X_USER_ID, "1002")
                .header(SpreadCode.X_ROOM_ID, roomId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


        mockMvc.perform(put("/kakaopay/spread/" + token)
                .header(SpreadCode.X_USER_ID, "1002")
                .header(SpreadCode.X_ROOM_ID, roomId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();

    }

    /**
     * 자신이 뿌리기한 건은 자신이 받을 수 없습니다.
     */
    @Test
    public void 자기가_뿌린건은_받을_수_없다() throws Exception {

        Spread spread = createSpread();

        String token = spread.getToken();
        String roomId = spread.getRoomId();

        mockMvc.perform(put("/kakaopay/spread/" + token)
                .header(SpreadCode.X_USER_ID, spread.getUserId())
                .header(SpreadCode.X_ROOM_ID, roomId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

    }

}