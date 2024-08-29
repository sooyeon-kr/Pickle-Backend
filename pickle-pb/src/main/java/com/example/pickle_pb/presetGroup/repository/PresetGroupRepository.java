package com.example.pickle_pb.presetGroup.repository;

import com.example.pickle_pb.presetGroup.entity.PresetGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PresetGroupRepository extends JpaRepository<PresetGroup, Long> {
    List<String> findAllById(Long id);
}
