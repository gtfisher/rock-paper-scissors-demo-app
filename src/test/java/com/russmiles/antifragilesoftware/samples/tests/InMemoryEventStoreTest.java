package com.russmiles.antifragilesoftware.samples.tests;


import com.russmiles.antifragilesoftware.samples.es.store.EventStream;
import com.russmiles.antifragilesoftware.samples.rps.event.*;
import com.russmiles.antifragilesoftware.samples.es.api.BaseEvent;
import com.russmiles.antifragilesoftware.samples.es.store.ListEventStream;
import com.russmiles.antifragilesoftware.samples.es.store.memory.InMemoryEventStore;
import com.russmiles.antifragilesoftware.samples.rps.Move;
import com.russmiles.antifragilesoftware.samples.rps.event.GameTiedEvent;

import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;

public class InMemoryEventStoreTest {
	UUID gameId = UUID.randomUUID();
    UUID gameId2 = UUID.randomUUID();

	@Test
	public void testLoadEventsAfter() throws Exception {
		InMemoryEventStore es = new InMemoryEventStore();
		es.store(gameId, 0, Arrays.asList(new GameTiedEvent(gameId)));
		Thread.sleep(1);
		es.store(gameId, 1, Arrays.asList(new GameTiedEvent(gameId)));
        ListEventStream events = es.loadEventStream(gameId);
        System.out.println("eventCount=" + countEvents(events));
        assertEquals(2, countEvents(events));
        EventStream<Long> stream = es.loadEventsAfter(0L);
		assertEquals(2, countEvents(stream));
		Long id = stream.version();
		System.out.println("id=" + id);
	}

    @Test
    public void testeventStream() throws Exception {
        InMemoryEventStore es = new InMemoryEventStore();
        es.store(gameId2, 0, Arrays.asList(new GameCreatedEvent(gameId2, "winner")));
        Thread.sleep(1);
        es.store(gameId2, 1, Arrays.asList(new MoveDecidedEvent(gameId2,"winner", Move.paper)));
        Thread.sleep(1);
        es.store(gameId2, 2, Arrays.asList(new GameTiedEvent(gameId2)));
        Thread.sleep(1);
        es.store(gameId2, 3, Arrays.asList(new GameWonEvent(gameId2,"winner","looser")));
        ListEventStream events = es.loadEventStream(gameId2);
        assertEquals(4, countEvents(events));
        Iterator<BaseEvent> iterator = events.iterator();
        while (iterator.hasNext())
        {
            BaseEvent ev = iterator.next();

            assertEquals(gameId2,ev.getUUID());

        }



    }

	private int countEvents(EventStream<Long> stream) {
		int result = 0;
		for (BaseEvent baseEvent : stream) {
			result++;
		}
		return result;
	}

}
