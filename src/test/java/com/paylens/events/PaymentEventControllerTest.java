package com.paylens.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void acceptsANewEvent() throws Exception {
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validEvent("evt-accepted"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result").value("ACCEPTED"))
                .andExpect(jsonPath("$.eventId").value("evt-accepted"));
    }

    @Test
    void rejectsADuplicateEventId() throws Exception {
        String request = objectMapper.writeValueAsString(validEvent("evt-duplicate"));

        mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.result").value("DUPLICATE_EVENT"));
    }

    @Test
    void rejectsAnInvalidEvent() throws Exception {
        PaymentEvent invalid = new PaymentEvent(
                "txn-invalid", "evt-invalid", BigDecimal.ZERO, "inr", "UPI", "FAILED", null, Instant.now());

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.fields.amount").exists())
                .andExpect(jsonPath("$.fields.currency").exists());
    }

    private PaymentEvent validEvent(String eventId) {
        return new PaymentEvent(
                "txn-001", eventId, new BigDecimal("500.00"), "INR", "UPI", "FAILED", "GATEWAY_TIMEOUT", Instant.now());
    }
}
