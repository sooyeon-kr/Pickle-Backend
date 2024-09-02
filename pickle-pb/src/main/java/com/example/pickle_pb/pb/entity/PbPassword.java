package com.example.pickle_pb.pb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor()
@AllArgsConstructor
@Builder
public class PbPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pb_password_id")
    private int id;
    @Column(length = 15, nullable = false)
    private String pbNumber;

    private String password;
    @OneToOne
    @JoinColumn(name = "pb_id") // 외래키 설정
    private Pb pb; // Pb 엔티티와의 관계 설정

}
