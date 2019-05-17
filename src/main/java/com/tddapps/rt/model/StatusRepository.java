package com.tddapps.rt.model;

import java.util.function.Function;

public interface StatusRepository {
    Status read();
    void save(Status status);
    void update(Function<Status, Status> statusUpdater);
}
