package com.example.pickle_common.consulting.repository;

import com.example.pickle_common.consulting.entity.ConsultingHistory;
import com.example.pickle_common.consulting.entity.ConsultingStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultingHistoryRepository extends JpaRepository<ConsultingHistory, Integer> {
    List<ConsultingHistory> findAllByCustomerId(int customerId);
    List<ConsultingHistory> findByCustomerIdAndConsultingStatusNameIn(int customerId, List<ConsultingStatusEnum> statuses);
}
