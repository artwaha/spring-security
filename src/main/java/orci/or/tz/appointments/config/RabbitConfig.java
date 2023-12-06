package orci.or.tz.appointments.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class RabbitConfig {

    @Value("${spring.rabbitmq.username}")
    private String rabbitMqUsername;
  
    @Value("${spring.rabbitmq.password}")
    private String rabbitMqPassword;
  
    @Value("${spring.rabbitmq.host}")
    private String rabbitMqHost;
  
    @Value("${spring.rabbitmq.virtual-host}")
    private String rabbitMqVirtualHost;
  
    @Bean
    public RabbitTransactionManager rabbitTransactionManager(ConnectionFactory connectionFactory) {
      return new RabbitTransactionManager(connectionFactory);
    }
  
    @Bean
    public ConnectionFactory connectionFactory() {
      CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
      connectionFactory.setUsername(rabbitMqUsername);
      connectionFactory.setPassword(rabbitMqPassword);
      connectionFactory.setHost(rabbitMqHost);
      connectionFactory.setVirtualHost(rabbitMqVirtualHost);
      return connectionFactory;
    }
  
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter(Jackson2ObjectMapperBuilder builder,
        ObjectMapper objectMapper) {
      return new Jackson2JsonMessageConverter(objectMapper);
    }
  
    @Bean
    public RabbitTemplate rabbitTemplate(Jackson2JsonMessageConverter jsonMessageConverter) {
      RabbitTemplate template = new RabbitTemplate();
      template.setMessageConverter(jsonMessageConverter);
      template.setConnectionFactory(connectionFactory());
      return template;
    }
  
    @Bean
    public SimpleMessageListenerContainer container(SimpleRabbitListenerContainerFactory factory,
        RabbitTransactionManager rabbitTransactionManager) {
      factory.setConnectionFactory(connectionFactory());
      factory.setConcurrentConsumers(5);
      factory.setMaxConcurrentConsumers(15);
      factory.setPrefetchCount(20);
      factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
  
      SimpleMessageListenerContainer container = factory.createListenerContainer();
      container.setConnectionFactory(connectionFactory());
      container.setTransactionManager(rabbitTransactionManager);
      container.setChannelTransacted(true);
  
      return container;
    }
    
}
