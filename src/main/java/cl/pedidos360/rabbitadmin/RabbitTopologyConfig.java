
package cl.pedidos360.rabbitadmin.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableRabbit
public class RabbitTopologyConfig {

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }

    @Bean
    public DirectExchange directExchange(
            @Value("${pedidos360.rabbit.exchanges.direct}") String name) {
        return new DirectExchange(name, true, false);
    }

    @Bean
    public TopicExchange topicExchange(
            @Value("${pedidos360.rabbit.exchanges.topic}") String name) {
        return new TopicExchange(name, true, false);
    }

    @Bean
    public DirectExchange deadLetterExchange(
            @Value("${pedidos360.rabbit.exchanges.dead-letter}") String name) {
        return new DirectExchange(name, true, false);
    }

    @Bean
    public Queue emailQueue(
            @Value("${pedidos360.rabbit.queues.email}") String name,
            @Value("${pedidos360.rabbit.exchanges.dead-letter}") String dlx,
            @Value("${pedidos360.rabbit.routing-keys.email}") String key) {
        return QueueBuilder.durable(name)
                .deadLetterExchange(dlx)
                .deadLetterRoutingKey(key)
                .build();
    }

    @Bean
    public Queue emailDlq(
            @Value("${pedidos360.rabbit.queues.email-dlq}") String name) {
        return QueueBuilder.durable(name).build();
    }

    @Bean
    public Queue kitchenQueue(
            @Value("${pedidos360.rabbit.queues.kitchen}") String name,
            @Value("${pedidos360.rabbit.exchanges.dead-letter}") String dlx,
            @Value("${pedidos360.rabbit.routing-keys.kitchen}") String key) {
        return QueueBuilder.durable(name)
                .deadLetterExchange(dlx)
                .deadLetterRoutingKey(key)
                .build();
    }

    @Bean
    public Queue kitchenDlq(
            @Value("${pedidos360.rabbit.queues.kitchen-dlq}") String name) {
        return QueueBuilder.durable(name).build();
    }

    @Bean
    public Queue invoiceQueue(
            @Value("${pedidos360.rabbit.queues.invoice}") String name,
            @Value("${pedidos360.rabbit.exchanges.dead-letter}") String dlx,
            @Value("${pedidos360.rabbit.routing-keys.invoice}") String key) {
        return QueueBuilder.durable(name)
                .deadLetterExchange(dlx)
                .deadLetterRoutingKey(key)
                .build();
    }

    @Bean
    public Queue invoiceDlq(
            @Value("${pedidos360.rabbit.queues.invoice-dlq}") String name) {
        return QueueBuilder.durable(name).build();
    }

    @Bean
    public Binding emailDirectBinding(
            Queue emailQueue,
            DirectExchange directExchange,
            @Value("${pedidos360.rabbit.routing-keys.email}") String key) {
        return BindingBuilder.bind(emailQueue).to(directExchange).with(key);
    }

    @Bean
    public Binding emailTopicBinding(
            Queue emailQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(emailQueue).to(topicExchange).with("email.*");
    }

    @Bean
    public Binding kitchenDirectBinding(
            Queue kitchenQueue,
            DirectExchange directExchange,
            @Value("${pedidos360.rabbit.routing-keys.kitchen}") String key) {
        return BindingBuilder.bind(kitchenQueue).to(directExchange).with(key);
    }

    @Bean
    public Binding kitchenTopicBinding(
            Queue kitchenQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(kitchenQueue).to(topicExchange).with("kitchen.#");
    }

    @Bean
    public Binding invoiceDirectBinding(
            Queue invoiceQueue,
            DirectExchange directExchange,
            @Value("${pedidos360.rabbit.routing-keys.invoice}") String key) {
        return BindingBuilder.bind(invoiceQueue).to(directExchange).with(key);
    }

    @Bean
    public Binding invoiceTopicBinding(
            Queue invoiceQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(invoiceQueue).to(topicExchange).with("invoice.*");
    }

    @Bean
    public Binding emailDlqBinding(
            Queue emailDlq, DirectExchange deadLetterExchange,
            @Value("${pedidos360.rabbit.routing-keys.email}") String key) {
        return BindingBuilder.bind(emailDlq).to(deadLetterExchange).with(key);
    }

    @Bean
    public Binding kitchenDlqBinding(
            Queue kitchenDlq, DirectExchange deadLetterExchange,
            @Value("${pedidos360.rabbit.routing-keys.kitchen}") String key) {
        return BindingBuilder.bind(kitchenDlq).to(deadLetterExchange).with(key);
    }

    @Bean
    public Binding invoiceDlqBinding(
            Queue invoiceDlq, DirectExchange deadLetterExchange,
            @Value("${pedidos360.rabbit.routing-keys.invoice}") String key) {
        return BindingBuilder.bind(invoiceDlq).to(deadLetterExchange).with(key);
    }
}

