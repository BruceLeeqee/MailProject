package cn.enjoy.mall.service.impl.manage;

import cn.enjoy.core.utils.GridModel;
import cn.enjoy.mall.constant.OrderStatus;
import cn.enjoy.mall.constant.PayStatus;
import cn.enjoy.mall.constant.ShippingStatus;
import cn.enjoy.mall.dao.OrderGoodsMapper;
import cn.enjoy.mall.dao.OrderManageMapper;
import cn.enjoy.mall.dao.OrderMapper;
import cn.enjoy.mall.model.Order;
import cn.enjoy.mall.model.OrderGoods;
import cn.enjoy.mall.service.manage.IOrderManageService;
import cn.enjoy.mall.vo.OrderVo;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单管理
 *
 * @author Jack
 * @date 2018/3/8.
 */
@RestController
//@RequestMapping("/order/mall/service/manage/IOrderManageService")
public class OrderManageServiceImpl implements IOrderManageService {


    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderManageMapper orderManageMapper;
    @Resource
    private OrderGoodsMapper orderGoodsMapper;


    /**
     * 查询订单列表
     *
     * @param page
     * @param pageSize
     * @param params
     * @return
     * @throws Exception
     * @author Jack
     * @date 2020/9/8
     * @version
     */
    //@RequestMapping(value = "/queryByPage", method = RequestMethod.POST)
    @Override
    public GridModel<OrderVo> queryByPage(int page, int pageSize, OrderVo params) {
        PageBounds pageBounds = new PageBounds(page, pageSize);
        return new GridModel<>(orderManageMapper.queryByPage(params, pageBounds));
    }

    /**
     * 查询订单详情
     *
     * @param orderId
     * @return
     * @throws Exception
     * @author Jack
     * @date 2020/9/8
     * @version
     */
    //@RequestMapping(value = "/queryOrderDetail", method = RequestMethod.POST)
    @Override
    public OrderVo queryOrderDetail(Long orderId) {
        return orderMapper.selectOrderById(orderId);
    }

    @Override
    public void save(OrderVo orderVo) {

    }

    @Override
    public void delete(short id) {

    }

    @Override
    public void deleteByIds(String[] ids) {

    }

    /**
     * 查询订单商品
     *
     * @param orderId
     * @return
     * @throws Exception
     * @author Jack
     * @date 2020/9/8
     * @version
     */
    //@RequestMapping(value = "/selectGoodsByOrderId", method = RequestMethod.POST)
    @Override
    public List<OrderGoods> selectGoodsByOrderId(Long orderId) {
        return orderGoodsMapper.selectByOrderId(orderId);
    }

    /**
     * 修改订单记录
     *
     * @param order
     * @return
     * @throws Exception
     * @author Jack
     * @date 2020/9/8
     * @version
     */
    //@RequestMapping(value = "/update", method = RequestMethod.POST)
    @Override
    public int update(Order order) {
        if (OrderStatus.CONFIRMED.getCode().equals(order.getOrderStatus())) {
            order.setConfirmTime(System.currentTimeMillis() / 1000);
        }
        if (PayStatus.PAID.getCode().equals(order.getPayStatus())) {
            order.setPayTime(System.currentTimeMillis() / 1000);
        }
        if (ShippingStatus.SHIPPED.getCode().equals(order.getShippingStatus())) {
            order.setShippingTime(System.currentTimeMillis() / 1000);
        }
        return orderMapper.updateByPrimaryKeySelective(order);
    }
}
