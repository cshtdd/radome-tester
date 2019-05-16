package com.tddapps.rt.model;

import java.util.function.Function;

public interface StatusRepository {
    Status CurrentStatus();
    void Save(Status currentStatus);
    void Update(Function<Status, Status> statusUpdater);
}
