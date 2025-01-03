package com.example.backend.config;

import com.banque.events.dto.AccountDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.topic.account-details-reply-topic}")
    private String replyTopic;

    @Value("${spring.kafka.topic.account-details-request-topic}")
    private String requestTopic;

    @Value("${spring.kafka.topic.account-update-topic}")
    private String accountUpdateTopic;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }


    @Bean
    public ProducerFactory<String, UUID> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }
    @Bean
    public ProducerFactory<String, AccountDto> accountDtoProducerFactory() {
        Map<String, Object> configProps = producerConfigs();
        return new DefaultKafkaProducerFactory<>(configProps);
    }


    @Bean
    public ConsumerFactory<String, AccountDto> consumerFactory() {

        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(AccountDto.class)
        );
    }

    @Bean
    public KafkaTemplate<String, UUID> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaTemplate<String, AccountDto> accountKafkaTemplate() {
        return new KafkaTemplate<>(accountDtoProducerFactory());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AccountDto> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AccountDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ReplyingKafkaTemplate<String, UUID, AccountDto> replyingKafkaTemplate(
            ProducerFactory<String, UUID> pf,
            ConcurrentKafkaListenerContainerFactory<String, AccountDto> factory) {
        ConcurrentMessageListenerContainer<String, AccountDto> replyContainer =
                factory.createContainer(replyTopic);
        replyContainer.getContainerProperties().setGroupId(groupId + "-reply");
        replyContainer.getContainerProperties().setMissingTopicsFatal(false);

        ReplyingKafkaTemplate<String, UUID, AccountDto> template =
                new ReplyingKafkaTemplate<>(pf, replyContainer);
        template.setSharedReplyTopic(true);
        template.setDefaultReplyTimeout(Duration.ofSeconds(30));
        return template;
    }
}