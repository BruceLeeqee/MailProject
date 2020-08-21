package cn.enjoy.mq;

import cn.enjoy.mall.constant.PayStatus;
import cn.enjoy.mall.constant.PayType;
import cn.enjoy.mall.model.Order;
import cn.enjoy.mall.service.IKillOrderActionService;
import cn.enjoy.mall.service.IKillOrderService;
import cn.enjoy.mall.service.IOrderActionService;
import cn.enjoy.mall.service.IOrderService;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 类说明：
 */
@Slf4j
@Component
public class OrderLogReceiver implements ChannelAwareMessageListener {

    @Resource
    private IKillOrderService killorderService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderActionService orderActionService;

    @Autowired
    private IKillOrderActionService killOrderActionService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            String orderId = new String(message.getBody());
            log.info("OrderLogReceiver>>>>>>>接收到消息:" + orderId);
            try {

                //1、根据订单id查询是什么订单
                Order order = orderService.selectOrderDetail(Long.valueOf(orderId));
                if(order != null) {
                    String orderStr = JSONObject.toJSONString(order);
                    orderActionService.savePre(orderStr,"微信支付成功",order.getUserId(),"微信支付成功");
                    order.setPayStatus(PayStatus.PAID.getCode());
                    order.setPayCode("weixin");
                    order.setPayName(PayType.getDescByCode("weixin"));
                    order.setPayTime(System.currentTimeMillis());
                    orderService.updateOrder(order);
                } else {
                    Order killorder = killorderService.search(Long.valueOf(orderId));
                    String orderStr = JSONObject.toJSONString(killorder);
                    killOrderActionService.savePre(orderStr,"微信支付成功",killorder.getUserId(),"微信支付成功");
                    killorder.setPayStatus(PayStatus.PAID.getCode());
                    killorder.setPayCode("weixin");
                    killorder.setPayName(PayType.getDescByCode("weixin"));
                    killorder.setPayTime(System.currentTimeMillis());
                    killorderService.updateOrder(killorder);
                }

                log.info("OrderLogReceiver>>>>>>消息已消费");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);//手工确认，可接下一条
            } catch (Exception e) {
                System.out.println(e.getMessage());
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);//失败，则直接忽略此订单

                log.info("OrderLogReceiver>>>>>>拒绝消息，直接忽略");
                throw e;
            }

        } catch (Exception e) {
            log.info(e.getMessage());
        }

    }
}

