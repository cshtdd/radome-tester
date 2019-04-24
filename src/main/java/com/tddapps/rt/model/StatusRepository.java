package com.tddapps.rt.model;

public interface StatusRepository {
    Status CurrentStatus();
    void Save(Status currentStatus);
}
