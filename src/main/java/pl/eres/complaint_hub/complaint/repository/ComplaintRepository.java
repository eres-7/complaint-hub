package pl.eres.complaint_hub.complaint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.eres.complaint_hub.complaint.entity.Complaint;

import java.util.Optional;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    @Query("SELECT c.id FROM Complaint c WHERE c.productId = :productId AND c.reporter = :reporter")
    Optional<Long> findIdByProductIdAndReporter(Long productId, String reporter);

    @Modifying
    @Query(value = """
        INSERT INTO complaint (product_id, reporter, content, country, created_at, count)
        VALUES (:#{#c.productId}, :#{#c.reporter}, :#{#c.content}, :#{#c.country}, :#{#c.createdAt}, :#{#c.count})
        ON CONFLICT (product_id, reporter)
        DO UPDATE SET count = complaint.count + 1
        """, nativeQuery = true)
    void upsertComplaint(@Param("c") Complaint complaint);

    @Modifying
    @Query("UPDATE Complaint c SET c.content = :content WHERE c.id = :id")
    void updateComplaintContent(Long id, String content);
}