package com.wl39.portfolio;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class PostTransactionTaskScheduler {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public void runAfterCommit(Runnable task, long delayMillis) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    scheduler.schedule(task, delayMillis, TimeUnit.MILLISECONDS);
                }
            });
        } else {
            task.run();
        }
    }
}
