package com.example.pickle_common.consulting.entity;

import com.example.pickle_common.strategy.entity.Strategy;
import com.example.real_common.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultingHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consulting_history_id")
    private int id;

    private int pbId;
    private String pbName;
    private String pbBranchOffice;

    private int customerId;
    private String customerName;


    private String roomId;
    private String pbImage;
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private ConsultingStatusEnum consultingStatusName;

    // 양방향이 꼭 필요한지 확인을 해봐야한다.
    // OneToOne 양방향 매핑에서는 fetch LAZY 적용이 안되는 상황이 많다.
    @OneToOne(mappedBy = "consultingHistory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RequestLetter requestLetter;
//
//    @OneToOne(mappedBy = "consultingHistory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private ConsultingConfirmDate consultingConfirmDate;

    // 전략과 양방향 매핑
//    @OneToOne(mappedBy = "consultingHistory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Strategy strategy;
}
