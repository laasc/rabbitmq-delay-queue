package top.myfanfou.rabbitmqdelayqueue.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.myfanfou.rabbitmqdelayqueue.config.RabbitMQConfig;
import top.myfanfou.rabbitmqdelayqueue.model.OrderMessage;

import java.io.IOException;

/**
 * @Author: laasc
 * @Date: 2022/3/30 17:18
 * @Description: mq消费监听器
 */
@Component
@Slf4j
@RabbitListener(queues = RabbitMQConfig.dealQueueOrder)
public class OrderFailureListener {

  /**
   * 延时关单监听器
   * @param orderMessage
   * @param message
   * @param channel
   */
  @RabbitHandler
  public void closeOrder(OrderMessage orderMessage, Message message, Channel channel) throws IOException {
    log.info("监听到延时消息订单号========》：{}", orderMessage.getOutTradeNo());

    // 手动确认消息
    long msgTag = message.getMessageProperties().getDeliveryTag();

    try {
      // 判断是否支付，如果支付取消关单，未支付关闭订单 这里可以调用其他接口完成订单的状态确认，我这里直接就写完成订单 TODO
      boolean flag = true;
      if(flag) {
        // 确认消费
        channel.basicAck(msgTag, false);
        log.info("延时订单消费成功");
      }else {
        // 重新投递
        
        channel.basicReject(msgTag, true);
      }

    }catch (IOException e) {
      log.error("延时关单异常：{}", orderMessage);
      channel.basicReject(msgTag, true);
    }

  }

}
