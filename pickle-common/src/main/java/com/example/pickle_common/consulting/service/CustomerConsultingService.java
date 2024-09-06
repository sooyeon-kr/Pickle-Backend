package com.example.pickle_common.consulting.service;

import com.example.pickle_common.consulting.dto.CreateRequestLetterRequestDto;
import com.example.pickle_common.consulting.dto.CreateRequestLetterResponseDto;
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

    public CreateRequestLetterResponseDto createRequestLetter(String authorizationHeader,CreateRequestLetterRequestDto requestDto) {
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
        String randomString = UUID.randomUUID().toString();
        ConsultingHistory consultingHistory = ConsultingHistory.builder()
                .customerId(customerId)
                .customerName(customerName)
                .pbId(pbId)
                .consultingStatusName(ConsultingStatusEnum.REQUESTED)
                .roomId(randomString)
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

        return CreateRequestLetterResponseDto.builder()
                .requestLetterId(savedRequestLetter.getId())
                .build();
    }
}
