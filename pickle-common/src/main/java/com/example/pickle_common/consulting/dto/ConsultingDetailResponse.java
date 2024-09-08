package com.example.pickle_common.consulting.dto;

import com.example.pickle_common.consulting.entity.RequestLetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultingDetailResponse {

    private int requestLetterId;
    private LocalDateTime date; // 상담일자
    private int status;

    private int customerId;
    private String customerName;

    private RequestInfo requestInfo;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestInfo{

        private String request;

        private int answer1;
        private int answer2;
        private int answer3;
        private int answer4;
        private long availableInvestAmount;
        private long desiredInvestAmount;
        private long monthlyIncome;

        private CustomerInfo customerInfo;

        private String referenceFileUrl; // S3 URL

        @Getter
        @AllArgsConstructor
        @Builder
        @NoArgsConstructor
        public static class CustomerInfo {
            private int customerAge;
            private int customerGender;
            private String customerJob;
        }

        public static RequestInfo entityToDto(RequestLetter requestLetter){
            return RequestInfo.builder()
                    .request(requestLetter.getRequest())
                    .answer1(requestLetter.getAnswer1().getValue())
                    .answer2(requestLetter.getAnswer2().getValue())
                    .answer3(requestLetter.getAnswer3().getValue())
                    .answer4(requestLetter.getAnswer4().getValue())
                    .availableInvestAmount(requestLetter.getAvailableInvestAmount())
                    .desiredInvestAmount(requestLetter.getDesiredInvestAmount())
                    .monthlyIncome(requestLetter.getMonthlyIncome())
                    .customerInfo(CustomerInfo.builder()
                            .customerAge(requestLetter.getCustomerAge())
                            .customerGender(requestLetter.getCustomerGender().getValue())
                            .customerJob(requestLetter.getCustomerJob())
                            .build())
                    .referenceFileUrl(requestLetter.getReferenceFileUrl())
                    .build();
        }
    }

}
