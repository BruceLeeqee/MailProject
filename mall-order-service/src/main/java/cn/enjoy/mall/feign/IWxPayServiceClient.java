package cn.enjoy.mall.feign;

import cn.enjoy.mall.service.IWxPayService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = /*"MALL-PAY-SERVICE"*/"API-GATEWAY")
public interface IWxPayServiceClient extends IWxPayService {
}
