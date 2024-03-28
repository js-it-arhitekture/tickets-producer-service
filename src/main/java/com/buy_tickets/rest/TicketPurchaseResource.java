package com.buy_tickets.rest;

import amq.producer.TicketPurchaseProducer;
import com.buy_tickets.model.TicketPurchase;
import com.buy_tickets.repository.TicketPurchaseRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.bson.types.ObjectId;
import org.jboss.logging.Logger;

@Path("/api/purchases")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TicketPurchaseResource {

    private static final Logger LOG = Logger.getLogger(TicketPurchaseResource.class.getName());
    @Inject
    public
    TicketPurchaseRepository ticketPurchaseRepository;

    @Inject
    public
    TicketPurchaseProducer ticketPurchaseProducer;

    public void sendToQueue(TicketPurchase ticketPurchase) {
        ticketPurchaseProducer.sendMessageToQueue("ticket-purchase", ticketPurchase.toString());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createTicketPurchase(TicketPurchase ticketPurchase) {
        LOG.info("Creating a new ticket purchase for ${0}");

        // Persist the ticket purchase
        Uni<TicketPurchase> persistUni = ticketPurchaseRepository.persist(ticketPurchase);

        // Transform the result of ticket purchase persistence into a response
        return persistUni
                .onItem().transform(inserted -> {
                    LOG.info("Inserted successfully!");
                    // SEND TO QUEUE
                    sendToQueue(ticketPurchase);
                    // Return CREATED
                    return Response.ok(inserted).status(Response.Status.CREATED).build();
                })
                .onFailure().recoverWithItem(e -> {
                    LOG.error("Error creating reservation", e);
                    // Return internal server error response
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("Error creating ticket purchase" + e.getMessage()).build();
                });
    }


    @GET
    public Multi<Response> getAllPurchases() {
        LOG.info("Getting all ticket purchases");
        return ticketPurchaseRepository.listAll()
                .onItem().transform(purchases -> Response.ok(purchases).build())
                .onFailure().recoverWithItem(error -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Error retrieving ticket purchases: " + error.getMessage())
                        .build()).toMulti();
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getPurchaseByIs(@PathParam("id") String id){
        LOG.infov("Getting ticket purchase with id: {0}", id);
        return ticketPurchaseRepository.findById(new ObjectId(id))
                .onItem().transform(purchase -> Response.ok(purchase).build())
                .onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND)::build)
                .onFailure().invoke(e -> LOG.errorv("Error retrieving ticket purchase",e))
                .onFailure().recoverWithItem(e -> Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retieving purchases" + e.getMessage()).build());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> deleteTicketPurchase(@PathParam("id") String id){
        LOG.infov("Deleting purchase id: {0} ", id);
        return ticketPurchaseRepository.deleteById(new ObjectId(id))
                .onItem().transform(deleted -> deleted
                ? Response.ok().status(Response.Status.NO_CONTENT).build()
                        :Response.status(Response.Status.NOT_FOUND).build())
                .onFailure().invoke(e -> LOG.errorv("Error deleting purchase with id {0},",id, e))
                .onFailure().recoverWithItem(e->Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting purchase" + e.getMessage()).build());
    }

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String getTstOutput(){
        return "welcome to test2";
    }

    @GET
    @Path("/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<Response> getTicketPurchaseByUser(@PathParam("userId") String userId) {
        LOG.infov("Getting ticket purchases by user id: {0}", userId);
        return ticketPurchaseRepository.findByUser(userId)
                .onItem().transform(purchases -> Response.ok(purchases).build())
                .onFailure().recoverWithItem(error -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Error retrieving ticket purchases by user: " + error.getMessage())
                        .build());
    }

    @GET
    @Path("/ticket/{ticketId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<Response> getTicketPurchaseByTicket(@PathParam("ticketId") String ticketId) {
        LOG.infov("Getting ticket purchases by ticket id: {0}", ticketId);
        return ticketPurchaseRepository.findByTicket(ticketId)
                .onItem().transform(purchases -> Response.ok(purchases).build())
                .onFailure().recoverWithItem(error -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Error retrieving ticket purchases by ticket: " + error.getMessage())
                        .build());
    }

}
