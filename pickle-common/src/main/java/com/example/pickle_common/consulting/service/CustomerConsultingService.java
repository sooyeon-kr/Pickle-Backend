package com.example.pickle_common.consulting.service;

import com.example.pickle_common.consulting.dto.CreateRequestLetterRequestDto;
import com.example.pickle_common.consulting.dto.CreateRequestLetterResponseDto;
import com.example.pickle_common.consulting.entity.AnswerType;
import com.example.pickle_common.consulting.entity.ConsultingHistory;
import com.example.pickle_common.consulting.entity.ConsultingStatusEnum;
import com.example.pickle_common.consulting.entity.RequestLetter;
import com.example.pickle_common.consulting.repository.ConsultingConfirmDateRepository;
import com.example.pickle_common.consulting.repository.ConsultingHistoryRepository;
import com.example.pickle_common.consulting.repository.ConsultingRejectInfoRepository;
import com.example.pickle_common.consulting.repository.RequestLetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerConsultingService {
    private final RequestLetterRepository requestLetterRepository;
    private final ConsultingHistoryRepository consultingHistoryRepository;
    private final ConsultingConfirmDateRepository consultingConfirmDateRepository;
    private final ConsultingRejectInfoRepository consultingRejectInfoRepository;

    public CreateRequestLetterResponseDto createRequestLetter(CreateRequestLetterRequestDto requestDto) {
        //상담기록이 먼저 생성 되어야 한다.
        /**
         * TODO 현재는 더미값임.
         * customerId 추가
         * pbId 추가
         * customername추가
         */
        // UUID를 사용해 난수 생성
        String randomString = UUID.randomUUID().toString();
        ConsultingHistory consultingHistory = ConsultingHistory.builder()
                .customerId(0)
                .pbId(0)
                .consultingStatusName(ConsultingStatusEnum.REQUESTED)
                .roomId(randomString)
                .pbName(requestDto.getPbInfo().getName())
                .pbBranchOffice(requestDto.getPbInfo().getBranchOffice())
                .customerName("이름들어가야함")
                .build();
        ConsultingHistory savedConsultingHistory = consultingHistoryRepository.save(consultingHistory);

        // 이제 요청서 생성
        // 여기도 id값 제대로 넣어줘야함
        RequestLetter requestLetter = RequestLetter.builder()
                .consultingHistory(savedConsultingHistory)
                .customerId(0)
                .request(requestDto.getRequest())
                .answer1(AnswerType.fromValue(requestDto.getAnswer1()))
                .answer2(AnswerType.fromValue(requestDto.getAnswer2()))
                .answer3(AnswerType.fromValue(requestDto.getAnswer3()))
                .answer4(AnswerType.fromValue(requestDto.getAnswer4()))
                .availableInvestAmount(requestDto.getAvailableInvestAmount())
                .desiredInvestAmount(requestDto.getDesiredInvestAmount())
                .monthlyIncome(requestDto.getMonthlyIncome())
                .build();
        RequestLetter savedRequestLetter = requestLetterRepository.save(requestLetter);

        return CreateRequestLetterResponseDto.builder()
                .requestLetterId(savedRequestLetter.getId())
                .build();
    }
}
