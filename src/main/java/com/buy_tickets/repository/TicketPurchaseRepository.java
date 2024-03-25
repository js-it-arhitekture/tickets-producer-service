package com.buy_tickets.repository;

import com.buy_tickets.model.TicketPurchase;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TicketPurchaseRepository implements ReactivePanacheMongoRepository<TicketPurchase> {

    public Multi<TicketPurchase> findByUser(String userId) {
        return find("userId", userId).stream();
    }

    public Multi<TicketPurchase> findByTicket(String ticketId) {
        return find("ticketId", ticketId).stream();
    }
}
