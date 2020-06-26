package com.kakaopay.spread.controller;

import com.kakaopay.spread.code.SpreadCode;
import com.kakaopay.spread.vo.SpreadCreateRequestVO;
import com.kakaopay.spread.vo.SpreadCreateResponseVO;
import com.kakaopay.spread.vo.SpreadSearchResponseVO;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 조회() throws Exception {
        MvcResult result = mockMvc.perform(get("/kakaopay/spread/w2E")
                .header(SpreadCode.X_USER_ID, "486")
                .header(SpreadCode.X_ROOM_ID, "room-486-02")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        SpreadSearchResponseVO responseVO = objectMapper.readValue(result.getResponse().getContentAsString(), SpreadSearchResponseVO.class);
        assertTrue(responseVO.getReceivedUsers().isEmpty());
    }

    @Test()
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

        mockMvc.perform(get("/kakaopay/spread/w2E")
                .header(SpreadCode.X_USER_ID, "1004")
                .header(SpreadCode.X_ROOM_ID, "room-486-02")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();

    }

    @Test
    void 뿌리기() throws Exception {

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
    public void test () throws Exception {
        MvcResult result = mockMvc.perform(put("/kakaopay/spread/w2E")
                .header(SpreadCode.X_USER_ID, "1002")
                .header(SpreadCode.X_ROOM_ID, "room-486-02")
                .contentType(MediaType.APPLICATION_JSON))
//                .content(reqeustBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
}