package amq.producer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.ConnectionFactory;

@ApplicationScoped
public class ConnectionFactoryProducer {

    @Produces
    @ApplicationScoped
    public ConnectionFactory connectionFactory() {
        // Create and configure ActiveMQ connection factory
        return new ActiveMQConnectionFactory("tcp://localhost:61616");
    }
}
