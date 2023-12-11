package com.wl39.portfolio.stopwatch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StopwatchRepository extends JpaRepository<Stopwatch, Long> {
    @Query(value = "SELECT * FROM Stopwatch s WHERE s.relatedID = ?1", nativeQuery = true)
    List<Stopwatch> findRelatedStopwatchByRelatedID(Long relatedID);
    Page<Stopwatch> findAll(Pageable pageable);
}
