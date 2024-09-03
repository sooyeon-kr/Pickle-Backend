package com.example.pickle_pb.pb.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pb_id")
    private Long id;
    @Column(nullable = false)
    private String password;

    @Column(name = "pb_number", length = 15, nullable = false, unique = true)
    private String pbNumber;
    @Column(length = 30, nullable = false)
    private String username;
    @Column(length = 15, nullable = false)
    private String phoneNumber;
    @Column(length = 15, nullable = false)
    private String branchOffice;


    private String email;
    private String introduction;
    private int consultingCount;
    private int transactionCount;
    private Long minConsultingAmount;
    @OneToMany(mappedBy = "pb", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PbMainField> pbMainFields;

    @OneToMany(mappedBy = "pb", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PbTag> pbTags;


}
