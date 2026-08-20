package com.paylens.events;

import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class PaymentEventStore {

    private final ConcurrentMap<String, PaymentEvent> events = new ConcurrentHashMap<>();

    public boolean storeIfNew(PaymentEvent event) {
        return events.putIfAbsent(event.eventId(), event) == null;
    }
}
