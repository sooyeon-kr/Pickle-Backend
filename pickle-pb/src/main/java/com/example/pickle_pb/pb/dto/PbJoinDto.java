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
    private String pbNumber;
    private String password;
    private String phoneNumber;
    private String branchOffice;
    private String username;
}
