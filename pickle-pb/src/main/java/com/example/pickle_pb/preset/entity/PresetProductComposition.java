package com.example.pickle_pb.preset.entity;

import com.example.real_common.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PresetProductComposition extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_product_id")
    private int id;

    @Column(length = 10, nullable = false)
    private String code;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 30, nullable = false)
    private String categoryName;

    @Column(length = 20)
    private String themeName;

    @Column(nullable = false)
    private double ratio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_composition_id")
    private PresetCategoryComposition categoryComposition;
}
