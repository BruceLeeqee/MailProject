package cn.enjoy.kill.service.impl;

import cn.enjoy.kill.dao.OrderActionMapper;
import cn.enjoy.mall.model.Order;
import cn.enjoy.mall.model.OrderAction;
import cn.enjoy.mall.service.IKillOrderActionService;
import com.alibaba.fastjson.JSONObject;
import com.baidu.fsg.uid.impl.CachedUidGenerator;
import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@RestController
public class OrderActionServiceImpl implements IKillOrderActionService {
    @Resource
    private OrderActionMapper orderActionMapper;

    @Autowired
    private DefaultUidGenerator defaultUidGenerator;

    @Autowired
    private CachedUidGenerator cachedUidGenerator;

    @Override
    public void save(Order order, String action, String userId) {
        this.save(order, action, userId, null);
    }
    @Override
    public Long savePre(String orderStr, Map map , String action, String userId, String remark) {
        Order order = JSONObject.parseObject(orderStr,Order.class);
        OrderAction orderAction = new OrderAction();
        orderAction.setActionId(defaultUidGenerator.getUID());
        orderAction.setActionUser(userId);
        orderAction.setLogTime(System.currentTimeMillis());
        orderAction.setOrderId(order.getOrderId());
        orderAction.setOrderStatus(order.getOrderStatus());
        orderAction.setPayStatus(order.getPayStatus());
        orderAction.setShippingStatus(order.getShippingStatus());
        orderAction.setStatusDesc(action);
        orderAction.setActionNote(remark);
        if(map !=null&&map.get("trade_type")!=null){
            orderAction.setTradeType(map.get("trade_type").toString());
        }
        if(map !=null&&map.get("prepay_id")!=null){
            orderAction.setPrepayId(map.get("prepay_id").toString());
        }
        if(map !=null&&map.get("code_url")!=null){
            orderAction.setCodeUrl(map.get("code_url").toString());
        }
        orderActionMapper.insert(orderAction);
        return orderAction.getActionId();
    }

    @Override
    public Long savePre(String orderStr, String action, String userId, String remark) {
        Order order = JSONObject.parseObject(orderStr,Order.class);
        OrderAction orderAction = new OrderAction();
        orderAction.setActionId(defaultUidGenerator.getUID());
        orderAction.setActionUser(userId);
        orderAction.setLogTime(System.currentTimeMillis());
        orderAction.setOrderId(order.getOrderId());
        orderAction.setOrderStatus(order.getOrderStatus());
        orderAction.setPayStatus(order.getPayStatus());
        orderAction.setShippingStatus(order.getShippingStatus());
        orderAction.setStatusDesc(action);
        orderAction.setActionNote(remark);
        orderActionMapper.insert(orderAction);
        return orderAction.getActionId();
    }

    @Override
    public Long updatePre(Long actionId,Map map ) {
        OrderAction orderAction = orderActionMapper.selectByPrimaryKey(actionId);
        if(map.get("trade_type")!=null){
            orderAction.setTradeType(map.get("trade_type").toString());
        }
        if(map.get("prepay_id")!=null){
            orderAction.setPrepayId(map.get("prepay_id").toString());
        }
        if(map.get("code_url")!=null){
            orderAction.setCodeUrl(map.get("code_url").toString());
        }
        orderActionMapper.updateByPrimaryKey(orderAction);
        return orderAction.getActionId();
    }


    @Override
    public void save(Order order, String action, String userId, String remark) {
        OrderAction orderAction = new OrderAction();
        orderAction.setActionId(defaultUidGenerator.getUID());
        orderAction.setOrderType(order.getOrderType());
        orderAction.setActionUser(userId);
        orderAction.setLogTime(System.currentTimeMillis());
        orderAction.setOrderId(order.getOrderId());
        orderAction.setOrderStatus(order.getOrderStatus());
        orderAction.setPayStatus(order.getPayStatus());
        orderAction.setShippingStatus(order.getShippingStatus());
        orderAction.setStatusDesc(action);
        orderAction.setActionNote(remark);
        orderActionMapper.insert(orderAction);
    }

    @Override
    public OrderAction queryByPrepayId(String prepayId) {
        OrderAction orderAction = new OrderAction();
        orderAction = orderActionMapper.queryByPrepayId(prepayId);
        return orderAction;
    }

    @Override
    public List<OrderAction> queryByOrderId(Long orderId) {
        return orderActionMapper.queryByOrderId(orderId);
    }
}
