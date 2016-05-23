package com.russmiles.antifragilesoftware.samples.es.store;

import com.russmiles.antifragilesoftware.samples.es.api.BaseEvent;

public interface EventStream<V> extends Iterable<BaseEvent> {
    V version();
}