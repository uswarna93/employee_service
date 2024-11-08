package com.demo_emp_service.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaTopicConfig {

//   @Value("${spring.kafka.bootstrap-servers}")
//  public String bootStrapServers;

        @Bean
        public KafkaAdmin kafkaAdmin() {
            Map<String, Object> configs = new HashMap<>();
            configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Replace with your broker address
            return new KafkaAdmin(configs);
        }
        @Bean
        public NewTopic myTopic() {
            return new NewTopic("EmployeeInfoTopic", 1, (short) 1); // Topic name, partitions, replication factor
        }
    }

