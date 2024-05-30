package ch.hslu.url_shortener.repositories;

import ch.hslu.url_shortener.entities.UrlStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UrlStatisticsRepository extends JpaRepository<UrlStatistics, UUID> {
    Optional<UrlStatistics> findByUrlId(UUID urlId);
}
