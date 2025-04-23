package ba.unsa.etf.nbp.DonationPlatform.repository;

import ba.unsa.etf.nbp.DonationPlatform.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}
