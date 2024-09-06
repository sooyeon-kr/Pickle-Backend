package com.example.pickle_common.consulting.controller;

import com.example.pickle_common.consulting.dto.CreateRequestLetterRequest;
import com.example.pickle_common.consulting.dto.CreateRequestLetterResponse;
import com.example.pickle_common.consulting.dto.RequestHistoriesResponse;
import com.example.pickle_common.consulting.repository.ConsultingHistoryRepository;
import com.example.pickle_common.consulting.service.CustomerConsultingService;
import com.example.real_common.global.common.CommonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pickle-common/consulting/customer")
@Validated
@RequiredArgsConstructor
public class CustomerConsultingController {

    private final CustomerConsultingService customerConsultingService;
    private final ConsultingHistoryRepository consultingHistoryRepository;

    @PostMapping("/request-letters")
    public ResponseEntity<CommonResDto<?>> createRequestLetter(@RequestHeader("Authorization") String authorizationHeader,@RequestBody CreateRequestLetterRequest requestDto) {
        CreateRequestLetterResponse result = customerConsultingService.createRequestLetter(authorizationHeader, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "요청서 생성 성공", result));
    }

    @GetMapping("/request-letters")
    public CommonResDto<List<RequestHistoriesResponse>> getRequestHistories(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(name="status", required = false, defaultValue = "0") int status) {
        List<RequestHistoriesResponse> histories;
        if(status != 0) {
             histories = customerConsultingService.getRequestHistoriesByStatus(authorizationHeader, status);
        }else{
            histories = customerConsultingService.getAllRequesetHistories(authorizationHeader);
        }

        CommonResDto<List<RequestHistoriesResponse>> response = CommonResDto.<List<RequestHistoriesResponse>>builder()
                .code(1)
                .message("상담 요청 목록 조회 성공")
                .data(histories)
                .build();

        return response;
    }

   


}
