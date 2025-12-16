package spring_micro.flow_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring_micro.flow_manager.model.ConversionJob;
import java.util.UUID;

@Repository
public interface ConversionJobRepository extends JpaRepository<ConversionJob, UUID> {
}
