package com.tddapps.rt.model.internal;

import com.tddapps.rt.model.Status;
import com.tddapps.rt.model.StatusRepository;

public class StatusRepositoryInMemory implements StatusRepository {
    private Status status = null;

    @Override
    public Status CurrentStatus() {
        return status;
    }

    @Override
    public void Save(Status currentStatus) {
        status = currentStatus.toBuilder().build();
    }
}
