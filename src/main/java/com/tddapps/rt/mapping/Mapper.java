package com.tddapps.rt.mapping;

public interface Mapper {
    <Source, Dest> Dest map(Source src, Class<Dest> type);
}
