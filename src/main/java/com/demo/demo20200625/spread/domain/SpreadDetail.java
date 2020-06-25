package com.demo.demo20200625.spread.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class SpreadDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Spread spread;

    @Version
    @Column
    private int version;

    @Column
    private int money;

    @Column
    private boolean gave;

    @Column
    private LocalDateTime createDate;

    @Column
    private LocalDateTime updateDate;

}
