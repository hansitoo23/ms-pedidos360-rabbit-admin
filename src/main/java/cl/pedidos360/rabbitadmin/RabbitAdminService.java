
package cl.pedidos360.rabbitadmin.service;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.stereotype.Service;

@Service
public class RabbitAdminService {

    private final AmqpAdmin amqpAdmin;

    public RabbitAdminService(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    public void createQueue(String name, boolean durable) {
        validateName(name);
        amqpAdmin.declareQueue(new Queue(name, durable));
    }

    public void deleteQueue(String name) {
        validateName(name);
        boolean deleted = amqpAdmin.deleteQueue(name);
        if (!deleted) {
            throw new IllegalArgumentException(
                    "No se pudo eliminar la cola: " + name);
        }
    }

    public void createExchange(String name, String type, boolean durable) {
        validateName(name);

        Exchange exchange = switch (type.toLowerCase()) {
            case "direct" -> new DirectExchange(name, durable, false);
            case "topic" -> new TopicExchange(name, durable, false);
            case "fanout" -> new FanoutExchange(name, durable, false);
            default -> throw new IllegalArgumentException(
                    "Tipo de exchange no válido. Usa direct, topic o fanout.");
        };

        amqpAdmin.declareExchange(exchange);
    }

    public void deleteExchange(String name) {
        validateName(name);
        amqpAdmin.deleteExchange(name);
    }

    public void createBinding(
            String queueName,
            String exchangeName,
            String routingKey) {

        validateName(queueName);
        validateName(exchangeName);

        if (routingKey == null || routingKey.isBlank()
                || routingKey.length() > 255) {
            throw new IllegalArgumentException(
                    "La routing key debe tener entre 1 y 255 caracteres.");
        }

        Binding binding = new Binding(
                queueName,
                Binding.DestinationType.QUEUE,
                exchangeName,
                routingKey,
                null
        );

        amqpAdmin.declareBinding(binding);
    }

    public void deleteBinding(
            String queueName,
            String exchangeName,
            String routingKey) {

        validateName(queueName);
        validateName(exchangeName);

        Binding binding = new Binding(
                queueName,
                Binding.DestinationType.QUEUE,
                exchangeName,
                routingKey,
                null
        );

        amqpAdmin.removeBinding(binding);
    }

    private void validateName(String name) {
        if (name == null || !name.matches("[a-zA-Z0-9._-]{1,180}")) {
            throw new IllegalArgumentException(
                    "Nombre no válido. Usa letras, números, punto, guion "
                    + "o guion bajo; máximo 180 caracteres.");
        }
    }
}
