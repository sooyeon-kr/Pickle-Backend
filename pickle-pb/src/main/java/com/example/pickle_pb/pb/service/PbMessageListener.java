package com.example.pickle_pb.pb.service;

import com.example.pickle_pb.pb.entity.Pb;
import com.example.pickle_pb.pb.repository.PbRepository;
import com.example.real_common.config.RabbitMQConfig;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PbMessageListener {

    private final PbRepository pbRepository;

    /***
     * 사번을 통해 pk를 구하는 메소드
     * @param pbNumber
     * @return pb의 PK, 없다면 -1 반환
     */
    @RabbitListener(queues = RabbitMQConfig.PB_NUMBER_TO_ID_CONVERSION_QUEUE)
    public Integer handlePbIdRequest(String pbNumber) {
        return pbRepository.findByPbNumber(pbNumber).map(Pb::getId).orElse(RabbitMQConfig.INVALID_PB_NUMBER);
    }


}
