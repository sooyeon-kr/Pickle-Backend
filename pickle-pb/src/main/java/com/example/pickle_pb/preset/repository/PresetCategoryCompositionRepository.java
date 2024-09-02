package com.example.pickle_pb.preset.repository;

import com.example.pickle_pb.preset.entity.Preset;
import com.example.pickle_pb.preset.entity.PresetCategoryComposition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PresetCategoryCompositionRepository extends JpaRepository<PresetCategoryComposition, Integer> {
    List<PresetCategoryComposition> findByPreset(Preset preset);
}
