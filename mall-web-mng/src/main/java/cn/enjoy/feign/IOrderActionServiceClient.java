package cn.enjoy.feign;

import cn.enjoy.mall.service.IOrderActionService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "MALL-ORDER-SERVICE")
public interface IOrderActionServiceClient extends IOrderActionService {
}
