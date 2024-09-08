package com.example.pickle_common.consulting.controller;

import com.example.pickle_common.consulting.dto.CreateRequestLetterRequest;
import com.example.pickle_common.consulting.dto.CreateRequestLetterResponse;
import com.example.pickle_common.consulting.dto.ConsultingResponse;
import com.example.pickle_common.consulting.service.CustomerConsultingService;
import com.example.real_common.global.common.CommonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pickle-common/consulting/customer")
@Validated
@RequiredArgsConstructor
public class CustomerConsultingController {

    private final CustomerConsultingService customerConsultingService;

    @PostMapping("/request-letters")
    public ResponseEntity<CommonResDto<?>> createRequestLetter(@RequestHeader("Authorization") String authorizationHeader,@RequestBody CreateRequestLetterRequest requestDto) {
        CreateRequestLetterResponse result = customerConsultingService.createRequestLetter(authorizationHeader, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "요청서 생성 성공", result));
    }

    @GetMapping("/request-letters")
    public CommonResDto<List<ConsultingResponse>> getConsultingReservations(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(name="status", required = false) List<Integer> status) {
        List<ConsultingResponse> reservations;
        if(status != null && !status.isEmpty()) {
            reservations = customerConsultingService.getConsultingReservationsByStatus(authorizationHeader, status);
        }else{
            reservations = customerConsultingService.getAllConsultingReservations(authorizationHeader);
        }

        CommonResDto<List<ConsultingResponse>> response = CommonResDto.<List<ConsultingResponse>>builder()
                .code(1)
                .message("상담 요청 목록 조회 성공")
                .data(reservations)
                .build();

        return response;
    }

    @GetMapping("/histories")
    public CommonResDto<List<ConsultingResponse>> getConsultingHistories(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(name="status", required = false) List<Integer> status) {
        List<ConsultingResponse> histories;
        if(status != null && !status.isEmpty()) {
            histories = customerConsultingService.getConsultingHistoriesRequestedStatus(authorizationHeader, status);
        }else{
            histories = customerConsultingService.getAllConsultingHistories(authorizationHeader);
        }

        CommonResDto<List<ConsultingResponse>> response = CommonResDto.<List<ConsultingResponse>>builder()
                .code(1)
                .message("상담 내역 목록 조회 성공")
                .data(histories)
                .build();

        return response;
    }

   


}
