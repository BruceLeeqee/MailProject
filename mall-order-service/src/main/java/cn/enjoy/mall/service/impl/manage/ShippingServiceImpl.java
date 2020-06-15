package cn.enjoy.mall.service.impl.manage;

import cn.enjoy.mall.dao.ShippingMapper;
import cn.enjoy.mall.model.Shipping;
import cn.enjoy.mall.service.manage.IShippingService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Ray
 * @date 2018/3/21.
 */
@RestController
public class ShippingServiceImpl implements IShippingService {

    @Resource
    private ShippingMapper shippingMapper;

    @Override
    public List<Shipping> queryAll() {
        return shippingMapper.queryAll();
    }


}
