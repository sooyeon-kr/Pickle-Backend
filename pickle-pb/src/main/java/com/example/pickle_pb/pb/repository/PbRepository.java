package com.example.pickle_pb.pb.repository;

import com.example.pickle_pb.pb.entity.Pb;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PbRepository extends JpaRepository<Pb, Integer> {
    Optional<Pb> findByUsername(String username);
    Optional<Pb> findByPbNumber(String PbNumber);
    Optional<Pb> findById(Integer id);

    @Query("SELECT DISTINCT pb FROM Pb pb " +
            "JOIN pb.pbMainFields pmf " +
            "JOIN pb.pbTags pt " +
            "WHERE (:mainFields IS NULL OR pmf.mainField.name IN :mainFields) " +
            "AND (:tags IS NULL OR pt.tag.name IN :tags) " +
            "AND (:minConsultingAmount IS NULL OR pb.minConsultingAmount >= :minConsultingAmount)")
    List<Pb> findByFilters(@Param("mainFields") List<String> mainFields,
                           @Param("tags") List<String> tags,
                           @Param("minConsultingAmount") Long minConsultingAmount);
}
