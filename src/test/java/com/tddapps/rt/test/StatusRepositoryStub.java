package com.tddapps.rt.test;

import com.tddapps.rt.model.Status;
import com.tddapps.rt.model.StatusRepository;

import java.util.function.Function;

public class StatusRepositoryStub implements StatusRepository {
    private Status currentStatus = null;

    @Override
    public Status CurrentStatus() {
        return currentStatus;
    }

    @Override
    public void Save(Status currentStatus) {
        this.currentStatus = currentStatus;
    }

    @Override
    public void Update(Function<Status, Status> statusUpdater) {
        currentStatus = statusUpdater.apply(currentStatus);
    }
}
