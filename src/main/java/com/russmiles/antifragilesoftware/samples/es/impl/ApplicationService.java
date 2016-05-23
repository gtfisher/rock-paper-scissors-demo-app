package com.russmiles.antifragilesoftware.samples.es.impl;

import com.russmiles.antifragilesoftware.samples.es.api.BaseEvent;
import com.russmiles.antifragilesoftware.samples.es.store.EventStream;
import com.russmiles.antifragilesoftware.samples.es.api.Command;
import com.russmiles.antifragilesoftware.samples.es.store.EventStore;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ApplicationService {

    private static final String HANDLE_METHOD = "handle";
    private final EventStore eventStore;
    private CommandHandlerLookup commandHandlerLookup;




    public ApplicationService(EventStore eventStore, Class<?>... aggregateTypes) {
        this.eventStore = eventStore;
        this.commandHandlerLookup = new CommandHandlerLookup(HANDLE_METHOD, aggregateTypes);
    }

    public void handle(Command command) throws Exception {
        EventStream<Long> eventStream = eventStore.loadEventStream(command.aggregateId());
        Object target = newAggregateInstance(command);
        for (BaseEvent baseEvent : eventStream) {
            handle(target, baseEvent);
        }
        List<BaseEvent> baseEvents = handle(target, command);
        if (baseEvents != null && baseEvents.size() > 0) {
            eventStore.store(command.aggregateId(), eventStream.version(), baseEvents);
        } else {
            // Command generated no baseEvents
        }
    }

    private Object newAggregateInstance(Command command) throws InstantiationException, IllegalAccessException {
        return commandHandlerLookup.targetType(command).newInstance();
    }

    @SuppressWarnings("unchecked")
    private <R> R handle(Object target, Object param) throws Exception {
        Method method = target.getClass().getMethod(HANDLE_METHOD, param.getClass());
        try {
            return (R) method.invoke(target, param);
        } catch (InvocationTargetException e) {
            throw Sneak.sneakyThrow(e.getTargetException());
        }
    }


}