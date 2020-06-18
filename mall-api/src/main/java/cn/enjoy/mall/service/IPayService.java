package cn.enjoy.mall.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Map;

@RequestMapping("/order/mall/service/IPayService")
public interface IPayService {

    @RequestMapping(value = "/doPrePay", method = RequestMethod.POST)
    Map<String, String> doPrePay(@RequestParam("orderId") Integer orderId, @RequestParam("payCode") String payCode,
                                 @RequestParam("payAmount") BigDecimal payAmount, @RequestParam("userId") String userId) ;

    @RequestMapping(value = "/updateByActionId", method = RequestMethod.POST)
    String updateByActionId(@RequestParam("actionId") String actionId) ;

    @RequestMapping(value = "/queryByPrepayId", method = RequestMethod.POST)
    String queryByPrepayId(@RequestParam("prepayId") String prepayId) ;

    @RequestMapping(value = "/doPay", method = RequestMethod.POST)
    String doPay(@RequestParam("orderId") Integer orderId, @RequestParam("payCode") String payCode,
                 @RequestParam("payAmount") BigDecimal payAmount, @RequestParam("userId") String userId) ;
}
