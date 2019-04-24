package com.tddapps.rt.model.internal;

import com.tddapps.rt.model.Status;
import com.tddapps.rt.model.StatusRepository;

public class StatusRepositoryInMemory implements StatusRepository {
    @Override
    public Status CurrentStatus() {
        return null;
    }

    @Override
    public void Save(Status currentStatus) {

    }
}
