package fileconverter.flowmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import fileconverter.flowmanager.model.ConversionJob;
import java.util.UUID;

@Repository
public interface ConversionJobRepository extends JpaRepository<ConversionJob, UUID> {
}
