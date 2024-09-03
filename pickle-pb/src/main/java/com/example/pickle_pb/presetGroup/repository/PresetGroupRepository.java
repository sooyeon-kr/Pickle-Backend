package com.example.pickle_pb.presetGroup.repository;

import com.example.pickle_pb.presetGroup.entity.PresetGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PresetGroupRepository extends JpaRepository<PresetGroup, Integer> {
    List<PresetGroup> findAllById(Integer id);

    List<PresetGroup> findAllByPbId(Integer pbId);

}
