package com.example.pickle_pb.pb.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PbJoinDto {

    @Column(length = 15, nullable = false, unique = true)
    private String pbNumber;
    @Column(nullable = false)
    private String password;

    @Column(length = 15, nullable = false)
    private String phoneNumber;
    @Column(length = 15, nullable = false)
    private String branchOffice;
    @Column(length = 30, nullable = false)
    private String username;
}
