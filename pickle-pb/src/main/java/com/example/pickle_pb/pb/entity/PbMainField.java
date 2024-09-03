package com.example.pickle_pb.pb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor()
@AllArgsConstructor
@Builder
public class PbMainField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pbMainFieldId;

    @ManyToOne
    @JoinColumn(name = "pb_id", referencedColumnName = "pb_id")
    private Pb pb;

    @ManyToOne
    @JoinColumn(name = "main_field_id", referencedColumnName = "mainFieldId")
    private MainField mainField;

    // 기본 생성자, 게터 및 세터 추가 가능
}

