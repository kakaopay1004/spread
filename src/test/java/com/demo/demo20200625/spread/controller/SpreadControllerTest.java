package com.demo.demo20200625.spread.controller;

import com.demo.demo20200625.spread.Demo20200625Application;
import com.demo.demo20200625.spread.code.SpreadCode;
import com.demo.demo20200625.spread.exception.ErrorResponseVO;
import com.demo.demo20200625.spread.vo.ResponseVO;
import com.demo.demo20200625.spread.vo.SpreadCreateRequestVO;
import com.demo.demo20200625.spread.vo.SpreadCreateResponseVO;
import com.demo.demo20200625.spread.vo.SpreadSearchResponseVO;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        MvcResult result = mockMvc.perform(get("/kakaopay/spread/tz6")
                .header(SpreadCode.X_USER_ID, "1004")
                .header(SpreadCode.X_ROOM_ID, "room-1004-02")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        SpreadSearchResponseVO responseVO = objectMapper.readValue(result.getResponse().getContentAsString(), SpreadSearchResponseVO.class);
        assertEquals(responseVO.getCode(), 200);
        assertEquals(responseVO.getMoney(), 105);
    }

    @Test
    void 조회_작성자가_아닌경우() throws Exception {
        MvcResult result = mockMvc.perform(get("/kakaopay/spread/tz6")
                .header(SpreadCode.X_USER_ID, "10533")
                .header(SpreadCode.X_ROOM_ID, "room-1004-02")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        SpreadSearchResponseVO responseVO = objectMapper.readValue(result.getResponse().getContentAsString(), SpreadSearchResponseVO.class);
        assertEquals(responseVO.getCode(), 901);
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
        assertTrue(responseVO.getToken().length() == 3);

    }
}