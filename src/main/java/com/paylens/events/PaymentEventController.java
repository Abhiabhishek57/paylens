package com.paylens.events;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class PaymentEventController {

    private final EventIngestionService ingestionService;

    public PaymentEventController(EventIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping
    public ResponseEntity<EventIngestionResponse> ingest(@Valid @RequestBody PaymentEvent event) {
        EventIngestionResult result = ingestionService.ingest(event);
        HttpStatus status = result == EventIngestionResult.ACCEPTED
                ? HttpStatus.CREATED
                : HttpStatus.CONFLICT;

        return ResponseEntity.status(status)
                .body(new EventIngestionResponse(result, event.eventId(), event.timestamp()));
    }
}
