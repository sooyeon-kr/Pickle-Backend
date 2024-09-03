package com.example.pickle_pb.pb.repository;

import com.example.pickle_pb.pb.entity.MainField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MainFieldRepository extends JpaRepository<MainField, Integer> {
    @Query("SELECT COALESCE(MAX(p.mainId), 0) FROM PbMainField p")
    int findMaxMainId();
}
