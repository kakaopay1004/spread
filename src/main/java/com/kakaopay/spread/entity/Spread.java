package com.kakaopay.spread.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"room_id", "token"})})
public class Spread extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "room_id", nullable = false)
    private String roomId;

    private int money;

    private int count;

    @Setter
    @Column(nullable = false)
    private String token;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "spread")
    private List<SpreadDetail> spreadDetails = new ArrayList<>();

    @Builder
    public Spread(Long userId, String roomId, int money, int count, String token) {
        this.userId = userId;
        this.roomId = roomId;
        this.money = money;
        this.count = count;
        this.token = token;
    }

}
