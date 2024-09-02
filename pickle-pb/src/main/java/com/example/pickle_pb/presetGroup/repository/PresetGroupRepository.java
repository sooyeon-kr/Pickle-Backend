package com.example.pickle_pb.presetGroup.repository;

import com.example.pickle_pb.presetGroup.entity.PresetGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PresetGroupRepository extends JpaRepository<PresetGroup, Integer> {
    List<String> findAllById(Integer id);
}
