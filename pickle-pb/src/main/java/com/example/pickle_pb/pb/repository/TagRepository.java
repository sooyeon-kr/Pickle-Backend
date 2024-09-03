package com.example.pickle_pb.pb.repository;

import com.example.pickle_pb.pb.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {
}
