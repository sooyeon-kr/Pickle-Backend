package com.example.pickle_common.strategy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CategoryComposition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryCompositionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "strategy_id")
    private Strategy strategy;

    private String categoryName;

    private double categoryRatio;

}
