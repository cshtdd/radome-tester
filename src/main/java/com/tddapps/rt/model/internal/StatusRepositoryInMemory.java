package com.tddapps.rt.model.internal;

import com.tddapps.rt.model.Status;
import com.tddapps.rt.model.StatusRepository;

import java.util.function.Function;

class StatusRepositoryInMemory implements StatusRepository {
    private final Object criticalSection = new Object();
    private Status status = null;

    @Override
    public Status read() {
        synchronized (criticalSection){
            return status;
        }
    }

    @Override
    public void save(Status status) {
        synchronized (criticalSection){
            this.status = status.toBuilder().build();
        }
    }

    @Override
    public void update(Function<Status, Status> statusUpdater) {
        synchronized (criticalSection){
            status = statusUpdater.apply(status);
        }
    }
}
