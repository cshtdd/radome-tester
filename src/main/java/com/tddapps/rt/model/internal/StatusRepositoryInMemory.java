package com.tddapps.rt.model.internal;

import com.tddapps.rt.model.Status;
import com.tddapps.rt.model.StatusRepository;

class StatusRepositoryInMemory implements StatusRepository {
    private final Object criticalSection = new Object();
    private Status status = null;

    @Override
    public Status CurrentStatus() {
        synchronized (criticalSection){
            return status;
        }
    }

    @Override
    public void Save(Status currentStatus) {
        synchronized (criticalSection){
            status = currentStatus.toBuilder().build();
        }
    }
}
