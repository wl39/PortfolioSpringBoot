package com.wl39.portfolio.stopwatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StopwatchService {
    private final StopwatchRepository stopwatchRepository;

    @Autowired
    public StopwatchService(StopwatchRepository stopwatchRepository) {
        this.stopwatchRepository = stopwatchRepository;
    }

    public List<Stopwatch> getAllStopwatches() {
        return this.stopwatchRepository.findAll();
    }

    public Page<Stopwatch> getStopwatchesPage(Pageable pageable) {
//        Page<Stopwatch> stopwatches = stopwatchRepository.findAll(pageable);
        return stopwatchRepository.findAll(pageable);
    }

    public void uploadStopwatch(Stopwatch stopwatch) {
        this.stopwatchRepository.save(stopwatch);
    }

    public void deleteStopwatch(Long stopwatchID) {
        List<Stopwatch> stopwatches = this.stopwatchRepository.findRelatedStopwatchByRelatedID(stopwatchID);

        for (Stopwatch stopwatch : stopwatches) {
            if (!this.stopwatchRepository.existsById(stopwatch.getId())) {
                throw new IllegalStateException("Stopwatch with ID (" + stopwatch.getId() + ") does not exist");
            }

            this.stopwatchRepository.deleteById(stopwatch.getId());
        }

        if(!this.stopwatchRepository.existsById(stopwatchID)) {
            throw new IllegalStateException("Stopwatch with ID (" + stopwatchID + ") does not exist");
        }

        this.stopwatchRepository.deleteById(stopwatchID);
    }

    public void deleteRelatedStopwatch(Long relatedID) {
        List<Stopwatch> stopwatches = this.stopwatchRepository.findRelatedStopwatchByRelatedID(relatedID);

        for (Stopwatch stopwatch : stopwatches) {
            if (!this.stopwatchRepository.existsById(stopwatch.getId())) {
                throw new IllegalStateException("Stopwatch with ID (" + stopwatch.getId() + ") does not exist");
            }

            this.stopwatchRepository.deleteById(stopwatch.getId());
        }

        if (!this.stopwatchRepository.existsById(relatedID)) {
            throw new IllegalStateException("Stopwatch with ID (" + relatedID + ") does not exist");
        }

        this.stopwatchRepository.deleteById(relatedID);
    }
}
