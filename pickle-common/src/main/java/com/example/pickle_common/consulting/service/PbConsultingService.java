package com.example.pickle_common.consulting.service;


import com.example.pickle_common.consulting.dto.ConsultingDetailResponse;
import com.example.pickle_common.consulting.dto.ConsultingRejectInfoDto;
import com.example.pickle_common.consulting.dto.PBConsultingResponse;
import com.example.pickle_common.consulting.dto.RejectConsultingRequest;
import com.example.pickle_common.consulting.entity.ConsultingHistory;
import com.example.pickle_common.consulting.entity.ConsultingRejectInfo;
import com.example.pickle_common.consulting.entity.ConsultingStatusEnum;
import com.example.pickle_common.consulting.entity.RequestLetter;
import com.example.pickle_common.consulting.repository.ConsultingHistoryRepository;
import com.example.pickle_common.consulting.repository.ConsultingRejectInfoRepository;
import com.example.pickle_common.consulting.repository.RequestLetterRepository;
import com.example.pickle_common.mq.MessageQueueService;
import com.example.real_common.config.RabbitMQConfig;
import com.example.real_common.global.exception.error.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    public List<PBConsultingResponse> getAllConsultingReservations(String authorizationHeader) {
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
    public List<PBConsultingResponse> getAllConsultingHistories(String authorizationHeader) {
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
    public List<PBConsultingResponse> getConsultingReservationsByStatus(String authorizationHeader, List<Integer> statusCodes) {
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
    public List<PBConsultingResponse> getConsultingHistoriesRequestedStatus(String authorizationHeader, List<Integer> statusCodes) {
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
    public List<PBConsultingResponse> getConsultingHistoriesByStatus(String authorizationHeader, List<ConsultingStatusEnum> statuses) {
        List<PBConsultingResponse> pBConsultingResponses = new ArrayList<>();
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

                PBConsultingResponse pBConsultingResponse = PBConsultingResponse.builder()
                        .requestLetterId(requestLetter.getId())
                        .customerId(consultingHistory.getId())
                        .customerName(consultingHistory.getCustomerName())
                        .date(consultingHistory.getDate())
                        .createdAt(consultingHistory.getCreatedAt())
                        .status(ConsultingStatusEnum.valueOf(String.valueOf(consultingHistory.getConsultingStatusName())))
                        .consultingRejectInfo(consultingRejectInfo != null ?
                                new ConsultingRejectInfoDto(consultingRejectInfo.getContent(), consultingRejectInfo.getCreatedAt()) : null)
                        .build();

                pBConsultingResponses.add(pBConsultingResponse);
            }
        } catch (Exception e) {
            log.error("상담 내역 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new UnexpectedServiceException("상담 내역 조회 중 예기치 않은 오류가 발생했습니다.", e);
        }
        return pBConsultingResponses;
    }

    public ConsultingDetailResponse getConsultingDetail(String authorizationHeader, int requestLetterId){
        ConsultingDetailResponse response;

        int pbId = messageQueueService.getPbIdByPbToken(authorizationHeader);

        if(pbId == RabbitMQConfig.INVALID_VALUE ){
            throw new UnexpectedServiceException("접근 권한이 없는 사용자입니다.");
        }

        RequestLetter requestLetter = requestLetterRepository.findById(requestLetterId).orElseThrow(() ->  new NotFoundRequestLetterException("요청서를 찾을 수 없습니다."));
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

        return response;
    }

    protected boolean sendMessageToMQ(ConsultingHistory consultingHistory) {
    try {
        Map<String, String> message = new HashMap<>();
        message.put("consultingHistoryId", String.valueOf(consultingHistory.getId()));
        message.put("roomId", consultingHistory.getRoomId());
        message.put("date", String.valueOf(consultingHistory.getDate()));
        message.put("pbBranchOffice", consultingHistory.getPbBranchOffice());
        message.put("customerName", consultingHistory.getCustomerName());
        message.put("customerId", String.valueOf(consultingHistory.getCustomerId()));
        message.put("pbImage", consultingHistory.getPbImage());
        message.put("pbName", consultingHistory.getPbName());
        message.put("pbId", String.valueOf(consultingHistory.getPbId()));
        message.put("statue", consultingHistory.getConsultingStatusName().name());

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonMessage = objectMapper.writeValueAsString(message);

        if(!messageQueueService.sendMessage(jsonMessage)){
            return false;
        }
        return true;
    } catch (JsonProcessingException e) {
        return false;
//        throw new UnableToSendRoomInfoToMqException("상담룸을 생성할 수 없습니다.");
    }

    }

    @Transactional
    public int rejectConsultingReservation(String authorizationHeader, int requestLetterId, RejectConsultingRequest rejectConsultingRequest) {

        int pbId = messageQueueService.getPbIdByPbToken(authorizationHeader);

        if(pbId == RabbitMQConfig.INVALID_VALUE ){
            throw new UnexpectedServiceException("접근 권한이 없는 사용자입니다.");
        }

        RequestLetter requestLetter = requestLetterRepository.findById(requestLetterId).orElseThrow(() ->  new NotFoundRequestLetterException("요청서를 찾을 수 없습니다."));
        ConsultingHistory consultingHistory = consultingHistoryRepository.findById(requestLetter.getConsultingHistory().getId())
                .orElseThrow(() -> new NotFoundConsultingHistoryException("요청서에 해당하는 상담 내역을 찾을 수 없습니다."));

        consultingHistory.changeStatus(ConsultingStatusEnum.REJECTED);
        consultingHistoryRepository.save(consultingHistory);

        ConsultingRejectInfo consultingRejectInfo = ConsultingRejectInfo.builder()
                .consultingHistory(consultingHistory)
                .content(rejectConsultingRequest.getContent())
                .build();

        ConsultingRejectInfo savedConsultingRejectInfo = consultingRejectInfoRepository.save(consultingRejectInfo);

        RequestLetter responseRequestLetter = requestLetterRepository.findByConsultingHistoryId(savedConsultingRejectInfo.getConsultingHistory().getId());
        if(responseRequestLetter.getId() == requestLetterId){
            return requestLetterId;
        }else{
            throw new UnableToCreateRejectedInfoException("요청에 대해 거절하지 못했습니다.");
        }

    }

    @Transactional
    public int completeConsulting(String authorizationHeader, int requestLetterId) {
        int pbId = messageQueueService.getPbIdByPbToken(authorizationHeader);

        if(pbId == RabbitMQConfig.INVALID_VALUE ){
            throw new UnexpectedServiceException("접근 권한이 없는 사용자입니다.");
        }

        RequestLetter requestLetter = requestLetterRepository.findById(requestLetterId).orElseThrow(() ->  new NotFoundRequestLetterException("요청서를 찾을 수 없습니다."));
        ConsultingHistory consultingHistory = consultingHistoryRepository.findById(requestLetter.getConsultingHistory().getId())
                .orElseThrow(() -> new NotFoundConsultingHistoryException("요청서에 해당하는 상담 내역을 찾을 수 없습니다."));

        consultingHistory.changeStatus(ConsultingStatusEnum.COMPLETED);
        consultingHistoryRepository.save(consultingHistory);

        if(consultingHistory.getConsultingStatusName() == ConsultingStatusEnum.COMPLETED){
            return requestLetterId;
        }else{
            throw new UnableToCreateRejectedInfoException("상담을 완료하지 못했습니다.");
        }
    }

//    TODO: mq에 상담룸 정보 보내주기
    @Transactional
    public int acceptConsultingReservation(String authorizationHeader, int requestLetterId) {
        int pbId = messageQueueService.getPbIdByPbToken(authorizationHeader);

        if(pbId == RabbitMQConfig.INVALID_VALUE ){
            throw new UnexpectedServiceException("접근 권한이 없는 사용자입니다.");
        }

        RequestLetter requestLetter = requestLetterRepository.findById(requestLetterId).orElseThrow(() ->  new NotFoundRequestLetterException("요청서를 찾을 수 없습니다."));
        ConsultingHistory consultingHistory = consultingHistoryRepository.findById(requestLetter.getConsultingHistory().getId())
                .orElseThrow(() -> new NotFoundConsultingHistoryException("요청서에 해당하는 상담 내역을 찾을 수 없습니다."));

        if(!sendMessageToMQ(requestLetter.getConsultingHistory())){
            throw new UnableToCreateRejectedInfoException("요청을 수락하지 못했습니다.");
        }

        consultingHistory.changeStatus(ConsultingStatusEnum.ACCEPTED);
        String randomRoomId = UUID.randomUUID().toString();
        consultingHistory.setRoomId(randomRoomId);
        consultingHistoryRepository.save(consultingHistory);

        if(consultingHistory.getRoomId().equals(randomRoomId)){
            return requestLetterId;
        }else{
            throw new UnableToCreateRejectedInfoException("요청을 수락하지 못했습니다.");
        }
    }
}
