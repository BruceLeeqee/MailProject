package cn.enjoy.mall.service;

import cn.enjoy.mall.model.Order;
import cn.enjoy.mall.vo.KillGoodsSpecPriceDetailVo;
import cn.enjoy.mall.vo.KillOrderVo;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/order/mall/service/IKillOrderService")
public interface IKillOrderService {

    @RequestMapping(value = "/queryOrderByUserId", method = RequestMethod.POST)
    List<Order> queryOrderByUserId(@RequestParam("userId") String userId);

    @RequestMapping(value = "/killOrder", method = RequestMethod.POST)
    Long killOrder(@RequestParam("addressId") int addressId, @RequestBody KillGoodsSpecPriceDetailVo killGoods, @RequestParam("userId") String userId);

    @RequestMapping(value = "/killOrder2", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    Long killOrder(@RequestBody KillOrderVo killOrderVo);

    @RequestMapping(value = "/selectMyOrderDetail", method = RequestMethod.POST)
    Order selectMyOrderDetail(@RequestParam("orderId") Long orderId, @RequestParam("userId") String userId);

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    void cancel(@RequestParam("orderId") Long orderId);

    @RequestMapping(value = "/selfCancel", method = RequestMethod.POST)
    void selfCancel(@RequestParam("orderId") Long orderId, @RequestParam("userId") String userId);

    @RequestMapping(value = "/autoCancelExpiredOrder")
    void autoCancelExpiredOrder(); //自动取消过期订单

    @RequestMapping(value = "/queryOrderNum", method = RequestMethod.POST)
    Integer queryOrderNum(@RequestParam("type") Integer type, @RequestParam("userId") String userId);

    @RequestMapping(value = "/confirmReceiveGoods", method = RequestMethod.POST)
    void confirmReceiveGoods(@RequestParam("orderId") Long orderId, @RequestParam("userId") String userId);
}
