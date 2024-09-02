package com.example.pickle_pb.pb.repository;

import com.example.pickle_pb.pb.entity.Pb;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PbRepository extends JpaRepository<Pb, Long> {
    Optional<Pb> findByUsername(String username);
    Optional<Pb> findByPbNumber(String PbNumber);
}
