package com.example.pickle_common.consulting.service;


import com.example.pickle_common.consulting.dto.ConsultingDetailResponse;
import com.example.pickle_common.consulting.dto.ConsultingRejectInfoDto;
import com.example.pickle_common.consulting.dto.ConsultingResponse;
import com.example.pickle_common.consulting.entity.ConsultingHistory;
import com.example.pickle_common.consulting.entity.ConsultingRejectInfo;
import com.example.pickle_common.consulting.entity.ConsultingStatusEnum;
import com.example.pickle_common.consulting.entity.RequestLetter;
import com.example.pickle_common.consulting.repository.ConsultingHistoryRepository;
import com.example.pickle_common.consulting.repository.ConsultingRejectInfoRepository;
import com.example.pickle_common.consulting.repository.RequestLetterRepository;
import com.example.pickle_common.mq.MessageQueueService;
import com.example.real_common.config.RabbitMQConfig;
import com.example.real_common.global.exception.error.UnableToCreateRequestLetterDuoToMqFailure;
import com.example.real_common.global.exception.error.UnexpectedServiceException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PbConsultingService {

    private static final Logger log = LoggerFactory.getLogger(CustomerConsultingService.class);
    private final RequestLetterRepository requestLetterRepository;
    private final ConsultingHistoryRepository consultingHistoryRepository;
    private final ConsultingRejectInfoRepository consultingRejectInfoRepository;
    private final MessageQueueService messageQueueService;

    /**
     * 상담 예약을 가져오는 메소드(요청중, 거절됨)
     * @param authorizationHeader
     * @return
     */
    public List<ConsultingResponse> getAllConsultingReservations(String authorizationHeader) {
        return getConsultingHistoriesByStatus(authorizationHeader, Arrays.asList(
                ConsultingStatusEnum.REQUESTED,
                ConsultingStatusEnum.REJECTED
        ));
    }

    /**
     * 모든 상담 기록을 가져오는 메소드
     * 현재는 COMPLETED 상태만 가져옴
     * @param authorizationHeader
     * @return
     */
    public List<ConsultingResponse> getAllConsultingHistories(String authorizationHeader) {
        return getConsultingHistoriesByStatus(authorizationHeader, Arrays.asList(
                ConsultingStatusEnum.COMPLETED
        ));
    }

    /**
     * 상태코드에 따라 상담 예약을 가져오는 메소드
     * @param authorizationHeader
     * @param statusCodes
     * @return
     */
    public List<ConsultingResponse> getConsultingReservationsByStatus(String authorizationHeader, List<Integer> statusCodes) {
        List<ConsultingStatusEnum> statuses = statusCodes.stream()
                .map(ConsultingStatusEnum::fromCode)
                .collect(Collectors.toList());
        return getConsultingHistoriesByStatus(authorizationHeader, statuses);
    }

    /***
     * 선택된 상태의 상담 기록만 가져오는 메소드
     * @param authorizationHeader
     * @param statusCodes
     * @return
     */
    public List<ConsultingResponse> getConsultingHistoriesRequestedStatus(String authorizationHeader, List<Integer> statusCodes) {
        List<ConsultingStatusEnum> statuses = statusCodes.stream()
                .map(ConsultingStatusEnum::fromCode)
                .collect(Collectors.toList());
        return getConsultingHistoriesByStatus(authorizationHeader, statuses);
    }

    /***
     * 상태에 따라 상담 기록을 가져오는 메소드(모든 메소드에서 사용)
     * @param authorizationHeader
     * @param statuses
     * @return
     */
    public List<ConsultingResponse> getConsultingHistoriesByStatus(String authorizationHeader, List<ConsultingStatusEnum> statuses) {
        List<ConsultingResponse> consultingResponses = new ArrayList<>();
        try {
            int pbId = messageQueueService.getPbIdByPbToken(authorizationHeader);

            List<ConsultingHistory> consultingHistories = consultingHistoryRepository.findByPbIdAndConsultingStatusNameIn(
                    pbId,
                    statuses
            );

            for (ConsultingHistory consultingHistory : consultingHistories) {
                RequestLetter requestLetter = requestLetterRepository.findByConsultingHistoryId(consultingHistory.getId());
                ConsultingRejectInfo consultingRejectInfo = null;

                if (ConsultingStatusEnum.REJECTED.name().equals(consultingHistory.getConsultingStatusName())) {
                    consultingRejectInfo = consultingRejectInfoRepository.findByConsultingHistoryId(consultingHistory.getId());
                }

                ConsultingResponse consultingResponse = ConsultingResponse.builder()
                        .requestLetterId(requestLetter.getId())
                        .pbId(consultingHistory.getPbId())
                        .pbName(consultingHistory.getPbName())
                        .date(consultingHistory.getDate())
                        .createdAt(consultingHistory.getCreatedAt())
                        .pbImage(consultingHistory.getPbImage())
                        .status(ConsultingStatusEnum.valueOf(String.valueOf(consultingHistory.getConsultingStatusName())))
                        .consultingRejectInfo(consultingRejectInfo != null ?
                                new ConsultingRejectInfoDto(consultingRejectInfo.getContent(), consultingRejectInfo.getCreatedAt()) : null)
                        .build();

                consultingResponses.add(consultingResponse);
            }
        } catch (Exception e) {
            log.error("상담 내역 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new UnexpectedServiceException("상담 내역 조회 중 예기치 않은 오류가 발생했습니다.", e);
        }
        return consultingResponses;
    }

    public ConsultingDetailResponse getConsultingDetail(String authorizationHeader, int requestLetterId){
        ConsultingDetailResponse response;
        try{
            int pbId = messageQueueService.getPbIdByPbToken(authorizationHeader);

            if(pbId == RabbitMQConfig.INVALID_VALUE ){
                throw new UnexpectedServiceException("접근 권한이 없는 사용자입니다.");
            }

            RequestLetter requestLetter = requestLetterRepository.findById(requestLetterId).orElseThrow();
            if(requestLetter.getConsultingHistory().getPbId() != pbId){
                throw new UnexpectedServiceException("상담 내역에 접근 권한이 없습니다.");
            }

            response = ConsultingDetailResponse.builder()
                    .requestLetterId(requestLetterId)
                    .customerId(requestLetter.getCustomerId())
                    .customerName(requestLetter.getConsultingHistory().getCustomerName())
                    .date(requestLetter.getConsultingHistory().getDate())
                    .status(requestLetter.getConsultingHistory().getConsultingStatusName().getCode())
                    .requestInfo(ConsultingDetailResponse.RequestInfo.entityToDto(requestLetter))
                    .build();
        } catch (Exception e) {
            log.error("상담 내역 상세 조회 중 예외 발생: {}", e.getMessage(), e);
            throw new UnexpectedServiceException("상담 상세 내역 조회 중 예기치 않은 예외가 발생했습니다.", e);
        }

        return response;
    }
}
