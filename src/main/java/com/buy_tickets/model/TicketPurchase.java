package com.buy_tickets.model;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
@MongoEntity(collection="ticket_purchases")
public class TicketPurchase extends ReactivePanacheMongoEntity {
    private String userId;
    private String ticketId;
    private Integer quantity;
    private Instant timestamp; // Use Instant for timestamp

    public TicketPurchase() {
        // Initialize any default values if needed
        this.timestamp = Instant.now(); // Initialize timestamp with current time
    }

    public TicketPurchase(String userId, String ticketId, Integer quantity, Instant timestamp) {
        this.userId = userId;
        this.ticketId = ticketId;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public TicketPurchase(String userId, String ticketId, Integer quantity) {
        this.userId = userId;
        this.ticketId = ticketId;
        this.quantity = quantity;
        this.timestamp = Instant.now(); // Initialize timestamp with current time
    }
}
