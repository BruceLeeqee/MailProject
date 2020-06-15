package cn.enjoy.mall.web.feign;

import cn.enjoy.mall.service.IOrderService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "MALL-ORDER-SERVICE")
public interface IOrderServiceClient extends IOrderService {
}
