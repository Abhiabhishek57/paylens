package com.paylens.events;

import java.time.Instant;

public record EventIngestionResponse(
        EventIngestionResult result,
        String eventId,
        Instant timestamp
) {
}
