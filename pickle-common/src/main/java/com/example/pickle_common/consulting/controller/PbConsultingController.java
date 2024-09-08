package com.example.pickle_common.consulting.controller;

import com.example.pickle_common.consulting.dto.ConsultingDetailResponse;
import com.example.pickle_common.consulting.dto.ConsultingResponse;
import com.example.pickle_common.consulting.dto.RejectConsultingRequest;
import com.example.pickle_common.consulting.service.PbConsultingService;
import com.example.real_common.global.common.CommonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pickle-common/consulting/pb")
@Validated
@RequiredArgsConstructor
public class PbConsultingController {

    private final PbConsultingService pbConsultingService;

//    TODO: 요청서 목록 조회(수락 대기중인 사람, 내가 거절한 사람)
    @GetMapping("/request-letters")
    public CommonResDto<List<ConsultingResponse>> getConsultingReservations(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(name="status", required = false) List<Integer> status) {
        List<ConsultingResponse> reservations;
        if(status != null && !status.isEmpty()) {
            reservations = pbConsultingService.getConsultingReservationsByStatus(authorizationHeader, status);
        }else{
            reservations = pbConsultingService.getAllConsultingReservations(authorizationHeader);
        }

        CommonResDto<List<ConsultingResponse>> response = CommonResDto.<List<ConsultingResponse>>builder()
                .code(1)
                .message("상담 요청 목록 조회 성공")
                .data(reservations)
                .build();

        return response;
    }
//    TODO: 요청서 상세 조회
    @GetMapping("/request-letters/{requestLetterId}")
    public CommonResDto<ConsultingDetailResponse> getConsultingDetail(@RequestHeader("Authorization") String authorizationHeader, @PathVariable(name = "requestLetterId") int requestLetterId){
        ConsultingDetailResponse consultingDetailResponse;
        consultingDetailResponse = pbConsultingService.getConsultingDetail(authorizationHeader, requestLetterId);

        CommonResDto<ConsultingDetailResponse> response = CommonResDto.<ConsultingDetailResponse>builder()
                .code(1)
                .message("상담 상세 기록 조회 성공")
                .data(consultingDetailResponse)
                .build();

        return response;
    }


//    TODO: 요청 수락(상태를 ACCEPTED로 변경하고, roomId를 랜덤으로 넣어준 후, mq에 상담 정보를 넘겨줌) 정보 넘겨주는 건 나중에해도..

    @PostMapping("/request-letters/{requestLetterId}/accept")
    public CommonResDto<Integer> acceptConsultingReservation(@RequestHeader("Authorization") String authorizationHeader,@PathVariable("requestLetterId") int requestLetterId) {
        int responseRequestLetterId = pbConsultingService.acceptConsultingReservation(authorizationHeader, requestLetterId);
        CommonResDto<Integer> response = CommonResDto.<Integer>builder()
                .code(1)
                .message("상담을 수락하셨습니다.")
                .data(responseRequestLetterId)
                .build();

        return  response;
    }
//    TODO: 요청 거절(상태를 REJECTED로 변경하고, ConsultingRejectInfo를 생성하여 id를 넣어준다.)
    @PostMapping("/request-letters/{requestLetterId}/reject")
    public CommonResDto<Integer> rejectConsultingReservation(@RequestHeader("Authorization") String authorizationHeader,@PathVariable("requestLetterId") int requestLetterId,@RequestBody RejectConsultingRequest rejectConsultingRequest) {
        int responseRequestLetterId = pbConsultingService.rejectConsultingReservation(authorizationHeader, requestLetterId, rejectConsultingRequest);
        CommonResDto<Integer> response = CommonResDto.<Integer>builder()
                    .code(1)
                    .message("상담을 거절하셨습니다.")
                    .data(responseRequestLetterId)
                    .build();

        return  response;
    }

//    TODO: 상담 기록 조회
    @GetMapping("/histories")
    public CommonResDto<List<ConsultingResponse>> getConsultingCompleted(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(name="status", required = false) List<Integer> status) {
        List<ConsultingResponse> reservations;
        if(status != null && !status.isEmpty()) {
            reservations = pbConsultingService.getConsultingHistoriesRequestedStatus(authorizationHeader, status);
        }else{
            reservations = pbConsultingService.getAllConsultingHistories(authorizationHeader);
        }

        CommonResDto<List<ConsultingResponse>> response = CommonResDto.<List<ConsultingResponse>>builder()
                .code(1)
                .message("종료된 상담 조회 성공")
                .data(reservations)
                .build();

        return response;
    }


    @PostMapping("/request-letters/{requestLetterId}/complete")
    public CommonResDto<Integer> completeConsulting(@RequestHeader("Authorization") String authorizationHeader,@PathVariable("requestLetterId") int requestLetterId) {
        int responseRequestLetterId = pbConsultingService.completeConsulting(authorizationHeader, requestLetterId);
        CommonResDto<Integer> response = CommonResDto.<Integer>builder()
                .code(1)
                .message("상담을 완료했습니다.")
                .data(responseRequestLetterId)
                .build();

    return  response;
    }
}
