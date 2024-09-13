//package com.example.teacherassistant.common.config;
//
//import com.example.teacherassistant.notificationPackage.Notification;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
//import com.fasterxml.jackson.databind.ser.std.StringSerializer;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@EnableKafka
//public class KafkaConfig {
//
//    @Bean
//    public ProducerFactory<String, Notification> producerFactory() {
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        return new DefaultKafkaProducerFactory<>(configProps);
//    }
//
//    @Bean
//    public KafkaTemplate<String, Notification> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }
//
//    @Bean
//    public ConsumerFactory<String, Notification> consumerFactory() {
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "notification_group");
//        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), new JsonDeserializer<>(Notification.class));
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, Notification> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, Notification> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }
//}
