package com.example.pickle_common.consulting.service;

import com.example.pickle_common.consulting.dto.ConsultingRejectInfoDto;
import com.example.pickle_common.consulting.dto.CreateRequestLetterRequest;
import com.example.pickle_common.consulting.dto.CreateRequestLetterResponse;
import com.example.pickle_common.consulting.dto.CustomerConsultingResponse;
import com.example.pickle_common.consulting.entity.*;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerConsultingService {
    private static final Logger log = LoggerFactory.getLogger(CustomerConsultingService.class);
    private final RequestLetterRepository requestLetterRepository;
    private final ConsultingHistoryRepository consultingHistoryRepository;
    private final ConsultingRejectInfoRepository consultingRejectInfoRepository;
    private final MessageQueueService messageQueueService;

    @Transactional
    public CreateRequestLetterResponse createRequestLetter(String authorizationHeader, CreateRequestLetterRequest requestDto) {
        Instant start = Instant.now();

        String pbNumber = requestDto.getPbInfo().getPbNumber();
        int pbId = messageQueueService.getPbIdByPbNumberbySync(pbNumber);
        int customerId = messageQueueService.getCustomerIdByCustomerToken(authorizationHeader);
        String customerName = messageQueueService.getCustomerNameByCustomerToken(authorizationHeader);

        if (pbId == RabbitMQConfig.INVALID_VALUE || customerId == RabbitMQConfig.INVALID_VALUE || customerName.equals(RabbitMQConfig.UNKNOWN_CUSTOMER)) {
            throw new UnableToCreateRequestLetterDuoToMqFailure(String.format("{} {} {}", pbId, customerId, customerName));
        }
        ConsultingHistory consultingHistory = ConsultingHistory.builder()
                .customerId(customerId)
                .customerName(customerName)
                .pbId(pbId)
                .consultingStatusName(ConsultingStatusEnum.REQUESTED)
                .roomId(null)
                .pbName(requestDto.getPbInfo().getName())
                .pbBranchOffice(requestDto.getPbInfo().getBranchOffice())
                .pbImage(requestDto.getPbInfo().getImage())
                .date(requestDto.getDate())
                .customerName(customerName)
                .build();
        ConsultingHistory savedConsultingHistory = consultingHistoryRepository.save(consultingHistory);

        RequestLetter requestLetter = RequestLetter.builder()
                .consultingHistory(savedConsultingHistory)
                .customerId(customerId)
                .request(requestDto.getRequest())
                .answer1(AnswerType.fromValue(requestDto.getAnswer1()))
                .answer2(AnswerType.fromValue(requestDto.getAnswer2()))
                .answer3(AnswerType.fromValue(requestDto.getAnswer3()))
                .answer4(AnswerType.fromValue(requestDto.getAnswer4()))
                .availableInvestAmount(requestDto.getAvailableInvestAmount())
                .desiredInvestAmount(requestDto.getDesiredInvestAmount())
                .monthlyIncome(requestDto.getMonthlyIncome())
                .customerGender(GenderType.fromValue(requestDto.getCustomerInfo().getCustomerGender()))
                .customerAge(requestDto.getCustomerInfo().getCustomerAge())
                .customerJob(requestDto.getCustomerInfo().getCustomerJob())
                .referenceFileUrl(requestDto.getReferenceFileUrl())
                .build();

        RequestLetter savedRequestLetter = requestLetterRepository.save(requestLetter);

        Instant end = Instant.now();
        log.info("createRequestLetter execution time: {} ms", Duration.between(start, end).toMillis());

        return CreateRequestLetterResponse.builder()
                .requestLetterId(savedRequestLetter.getId())
                .build();
    }

    /**
     * 상담 예약을 가져오는 메소드(요청중, 거절됨)
     *
     * @param authorizationHeader
     * @return
     */
    public List<CustomerConsultingResponse> getAllConsultingReservations(String authorizationHeader) {
        Instant start = Instant.now();

        List<CustomerConsultingResponse> result = getConsultingHistoriesByStatus(authorizationHeader, Arrays.asList(
                ConsultingStatusEnum.REQUESTED,
                ConsultingStatusEnum.REJECTED
        ));

        Instant end = Instant.now();
        log.info("getAllConsultingReservations execution time: {} ms", Duration.between(start, end).toMillis());

        return result;
    }

    /**
     * 상태코드에 따라 상담 예약을 가져오는 메소드
     *
     * @param authorizationHeader
     * @param statusCodes
     * @return
     */
    public List<CustomerConsultingResponse> getConsultingReservationsByStatus(String authorizationHeader, List<Integer> statusCodes) {
        Instant start = Instant.now();

        List<ConsultingStatusEnum> statuses = statusCodes.stream()
                .map(ConsultingStatusEnum::fromCode)
                .collect(Collectors.toList());
        List<CustomerConsultingResponse> result = getConsultingHistoriesByStatus(authorizationHeader, statuses);

        Instant end = Instant.now();
        log.info("getConsultingReservationsByStatus execution time: {} ms", Duration.between(start, end).toMillis());

        return result;
    }

    /**
     * 모든 상담 기록을 가져오는 메소드
     * 현재는 COMPLETED 상태만 가져옴
     *
     * @param authorizationHeader
     * @return
     */
    public List<CustomerConsultingResponse> getAllConsultingHistories(String authorizationHeader) {
        Instant start = Instant.now();

        List<CustomerConsultingResponse> result = getConsultingHistoriesByStatus(authorizationHeader, Arrays.asList(
                ConsultingStatusEnum.COMPLETED
//                , ConsultingStatusEnum.REJECTED
//                , ConsultingStatusEnum.NO_SHOW
        ));

        Instant end = Instant.now();
        log.info("getAllConsultingHistories execution time: {} ms", Duration.between(start, end).toMillis());

        return result;
    }

    /***
     * 선택된 상태의 상담 기록만 가져오는 메소드
     * @param authorizationHeader
     * @param statusCodes
     * @return
     */
    public List<CustomerConsultingResponse> getConsultingHistoriesRequestedStatus(String authorizationHeader, List<Integer> statusCodes) {
        Instant start = Instant.now();

        List<ConsultingStatusEnum> statuses = statusCodes.stream()
                .map(ConsultingStatusEnum::fromCode)
                .collect(Collectors.toList());
        List<CustomerConsultingResponse> result = getConsultingHistoriesByStatus(authorizationHeader, statuses);

        Instant end = Instant.now();
        log.info("getConsultingHistoriesRequestedStatus execution time: {} ms", Duration.between(start, end).toMillis());

        return result;
    }

    /***
     * 상태에 따라 상담 기록을 가져오는 메소드(모든 메소드에서 사용)
     * @param authorizationHeader
     * @param statuses
     * @return
     */
    public List<CustomerConsultingResponse> getConsultingHistoriesByStatus(String authorizationHeader, List<ConsultingStatusEnum> statuses) {
        Instant start = Instant.now();

        List<CustomerConsultingResponse> consultingResponses = new ArrayList<>();
        try {
            int customerId = messageQueueService.getCustomerIdByCustomerToken(authorizationHeader);

            List<ConsultingHistory> consultingHistories = consultingHistoryRepository.findByCustomerIdAndConsultingStatusNameIn(
                    customerId,
                    statuses
            );

            for (ConsultingHistory consultingHistory : consultingHistories) {
                RequestLetter requestLetter = requestLetterRepository.findByConsultingHistoryId(consultingHistory.getId());
                ConsultingRejectInfo consultingRejectInfo = null;

                CustomerConsultingResponse consultingResponse = CustomerConsultingResponse.builder()
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

        Instant end = Instant.now();
        log.info("getConsultingHistoriesByStatus execution time: {} ms", Duration.between(start, end).toMillis());

        return consultingResponses;
    }
}