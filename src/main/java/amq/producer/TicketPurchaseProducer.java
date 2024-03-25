package amq.producer;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jms.TextMessage;


@ApplicationScoped
public class TicketPurchaseProducer {

    @Inject
    ConnectionFactory connectionFactory;

    public void sendMessageToQueue(String queueName, String messageContent) {
        try (JMSContext context = connectionFactory.createContext(JMSContext.AUTO_ACKNOWLEDGE)) {
            Queue queue = context.createQueue(queueName);
            TextMessage message = context.createTextMessage(messageContent);
            context.createProducer().send(queue, message);
        }
    }
}
