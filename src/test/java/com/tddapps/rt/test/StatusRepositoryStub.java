package com.tddapps.rt.test;

import com.tddapps.rt.model.Status;
import com.tddapps.rt.model.StatusRepository;

import java.util.function.Function;

public class StatusRepositoryStub implements StatusRepository {
    private Status currentStatus = null;

    @Override
    public Status read() {
        return currentStatus;
    }

    @Override
    public void save(Status status) {
        this.currentStatus = status;
    }

    @Override
    public void update(Function<Status, Status> statusUpdater) {
        currentStatus = statusUpdater.apply(currentStatus);
    }
}
