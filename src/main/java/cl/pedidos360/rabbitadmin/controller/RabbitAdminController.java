
package cl.pedidos360.rabbitadmin.controller;

import cl.pedidos360.rabbitadmin.service.RabbitAdminService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/rabbit")
public class RabbitAdminController {

    private final RabbitAdminService service;

    public RabbitAdminController(RabbitAdminService service) {
        this.service = service;
    }

    public record QueueRequest(
            @NotBlank
            @Pattern(regexp = "[a-zA-Z0-9._-]{1,180}")
            String name,
            boolean durable) {}

    public record ExchangeRequest(
            @NotBlank
            @Pattern(regexp = "[a-zA-Z0-9._-]{1,180}")
            String name,
            @NotBlank
            @Pattern(regexp = "(?i)direct|topic|fanout")
            String type,
            boolean durable) {}

    public record BindingRequest(
            @NotBlank
            @Pattern(regexp = "[a-zA-Z0-9._-]{1,180}")
            String queueName,
            @NotBlank
            @Pattern(regexp = "[a-zA-Z0-9._-]{1,180}")
            String exchangeName,
            @NotBlank
            @Size(max = 255)
            String routingKey) {}

    @PostMapping("/queues")
    public ResponseEntity<?> createQueue(
            @Valid @RequestBody QueueRequest request) {
        service.createQueue(request.name(), request.durable());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Cola creada", "name", request.name()));
    }

    @DeleteMapping("/queues/{name}")
    public ResponseEntity<?> deleteQueue(@PathVariable String name) {
        service.deleteQueue(name);
        return ResponseEntity.ok(Map.of("message", "Cola eliminada", "name", name));
    }

    @PostMapping("/exchanges")
    public ResponseEntity<?> createExchange(
            @Valid @RequestBody ExchangeRequest request) {
        service.createExchange(
                request.name(), request.type(), request.durable());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Exchange creado", "name", request.name()));
    }

    @DeleteMapping("/exchanges/{name}")
    public ResponseEntity<?> deleteExchange(@PathVariable String name) {
        service.deleteExchange(name);
        return ResponseEntity.ok(
                Map.of("message", "Exchange eliminado", "name", name));
    }

    @PostMapping("/bindings")
    public ResponseEntity<?> createBinding(
            @Valid @RequestBody BindingRequest request) {
        service.createBinding(
                request.queueName(),
                request.exchangeName(),
                request.routingKey());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Binding creado"));
    }

    @DeleteMapping("/bindings")
    public ResponseEntity<?> deleteBinding(
            @Valid @RequestBody BindingRequest request) {
        service.deleteBinding(
                request.queueName(),
                request.exchangeName(),
                request.routingKey());
        return ResponseEntity.ok(Map.of("message", "Binding eliminado"));
    }
}

