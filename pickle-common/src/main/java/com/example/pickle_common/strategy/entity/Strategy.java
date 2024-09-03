package com.example.pickle_common.strategy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Strategy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int strategyId;

    private int pbId;
    private int customerId;
    private String name;

    @Column(name = "consulting_history_id")
    private int consultingHistoryId; //아직 ConsultingHistory entity가 없다
}
