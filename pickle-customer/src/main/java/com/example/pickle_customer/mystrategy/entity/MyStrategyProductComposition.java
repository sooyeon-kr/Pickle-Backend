package com.example.pickle_customer.mystrategy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyStrategyProductComposition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_composition_id")
    private int id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_composition_id")
    private MyStrategyCategoryComposition categoryComposition;

    private String productCode;
    private String productName;
    private String categoryName;
    private String themeName;
    private double ratio;
}
