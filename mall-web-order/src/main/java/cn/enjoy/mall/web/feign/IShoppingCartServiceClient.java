package cn.enjoy.mall.web.feign;

import cn.enjoy.mall.service.IShoppingCartService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "MALL-ORDER-SERVICE")
public interface IShoppingCartServiceClient extends IShoppingCartService {
}
