package amq.producer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;
import jakarta.jms.TextMessage;

import java.util.logging.Logger;

@ApplicationScoped
public class TicketPurchaseProducer {

    private static final Logger LOG = Logger.getLogger(TicketPurchaseProducer.class.getName());
    @Inject
    ConnectionFactory connectionFactory;

    public void sendMessageToQueue(String queueName, String messageContent) {
        try (JMSContext context = connectionFactory.createContext(JMSContext.AUTO_ACKNOWLEDGE)) {

            Queue queue = context.createQueue(queueName);
            TextMessage message = context.createTextMessage(messageContent);
            context.createProducer().send(queue, message);
            LOG.info("logging send!");
        }
    }
}
