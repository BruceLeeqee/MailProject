package cn.enjoy.mall.web.feign;

import cn.enjoy.mall.service.IPayService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "MALL-ORDER-SERVICE")
public interface IPayServiceClient extends IPayService {
}
