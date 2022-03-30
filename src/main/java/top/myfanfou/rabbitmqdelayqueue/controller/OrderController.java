package top.myfanfou.rabbitmqdelayqueue.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.myfanfou.rabbitmqdelayqueue.config.RabbitMQConfig;
import top.myfanfou.rabbitmqdelayqueue.model.OrderMessage;

import java.util.UUID;

/**
 * @Author: laasc
 * @Date: 2022/3/30 16:45
 * @Description: 订单接口控制器
 * @Version: 1.0
 */

@RestController
@RequestMapping("/api/order/v1")
@Slf4j
public class OrderController {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private RabbitMQConfig rabbitMqConfig;


  /**
   * 摸拟订单提交
   * @return
   */
  @GetMapping("test/submit_order")
  public Object submit() {

    String orderId = UUID.randomUUID().toString();
    OrderMessage orderMessage = new OrderMessage();
    orderMessage.setOutTradeNo(orderId);
    log.info("发送到mq的订单号=====》: {}", orderId);
    /**
     * 1.发送到交换机的名称
     * 2.消息对象
     * 3.配置过期时间
     */
    rabbitTemplate.convertAndSend(RabbitMQConfig.eventExchange, RabbitMQConfig.orderDelayQueueRoutingKey, orderMessage, message -> {
      // 如果配置了 params.put("x-message-ttl", 5 * 1000); 这一句可以省略,具体根据业务需要是声明 Queue 的时候就指定好延迟时间还是在发送自己控制时间
      message.getMessageProperties().setExpiration(1000 * 15 + "");
      return message;
    });

    return  "{'order_id': '"+ orderId +"'}";
  }

}
