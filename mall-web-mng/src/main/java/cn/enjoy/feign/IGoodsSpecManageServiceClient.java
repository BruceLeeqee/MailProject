package cn.enjoy.feign;

import cn.enjoy.mall.service.manage.IGoodsSpecManageService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "MALL-PRODUCT-SERVICE")
public interface IGoodsSpecManageServiceClient extends IGoodsSpecManageService {
}
