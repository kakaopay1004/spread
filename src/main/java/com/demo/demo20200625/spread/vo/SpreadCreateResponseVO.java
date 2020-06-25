package com.demo.demo20200625.spread.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpreadCreateResponseVO {

    @Builder.Default
    private String code = "200";
    private String token;

}
