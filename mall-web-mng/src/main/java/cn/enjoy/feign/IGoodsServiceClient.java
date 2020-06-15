package cn.enjoy.feign;

import cn.enjoy.mall.service.IGoodsService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "MALL-PRODUCT-SERVICE")
public interface IGoodsServiceClient extends IGoodsService {
}
