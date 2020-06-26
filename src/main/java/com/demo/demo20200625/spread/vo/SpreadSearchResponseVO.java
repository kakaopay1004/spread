package com.demo.demo20200625.spread.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SpreadSearchResponseVO {

    private String spreadDate;

    private int money;

    private int giveMoney;

    private List<GaveUserVO> receivedUsers;

    @Builder.Default
    private int code = 200;

}
