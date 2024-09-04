package com.example.pickle_common.consulting.entity;

import com.example.real_common.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultingConfirmDate extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consulting_confirm_date_id")
    private int id;

    @OneToOne
    @JoinColumn(name = "consulting_history_id")
    private ConsultingHistory consultingHistory;

    private int customerId;
    private int pbId;

    private Date date;
}
