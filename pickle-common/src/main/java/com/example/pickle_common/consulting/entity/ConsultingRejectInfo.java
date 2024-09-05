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
public class ConsultingRejectInfo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consulting_reject_info_id")
    private int id;

    @OneToOne(mappedBy = "consultingRejectInfo")
    private RequestLetter requestLetter;
    private String content;

}
