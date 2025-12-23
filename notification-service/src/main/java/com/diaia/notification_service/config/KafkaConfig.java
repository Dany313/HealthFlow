package com.diaia.notification_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic appointmentTopic() {
        return TopicBuilder.name("appointment-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public RetryTopicConfiguration myRetryTopic(KafkaTemplate<String, Object> template) {
        return RetryTopicConfigurationBuilder
                .newInstance()
                .maxAttempts(3)
                .fixedBackOff(2000)
                .includeTopic("appointment-topic")
                // Aggiungiamo la gestione esplicita del DLT
                .dltHandlerMethod("appointmentConsumer", "handleDlt")
                .create(template);
    }
}
