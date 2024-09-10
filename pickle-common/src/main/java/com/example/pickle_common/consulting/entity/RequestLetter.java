package com.example.pickle_common.consulting.entity;

import com.example.real_common.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestLetter extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_letter_id")
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulting_history_id")
    private ConsultingHistory consultingHistory;

    private int customerId;
    private int customerAge;
    private String customerJob;

    @Enumerated(EnumType.STRING)
    private GenderType customerGender;

    private String request;
    @Enumerated(EnumType.STRING)
    private AnswerType answer1;

    @Enumerated(EnumType.STRING)
    private AnswerType answer2;

    @Enumerated(EnumType.STRING)
    private AnswerType answer3;

    @Enumerated(EnumType.STRING)
    private AnswerType answer4;

    private long availableInvestAmount;
    private long desiredInvestAmount;
    private long monthlyIncome;

    private String referenceFileUrl;
}
