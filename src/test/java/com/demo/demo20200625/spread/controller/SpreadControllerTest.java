package com.demo.demo20200625.spread.controller;

import com.demo.demo20200625.spread.Demo20200625Application;
import com.demo.demo20200625.spread.vo.ResponseVO;
import com.demo.demo20200625.spread.vo.SpreadCreateRequestVO;
import com.demo.demo20200625.spread.vo.SpreadCreateResponseVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = Demo20200625Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpreadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();;

    @Test
    void 조회() throws Exception {
        MvcResult result = mockMvc.perform(get("/kakaopay/spread")
                .header("userId", "0000")
                .contentType(MediaType.APPLICATION_JSON))
//                .content())
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ResponseVO responseVO = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseVO.class);

        assertEquals(responseVO.getCode(), "200");
    }

    @Test
    void 뿌리기() throws Exception {

        SpreadCreateRequestVO spreadCreateRequestVO = new SpreadCreateRequestVO();
        String reqeustBody = objectMapper.writeValueAsString(spreadCreateRequestVO);

        MvcResult result = mockMvc.perform(post("/kakaopay/spread")
                .header("X-USER-ID", "0000")
                .header("X-ROOM-ID", "0000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqeustBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        SpreadCreateResponseVO responseVO = objectMapper.readValue(result.getResponse().getContentAsString(), SpreadCreateResponseVO.class);
        assertEquals(responseVO.getCode(), "200");
        assertTrue(StringUtils.isNotBlank(responseVO.getToken()));

    }
}