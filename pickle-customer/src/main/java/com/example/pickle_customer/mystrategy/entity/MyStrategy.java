package com.example.pickle_customer.mystrategy.entity;

import com.example.pickle_customer.entity.Account;
import com.example.real_common.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MyStrategy extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "my_strategy_id")
    private int Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "selected_strategy")
    private int selectedStrategyId;

    private String name;
}
