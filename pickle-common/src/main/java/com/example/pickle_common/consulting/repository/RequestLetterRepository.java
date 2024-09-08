package com.example.pickle_common.consulting.repository;

import com.example.pickle_common.consulting.entity.ConsultingHistory;
import com.example.pickle_common.consulting.entity.RequestLetter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestLetterRepository extends JpaRepository<RequestLetter, Integer> {
    RequestLetter findByConsultingHistoryId(int id);
}
