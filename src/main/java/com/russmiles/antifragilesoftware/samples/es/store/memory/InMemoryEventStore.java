package com.russmiles.antifragilesoftware.samples.es.store.memory;

import com.russmiles.antifragilesoftware.samples.es.api.BaseEvent;
import com.russmiles.antifragilesoftware.samples.es.store.EventStore;
import com.russmiles.antifragilesoftware.samples.es.store.EventStream;
import com.russmiles.antifragilesoftware.samples.es.store.ListEventStream;
import rx.Observable;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gtarrant-fisher on 12/05/2016.
 */

public class InMemoryEventStore implements EventStore<Long> {
    private final Map<UUID, ListEventStream> streams = new ConcurrentHashMap<UUID, ListEventStream>();
    private final TreeSet<Transaction> transactions = new TreeSet<Transaction>();

    @Override
    public ListEventStream loadEventStream(UUID aggregateId) {
        ListEventStream eventStream = streams.get(aggregateId);
        if (eventStream == null) {
            eventStream = new ListEventStream();
            streams.put(aggregateId, eventStream);
        }
        return eventStream;
    }

    @Override
    public void store(UUID aggregateId, long version, List<? extends BaseEvent> events) {
        ListEventStream stream = loadEventStream(aggregateId);
        if (stream.version() != version) {
            throw new ConcurrentModificationException("Stream has already been modified");
        }
        streams.put(aggregateId, stream.append(events));
        synchronized (transactions) {
            transactions.add(new Transaction(events));
        }
    }

    public EventStream<Long> loadEventsAfter(Long timestamp) {
        // include all baseEvents after this timestamp, except the baseEvents with the current timestamp
        // since new baseEvents might be added with the current timestamp
        List<BaseEvent> baseEvents = new LinkedList<BaseEvent>();
        long now;
        synchronized (transactions) {
            now = System.currentTimeMillis();
            for (Transaction t : transactions.tailSet(new Transaction(timestamp)).headSet(new Transaction(now))) {
                baseEvents.addAll(t.events);
            }
        }
        return new ListEventStream(now-1, baseEvents);
    }

    @Override
    public Observable<BaseEvent> all() {
        throw new UnsupportedOperationException();
    }

}



class Transaction implements Comparable<Transaction> {
    public final List<? extends BaseEvent> events;
    private final long timestamp;

    public Transaction(long timestamp) {
        events = Collections.emptyList();
        this.timestamp = timestamp;

    }
    public Transaction(List<? extends BaseEvent> events) {
        this.events = events;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public int compareTo(Transaction other) {
        if (timestamp < other.timestamp) {
            return -1;
        } else if (timestamp > other.timestamp) {
            return 1;
        }
        return 0;
    }
}
