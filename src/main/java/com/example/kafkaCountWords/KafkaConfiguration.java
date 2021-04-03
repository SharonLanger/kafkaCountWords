package com.example.kafkaCountWords;


import com.example.kafkaCountWords.entity.Aggregator;
import com.example.kafkaCountWords.entity.MainReader;
import com.example.kafkaCountWords.service.MainReaderService;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;


@EnableKafka
@Configuration
public class KafkaConfiguration {

    @Value("${topic.reader.name}")
    private String topicReader;

    @Value("${topic.reader.partitions}")
    private String readerPartitions;

    @Value("${topic.aggregator.name}")
    private String topicAggregator;

    @Value("${topic.aggregator.partitions}")
    private String aggregatorPartitions;

//=============================================================
//=============================================================
//=============================================================

    @Bean
    public MainReader mainReader() {
        return new MainReader();
    }

    @Bean
    public MainReaderService mainReaderService() {
        return new MainReaderService();
    }

    @Bean
    public Aggregator aggregator() {
        return new Aggregator();
    }

//=============================================================
//============== MAIN READER KAFKA CONFIG =====================
//=============================================================

    @Bean
    public ProducerFactory<String, MainReader> producerFactoryMainReader() {
        Map<String, Object> config = defaultProducerConfig();

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public ConsumerFactory<String, MainReader> consumerFactoryMainReader() {
        Map<String, Object> config = defaultConsumerConfigMap("group_main_reader");

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(MainReader.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MainReader> concurrentKafkaListenerContainerFactoryMainReader() {
        ConcurrentKafkaListenerContainerFactory<String, MainReader> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryMainReader());
        return factory;
    }

    @Bean
    public KafkaTemplate<String, MainReader> kafkaTemplateMainReader() {
        return new KafkaTemplate<>(producerFactoryMainReader());
    }

    @Bean
    public NewTopic topicCreatorReader(){
        return TopicBuilder.name(topicReader)
                .partitions(Integer.parseInt(readerPartitions))
                .replicas(1)
                .build();
    }

//=============================================================
//============== AGGREGATOR KAFKA CONFIG ======================
//=============================================================

    @Bean
    public ProducerFactory<String, Aggregator> producerFactoryAggregator() {
        Map<String, Object> config = defaultProducerConfig();
        return new DefaultKafkaProducerFactory<>(config);
    }
    @Bean
    public ConsumerFactory<String, Aggregator> consumerFactoryAggregator() {
        Map<String, Object> config = defaultConsumerConfigMap("group_aggregator");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(Aggregator.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Aggregator> concurrentKafkaListenerContainerFactoryAggregator() {
        ConcurrentKafkaListenerContainerFactory<String, Aggregator> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryAggregator());
        return factory;
    }

    @Bean
    public KafkaTemplate<String, Aggregator> kafkaTemplateAggregator() {
        return new KafkaTemplate<>(producerFactoryAggregator());
    }

    @Bean
    public NewTopic topicCreatorAgg(){
        return TopicBuilder.name(topicAggregator)
                .partitions(Integer.parseInt(aggregatorPartitions))
                .replicas(1)
                .build();
    }

//  =====================================================================
//  =====================================================================
//  =====================================================================

    private static Map<String, Object> defaultProducerConfig() {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return config;
    }

    private static Map<String, Object> defaultConsumerConfigMap(String group_main_reader) {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, group_main_reader);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return config;
    }
}
