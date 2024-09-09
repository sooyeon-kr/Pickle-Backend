package com.example.pickle_pb.pb.service;

import com.example.pickle_pb.pb.auth.JwtService;
import com.example.pickle_pb.pb.entity.Pb;
import com.example.pickle_pb.pb.repository.PbRepository;
import com.example.real_common.config.RabbitMQConfig;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
public class PbMessageListener {

    private final PbRepository pbRepository;
    private final JwtService jwtService;


    /***
     * 사번을 통해 pk를 구하는 메소드
     * @param pbNumber
     * @return pb의 PK, 없다면 -1 반환
     */
    @RabbitListener(queues = RabbitMQConfig.PB_NUMBER_TO_ID_CONVERSION_QUEUE)
    public Integer handlePbIdRequest(String pbNumber) {
        try {
            // pbNumber가 null인지 확인
            if (pbNumber == null) {
                log.error("pbNumber is null");
                return RabbitMQConfig.INVALID_PB_NUMBER;
            }
            // pbRepository에서 pbNumber를 찾고 id를 반환
            return pbRepository.findByPbNumber(pbNumber)
                    .map(Pb::getId)
                    .orElse(RabbitMQConfig.INVALID_PB_NUMBER);
        } catch (Exception e) {
            // 예외가 발생할 경우 로깅
            log.error("Error in handlePbIdRequest: " + e.getMessage(), e);
            return RabbitMQConfig.INVALID_PB_NUMBER;
        }
    }

    /**
     * pb토큰을 통해 pbPK를 구하는 메소드
     * @param pbToken
     * @return pb의 PK, 없다면 -1 반환
     */
    @RabbitListener(queues = RabbitMQConfig.PB_TOKEN_TO_ID_CONVERSION_QUEUE)
    public Integer handlePbTokenRequest(String pbToken) {
        try {
            jwtService.validateToken(pbToken);
            String username = jwtService.extractUsername(pbToken);
            return Integer.parseInt(username);
        } catch (NumberFormatException e) {
            log.error("Invalid number format in token: " + e.getMessage());
            return RabbitMQConfig.INVALID_TOKEN;
        } catch (JwtException e) {  // JWT 관련 예외 처리
            log.error("JWT validation failed: " + e.getMessage());
            return RabbitMQConfig.INVALID_TOKEN;
        } catch (Exception e) {  // 기타 예외 처리
            log.error("Unexpected error in handlePbTokenRequest: " + e.getMessage());
            return RabbitMQConfig.INVALID_TOKEN;
        }
    }



}
