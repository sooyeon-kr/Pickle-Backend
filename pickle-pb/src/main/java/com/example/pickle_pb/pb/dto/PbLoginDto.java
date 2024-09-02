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
public class PbLoginDto {

    @Column(length = 15, nullable = false)
    private String pbNumber;
    @Column(length = 45, nullable = false)
    private String passWord;
}
