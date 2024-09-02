package com.example.pickle_common.strategy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductComposition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productCompositionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    private String name;
    private String code;

    private double ratio;
    private String themeName;
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_composition_id")
    private CategoryComposition categoryComposition;
}
