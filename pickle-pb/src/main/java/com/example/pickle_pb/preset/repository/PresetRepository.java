package com.example.pickle_pb.preset.repository;

import com.example.pickle_pb.preset.entity.Preset;
import com.example.pickle_pb.presetGroup.entity.PresetGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PresetRepository extends JpaRepository<Preset, Integer> {
    List<Preset> findByPresetGroup(PresetGroup presetGroup);

    List<Preset> findAllByPresetGroup(PresetGroup group);
}
