package com.buy_tickets.model;

import com.mongodb.internal.connection.Time;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MongoEntity(collection="ticket_purchases") // Specify the MongoDB collection name
public class TicketPurchase extends ReactivePanacheMongoEntity {
    private String userId;
    private String ticketId;
    private Integer quantity;
    private String timestamp;

    public TicketPurchase(String userId, String ticketId, Integer quantity) {
        this.userId = userId;
        this.ticketId = "100000000";
        this.quantity = quantity;
        this.timestamp = new Timestamp(System.currentTimeMillis()).toString();;
    }
}
