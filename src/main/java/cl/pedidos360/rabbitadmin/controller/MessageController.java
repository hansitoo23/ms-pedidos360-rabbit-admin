package cl.pedidos360.rabbitadmin.controller;

import cl.pedidos360.rabbitadmin.service.MessageProducerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageProducerService producer;

    public MessageController(MessageProducerService producer) {
        this.producer = producer;
    }

    public record EmailRequest(
        @NotBlank String destination,
        @NotBlank String subject,
        @NotBlank String content
    ) {}

    @PostMapping("/email")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, String> sendEmail(@Valid @RequestBody EmailRequest request) {
        producer.sendEmail(Map.of(
            "destination", request.destination(),
            "subject", request.subject(),
            "content", request.content()
        ));
        return Map.of("message", "Mensaje enviado a RabbitMQ");
    }
}
