package com.example.pickle_pb.pb.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Pb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pb_id")
    private Long id;
    @Column(length = 15, nullable = false, unique = true)
    private String pbNumber;
    @Column(length = 30, nullable = false)
    private String name;
    @Column(length = 15, nullable = false)
    private String phoneNumber;
    @Column(length = 15, nullable = false)
    private String branchOffice;
    @Column(length = 50, nullable = false)
    private String email;
    @Column(length = 255, nullable = false)
    private String introduction;
    private int consultingCount;
    private int transactionCount;
    private Long minConsultingAmount;
}
