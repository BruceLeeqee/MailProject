package cn.enjoy.feign;

import cn.enjoy.mall.service.manage.IShippingService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "MALL-ORDER-SERVICE")
public interface IShippingServiceClient extends IShippingService {
}
