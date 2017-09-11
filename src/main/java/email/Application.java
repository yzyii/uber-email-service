package email;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

/**
 * Entry point of the email-service application. Also contains the Application Configuration for the RabbitMQ endpoints and mappers.
 */
@SpringBootApplication
@EnableRabbit
public class Application {
    
    public final static String queueName = "email-service";
    
    @Bean
    public Queue getQueue() {
        return new Queue(queueName, true);
    }
    
    @Bean
    public Jackson2JsonMessageConverter getProducerMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public MappingJackson2MessageConverter getConsumerMessageConverter() {
       return new MappingJackson2MessageConverter();
    }
    
    @Bean
    public RabbitTemplate getRabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(getProducerMessageConverter());
        
        return rabbitTemplate;
    }
    
    @Bean
    public DefaultMessageHandlerMethodFactory getMessageHandlerMethodFactory() {
       DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
       factory.setMessageConverter(getConsumerMessageConverter());
       
       return factory;
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
