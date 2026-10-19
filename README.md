# Pedidos360 - Rabbit Admin

Microservicio desarrollado con Spring Boot y Java 21 para administrar recursos de RabbitMQ y gestionar el envío y consumo de mensajes de correo.

## Tecnologías
- Java 21
- Spring Boot
- Maven
- RabbitMQ
- Docker

## Funcionalidades
- Crear y eliminar colas.
- Crear y eliminar exchanges.
- Crear y eliminar bindings.
- Enviar mensajes mediante REST.
- Consumir mensajes desde RabbitMQ.
- Confirmar mensajes procesados mediante ACK manual.
- Rechazar mensajes inválidos mediante NACK.
- Validar los datos de entrada.
- Consultar el estado mediante Spring Boot Actuator.

## Requisitos
- Java 21
- Maven
- RabbitMQ disponible

## Configuración
Las siguientes variables de entorno permiten configurar la aplicación:

| Variable | Valor local predeterminado |
|---|---|
| SERVER_PORT | 8081 |
| RABBITMQ_HOST | localhost |
| RABBITMQ_PORT | 5672 |
| RABBITMQ_USERNAME | pedidos360 |
| RABBITMQ_PASSWORD | Pedidos360_Local_2026 |
| RABBITMQ_VHOST | / |

En otros entornos, define credenciales seguras mediante variables de entorno. No reutilices las credenciales locales en producción.

## Ejecución
Compilar y ejecutar las pruebas:

```bash
mvn clean package
```

Iniciar la aplicación:

```bash
mvn spring-boot:run
```

## Endpoints REST

### Estado
`GET /actuator/health`

### Administración de colas
`POST /api/admin/rabbit/queues`

```json
{
  "name": "q.test",
  "durable": true
}
```

`DELETE /api/admin/rabbit/queues/{name}`

### Administración de exchanges
`POST /api/admin/rabbit/exchanges`

```json
{
  "name": "ex.test",
  "type": "direct",
  "durable": true
}
```

`DELETE /api/admin/rabbit/exchanges/{name}`

### Administración de bindings
`POST /api/admin/rabbit/bindings`

```json
{
  "queueName": "q.test",
  "exchangeName": "ex.test",
  "routingKey": "test.created"
}
```

`DELETE /api/admin/rabbit/bindings`

Utiliza el mismo JSON para indicar la cola, el exchange y la routing key del binding que se desea eliminar.

### Envío de mensajes
`POST /api/messages/email`

```json
{
  "destination": "prueba@ejemplo.com",
  "subject": "Prueba Pedidos360",
  "content": "Mensaje de prueba"
}
```

El endpoint acepta la solicitud y publica el mensaje en RabbitMQ. El consumidor valida su contenido y confirma los mensajes válidos con ACK; los inválidos se rechazan con NACK sin reencolarlos.

## Topología RabbitMQ
- Exchange directo: `cmd.direct`
- Exchange topic: `cmd.topic`
- Exchange de mensajes rechazados: `cmd.dead.dlx`
- Cola principal de correo: `q.cmd.email`
- Cola de mensajes rechazados: `q.cmd.email.dlq`

## Pruebas realizadas
- Compilación Maven con resultado BUILD SUCCESS.
- Comprobación del estado de la aplicación.
- Creación y eliminación de recursos RabbitMQ mediante REST.
- Envío de mensajes mediante REST.
- Recepción y confirmación de mensajes por el consumidor.

## Desarrollo
Proyecto académico de la evaluación Cloud Native.
