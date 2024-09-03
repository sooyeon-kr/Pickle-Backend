package com.example.pickle_pb.pb.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PbProfileRequestDto {

    private String email;
    private String introduction;
    private int consultingCount;
    private int transactionCount;
    private Long minConsultingAmount;
    private List<String> mainFields;
    private List<String> tags;

}
