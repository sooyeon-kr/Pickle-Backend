package com.example.pickle_common.consulting.service;

import com.example.pickle_common.consulting.dto.ConsultingRejectInfoDto;
import com.example.pickle_common.consulting.dto.CreateRequestLetterRequest;
import com.example.pickle_common.consulting.dto.CreateRequestLetterResponse;
import com.example.pickle_common.consulting.dto.RequestHistoriesResponse;
import com.example.pickle_common.consulting.entity.*;
import com.example.pickle_common.consulting.repository.ConsultingConfirmDateRepository;
import com.example.pickle_common.consulting.repository.ConsultingHistoryRepository;
import com.example.pickle_common.consulting.repository.ConsultingRejectInfoRepository;
import com.example.pickle_common.consulting.repository.RequestLetterRepository;
import com.example.pickle_common.mq.MessageQueueService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerConsultingService {
    private static final Logger log = LoggerFactory.getLogger(CustomerConsultingService.class);
    private final RequestLetterRepository requestLetterRepository;
    private final ConsultingHistoryRepository consultingHistoryRepository;
    private final ConsultingConfirmDateRepository consultingConfirmDateRepository;
    private final ConsultingRejectInfoRepository consultingRejectInfoRepository;
    private final MessageQueueService messageQueueService;

    public CreateRequestLetterResponse createRequestLetter(String authorizationHeader, CreateRequestLetterRequest requestDto) {
        //상담기록이 먼저 생성 되어야 한다.
        /**
         * TODO 현재는 더미값임.
         * customerId 추가
         * pbId 추가
         * customername추가
         */
        // UUID를 사용해 난수 생성
        String pbNumber = requestDto.getPbInfo().getPbNumber();
        int pbId = messageQueueService.getPbIdByPbNumberbySync(pbNumber);
        int customerId = messageQueueService.getCustomerIdByCustomerToken(authorizationHeader);
        String customerName = messageQueueService.getCustomerNameByCustomerToken(authorizationHeader);

//        String randomString = UUID.randomUUID().toString();

        ConsultingHistory consultingHistory = ConsultingHistory.builder()
                .customerId(customerId)
                .customerName(customerName)
                .pbId(pbId)
                .consultingStatusName(ConsultingStatusEnum.REQUESTED)
                .roomId(null)
                .pbName(requestDto.getPbInfo().getName())
                .pbBranchOffice(requestDto.getPbInfo().getBranchOffice())
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

        return CreateRequestLetterResponse.builder()
                .requestLetterId(savedRequestLetter.getId())
                .build();
    }

    public List<RequestHistoriesResponse> getAllRequesetHistories(String authorizationHeader) {
        List<RequestHistoriesResponse> requestHistoriesResponses = new ArrayList<>();


        int customerId = messageQueueService.getCustomerIdByCustomerToken(authorizationHeader);
        List<ConsultingHistory> consultingHistories = consultingHistoryRepository.findAllByCustomerId(customerId);

        for (ConsultingHistory consultingHistory : consultingHistories) {
            RequestLetter requestLetter = requestLetterRepository.findByConsultingHistoryId(consultingHistory.getId());

            ConsultingRejectInfo consultingRejectInfo = null;
            if (consultingHistory.getConsultingStatusName().equals(ConsultingStatusEnum.REJECTED)) {
                consultingRejectInfo = consultingRejectInfoRepository.findByConsultingHistoryId(consultingHistory.getId());
            }

            RequestHistoriesResponse requestHistoriesResponse = RequestHistoriesResponse.builder()
                    .requestLetterId(requestLetter.getId())
                    .pbId(consultingHistory.getPbId())
                    .pbName(consultingHistory.getPbName())
                    .date(consultingHistory.getDate())
                    .createdAt(consultingHistory.getCreatedAt())
                    .status(consultingHistory.getConsultingStatusName())
                    .consultingRejectInfoInfo(consultingRejectInfo != null ?
                            new ConsultingRejectInfoDto(consultingRejectInfo.getContent(), consultingRejectInfo.getCreatedAt()) : null)
                    .build();

            requestHistoriesResponses.add(requestHistoriesResponse);
        }

        return requestHistoriesResponses;
    }

    public List<RequestHistoriesResponse> getRequestHistoriesByStatus(String authorizationHeader, int status) {
        //TODO: 상태값에 따라 목록 가져오기 구현
        return new ArrayList<>();
    }
}
