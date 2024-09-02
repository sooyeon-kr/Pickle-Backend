package com.example.pickle_pb.preset.repository;

import com.example.pickle_pb.preset.entity.Preset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresetRepository extends JpaRepository<Preset, Integer> {
}
