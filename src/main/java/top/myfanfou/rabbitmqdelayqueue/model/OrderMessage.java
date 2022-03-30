package top.myfanfou.rabbitmqdelayqueue.model;

/**
 * @Author: laasc
 * @Date: 2022/3/30 16:54
 * @Description: mq订单消息类 可以定义想要的属性
 */
public class OrderMessage {

  public String getOutTradeNo() {
    return outTradeNo;
  }

  public void setOutTradeNo(String outTradeNo) {
    this.outTradeNo = outTradeNo;
  }

  /**
   * 订单号
   */
  private String outTradeNo;

}
