package cn.enjoy.feign;

import cn.enjoy.mall.service.manage.IDeliveryService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "MALL-ORDER-SERVICE")
public interface IDeliveryServiceClient extends IDeliveryService {
}
