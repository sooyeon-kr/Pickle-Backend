package com.example.pickle_common.consulting.repository;

import com.example.pickle_common.consulting.entity.ConsultingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultingHistoryRepository extends JpaRepository<ConsultingHistory, Integer> {
}
