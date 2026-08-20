package com.paylens.events;

import org.springframework.stereotype.Service;

@Service
public class EventIngestionService {

    private final PaymentEventStore eventStore;

    public EventIngestionService(PaymentEventStore eventStore) {
        this.eventStore = eventStore;
    }

    public EventIngestionResult ingest(PaymentEvent event) {
        return eventStore.storeIfNew(event)
                ? EventIngestionResult.ACCEPTED
                : EventIngestionResult.DUPLICATE_EVENT;
    }
}
