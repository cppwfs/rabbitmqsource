/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hello;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.support.SimpleBatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;

@SpringBootApplication
public class Application implements CommandLineRunner {

	final static String queueName = "spring-boot";

	@Value("${batchSize:10}")
	private int batchSize;

	@Value("${messageCount:1000000}")
	private int messageCount;


	@Autowired
	AnnotationConfigApplicationContext context;

	@Autowired
	RabbitTemplate rabbitTemplate;
	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("spring-boot-exchange");
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(queueName);
	}

	@Bean
	BatchingRabbitTemplate getBatchingRabbitTemplate(ConnectionFactory factory){
		BatchingRabbitTemplate result = new BatchingRabbitTemplate(new SimpleBatchingStrategy(batchSize, 2000000, 5000),new DefaultManagedTaskScheduler() );
		result.setConnectionFactory(factory);
		return result; 
		
	}
	public static void main(String[] args) throws InterruptedException {
		System.exit(SpringApplication.exit(
				SpringApplication.run(Application.class, args)));
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Sending message...");
		for(int x=0 ; x< messageCount;x++) {
			rabbitTemplate.convertAndSend(queueName, "                                                                                                    ");
		}
	}
}