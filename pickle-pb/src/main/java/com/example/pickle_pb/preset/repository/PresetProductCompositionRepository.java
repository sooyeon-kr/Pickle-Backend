package com.example.pickle_pb.preset.repository;

import com.example.pickle_pb.preset.entity.PresetProductComposition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PresetProductCompositionRepository extends JpaRepository<PresetProductComposition, Integer> {
    List<PresetProductComposition> findByCategoryCompositionId(Integer categoryCompositionId);
}
