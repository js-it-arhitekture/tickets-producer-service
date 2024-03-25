package com.buy_tickets;

import amq.producer.TicketPurchaseProducer;
import com.buy_tickets.model.TicketPurchase;
import com.buy_tickets.repository.TicketPurchaseRepository;
import com.buy_tickets.rest.TicketPurchaseResource;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class TicketPurchaseResourceTest {

    @Mock
    TicketPurchaseRepository ticketPurchaseRepository;

    @InjectMocks
    TicketPurchaseResource ticketPurchaseResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testCreateTicketPurchase() {
        // Create a test ticket purchase
        TicketPurchase testTicketPurchase = new TicketPurchase(UUID.randomUUID().toString(), "100000000", 2);

        // Mock the behavior of ticketPurchaseRepository.persist to return a Uni<TicketPurchase>
        Uni<TicketPurchase> uniTicketPurchase = Uni.createFrom().item(testTicketPurchase);
        when(ticketPurchaseRepository.persist(any(TicketPurchase.class))).thenReturn(uniTicketPurchase);

        // Create a mocked instance of TicketPurchaseProducer
        TicketPurchaseProducer ticketPurchaseProducer = Mockito.mock(TicketPurchaseProducer.class);

        // Create an instance of TicketPurchaseResource and inject the mocked dependencies
        TicketPurchaseResource ticketPurchaseResource = new TicketPurchaseResource();
        ticketPurchaseResource.ticketPurchaseRepository = ticketPurchaseRepository;
        ticketPurchaseResource.ticketPurchaseProducer = ticketPurchaseProducer;

        // Call the method to be tested
        Response response = ticketPurchaseResource.createTicketPurchase(testTicketPurchase).await().indefinitely();

        // Verify the response
        assertEquals(201, response.getStatus(), "HTTP status code should be 201 (Created)");
        assertEquals(testTicketPurchase, response.getEntity(), "Response entity should be the test ticket purchase");
    }



}
