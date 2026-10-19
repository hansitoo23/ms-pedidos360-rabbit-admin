package cl.pedidos360.rabbitadmin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class MessageProducerService {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper;

    public MessageProducerService(RabbitTemplate rabbitTemplate, ObjectMapper mapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = mapper;
    }

    public void sendEmail(Map<String, String> message) {
        try {
            String json = mapper.writeValueAsString(message);
            rabbitTemplate.convertAndSend("cmd.direct", "email.send", json);
        } catch (Exception e) {
            throw new IllegalArgumentException("No se pudo enviar el mensaje", e);
        }
    }
}
