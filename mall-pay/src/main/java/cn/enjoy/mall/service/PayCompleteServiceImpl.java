package cn.enjoy.mall.service;

import cn.enjoy.mall.constant.PayStatus;
import cn.enjoy.mall.constant.PayType;
import cn.enjoy.mall.model.Order;
import com.alibaba.fastjson.JSONObject;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 支付完成后的业务处理
 *
 * @Classname PayCompleteServiceImpl
 * @Description TODO
 * @Author Jack
 * Date 2020/8/21 21:01
 * Version 1.0
 */
@Service
public class PayCompleteServiceImpl implements PayCompleteService {

    @Autowired
    private IKillOrderService killorderService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderActionService orderActionService;

    @Autowired
    private IKillOrderActionService killOrderActionService;

    @GlobalTransactional
    @Override
    public void payCompleteBusiness(String orderId) {
        //1、根据订单id查询是什么订单
        Order order = orderService.selectOrderDetail(Long.valueOf(orderId));
        if (order != null) {
            String orderStr = JSONObject.toJSONString(order);
            orderActionService.savePre(orderStr, "微信支付成功", order.getUserId(), "微信支付成功");
            order.setPayStatus(PayStatus.PAID.getCode());
            order.setPayCode("weixin");
            order.setPayName(PayType.getDescByCode("weixin"));
            order.setPayTime(System.currentTimeMillis());
            orderService.updateOrder(order);
        } else {
            Order killorder = killorderService.search(Long.valueOf(orderId));
            String orderStr = JSONObject.toJSONString(killorder);
            killOrderActionService.savePre(orderStr, "微信支付成功", killorder.getUserId(), "微信支付成功");
            killorder.setPayStatus(PayStatus.PAID.getCode());
            killorder.setPayCode("weixin");
            killorder.setPayName(PayType.getDescByCode("weixin"));
            killorder.setPayTime(System.currentTimeMillis());
            killorderService.updateOrder(killorder);
        }
    }
}
