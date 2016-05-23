package com.russmiles.antifragilesoftware.samples.es.store;


import com.russmiles.antifragilesoftware.samples.es.api.BaseEvent;

import java.util.List;
import java.util.UUID;
import rx.Observable;

public interface EventStore<V> {

    EventStream<Long> loadEventStream(UUID aggregateId);
    void store(UUID aggregateId, long version, List<? extends BaseEvent> events);
    Observable<BaseEvent> all();
}