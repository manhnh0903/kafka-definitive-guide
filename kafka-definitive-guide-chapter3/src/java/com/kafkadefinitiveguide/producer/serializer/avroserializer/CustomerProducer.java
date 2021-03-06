/*
 * Copyright 2020 Wuyi Chen.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kafkadefinitiveguide.producer.serializer.avroserializer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * Send Customer (message) by Avro serializer.
 * 
 * <p>Customer is not Pojo object, it is Avro object generated by avro-tools.jar.
 *
 * @author  Wuyi Chen
 * @date    06/03/2020
 * @version 1.0
 * @since   1.0
 */
public class CustomerProducer {
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers",   "localhost:9092");
		props.put("key.serializer",      "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer",    "io.confluent.kafka.serializers.KafkaAvroSerializer");
		props.put("schema.registry.url", "localhost:8081");     // URL points to the schema registry.

		String topic = "customerContacts";

		Producer<String, Customer> producer = new KafkaProducer<String, Customer>(props);

		// We keep producing new events until someone ctrl-c
		while (true) {
			Customer customer = CustomerGenerator.getNext();
			System.out.println("Generated customer " + customer.toString());
			ProducerRecord<String, Customer> record = new ProducerRecord<String, Customer>(topic, customer.getName().toString(), customer);
			producer.send(record);
		}
	}
}
