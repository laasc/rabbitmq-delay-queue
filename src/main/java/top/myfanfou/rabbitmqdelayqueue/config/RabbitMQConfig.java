package top.myfanfou.rabbitmqdelayqueue.config;

import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Exchange;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: laasc
 * @Date: 2022/3/30 15:02
 * @Description: RabbitMQ配置信息
 * @Version: 1.0
 */
@Configuration
@Data
public class RabbitMQConfig {
  /**
   * 交换机名称
   */
  public static final String eventExchange = "order_event_exchange";

  /**
   * 死信交换机名称
   */
  public static final String dealEventExchange = "order_event_exchange";

  /**
   * 死信队列名称
   */
  public static final String dealQueueOrder = "deal_queue_order";

  /**
   * 延迟队列名称
   */
  public static final String orderDelayQueue = "order_delay_queue";

  /**
   * 进入延迟队列路由key
   */
  public static final String orderDelayQueueRoutingKey = "order_delay_queue_routing_key";

  /**
   * 进入到死信队列路由key
   */
  public static final String dealQueueOrderRoutingKey = "deal_queue_order_routing_key";

  /**
   * 死信队列 交换机标识符
   */
  public final static String DEAD_LETTER_QUEUE_KEY = "x-dead-letter-exchange";

  /**
   * 死信队列 路由key
   */
  public final static String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

  @Autowired
  private CachingConnectionFactory connectionFactory;


  /**
   * 声明交换机 Topic类型 也可使用dirct路由
   * @return
   */
  @Bean
   public Exchange orderEventExchange() {
    return  new TopicExchange(eventExchange, true, false);
  }


  /**
   * 消息转换器
   * @return
   */
  @Bean
  public MessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  /**
   * 创建延迟队列
   * @return
   */
  @Bean
  public Queue orderDelayQueue() {
    Map<String, Object> args = new HashMap<>(3);
    // args.put("x-message-ttl", 5 * 1000 * 5); // 直接设置延迟队列进入死信队列时间
    args.put(DEAD_LETTER_ROUTING_KEY, dealQueueOrderRoutingKey);
    args.put(DEAD_LETTER_QUEUE_KEY, dealEventExchange);
    return new Queue(orderDelayQueue, true, false,false, args);
  }


  /**
   * 创建死信队列
   * @return
   */
  @Bean
  public Queue dealQueueOrder() {
    return new Queue(dealQueueOrder, true, false,false);
  }

  /**
   * 死信队列绑定交换机路由
   * @return
   */
  @Bean
  public Binding dealQueueOrderBinding() {
    return new Binding(dealQueueOrder, Binding.DestinationType.QUEUE, eventExchange,dealQueueOrderRoutingKey, null);
  }

  /**
   * 延迟队列绑定交换机路由
   * @return
   */
  @Bean
  public Binding orderDelayQueueBinding() {
    return new Binding(orderDelayQueue, Binding.DestinationType.QUEUE, dealEventExchange,orderDelayQueueRoutingKey, null);
  }





}
