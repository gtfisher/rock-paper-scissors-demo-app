package com.russmiles.antifragilesoftware.samples.es.store;

import com.russmiles.antifragilesoftware.samples.es.api.BaseEvent;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gtarrant-fisher on 13/05/2016.
 */
public class ListEventStream implements EventStream<Long> {
    private final long version;
    private final List<BaseEvent> baseEvents;

    public ListEventStream() {
        this.version = 0;
        baseEvents = Collections.emptyList();
    }

    public ListEventStream(long version, List<BaseEvent> baseEvents) {
        this.version = version;
        this.baseEvents = baseEvents;
    }

    public ListEventStream append(List<? extends BaseEvent> newEvents) {
        List<BaseEvent> baseEvents = new LinkedList<>(this.baseEvents);
        baseEvents.addAll(newEvents);
        return new ListEventStream(version+1, Collections.unmodifiableList(baseEvents));
    }

    @Override
    public Iterator<BaseEvent> iterator() {
        return baseEvents.iterator();
    }

    @Override
    public Long version() {
        return version;
    }

}
