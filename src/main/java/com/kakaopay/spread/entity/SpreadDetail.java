package com.kakaopay.spread.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(indexes = {@Index(columnList = "user_id"), @Index(columnList = "spread_id")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"spread_id", "user_id"})})
public class SpreadDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Spread spread;

    @Column(name = "user_id")
    private Long userId;

    @Version
    private int version;

    private int money;

    private boolean receive;

    private LocalDateTime receiveDate;

    @Builder
    public SpreadDetail(Spread spread) {
        this.spread = spread;
    }

}
