package cl.pedidos360.rabbitadmin.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class EmailMessageConsumer {
    private final ObjectMapper mapper;

    public EmailMessageConsumer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${pedidos360.rabbit.queues.email}")
    public void receive(Message message, Channel channel) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();

        try {
            JsonNode json = mapper.readTree(message.getBody());

            if (!json.hasNonNull("destination")
                    || !json.hasNonNull("subject")
                    || !json.hasNonNull("content")
                    || json.get("destination").asText().isBlank()
                    || json.get("subject").asText().isBlank()
                    || json.get("content").asText().isBlank()) {
                throw new IllegalArgumentException("Mensaje incompleto");
            }

            System.out.println("Mensaje recibido correctamente: " + json);
            channel.basicAck(tag, false);

        } catch (Exception e) {
            System.err.println("Mensaje rechazado: " + e.getMessage());
            channel.basicNack(tag, false, false);
        }
    }
}
