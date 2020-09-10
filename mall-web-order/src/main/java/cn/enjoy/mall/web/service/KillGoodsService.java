package cn.enjoy.mall.web.service;

import cn.enjoy.core.utils.GridModel;
import cn.enjoy.mall.constant.KillConstants;
import cn.enjoy.mall.model.KillGoodsPrice;
import cn.enjoy.mall.model.Order;
import cn.enjoy.mall.service.IKillOrderService;
import cn.enjoy.mall.service.manage.IKillSpecManageService;
import cn.enjoy.mall.vo.KillGoodsSpecPriceDetailVo;
import cn.enjoy.mall.vo.KillOrderVo;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 秒杀商品相关
 */
@Slf4j
@Service
public class KillGoodsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IKillSpecManageService iKillSpecManageService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SecKillSender secKillSender;

    @Autowired
    private IKillOrderService orderService;

    @Autowired
    private IKillSpecManageService killSpecManageService;

    @Autowired
    private CacheManager cacheManager;

    /**
     * 避免缓存雪崩情况出现
     *
     * @param
     * @return
     * @throws Exception
     * @author Jack
     * @date 2020/9/4
     * @version
     */
    public GridModel<KillGoodsSpecPriceDetailVo> queryByPage() {
        //1、先从缓存里面拿
        GridModel<KillGoodsSpecPriceDetailVo> gridModel = (GridModel) redisTemplate.opsForValue().get(KillConstants.KILLGOODS_LIST);
        if (null != gridModel) {
            return gridModel;
        }

        //所有线程在这里等待，避免大量请求怼到数据库，只有获取锁成功的线程允许去查询数据库
        synchronized (iKillSpecManageService) {
            //1、获取到锁后，先从缓存里面拿
            gridModel = (GridModel) redisTemplate.opsForValue().get(KillConstants.KILLGOODS_LIST);
            if (null != gridModel) {
                return gridModel;
            }

            //2、缓存里面没有，再去数据库拿
            gridModel = iKillSpecManageService.queryView(1, 100);

            //3、如果数据库里面能拿到就设置到缓存中
            if (null != gridModel) {
                redisTemplate.opsForValue().set(KillConstants.KILLGOODS_LIST, gridModel, 50000, TimeUnit.MILLISECONDS);//set缓存
            }
        }
        return gridModel;
    }

    public KillGoodsSpecPriceDetailVo detail(Integer id) {
        String killgoodDetail = KillConstants.KILLGOOD_DETAIL + id;

        //1、先查询本地缓存有没有
        Cache killgoodsCache = cacheManager.getCache("killgoodDetail");
        KillGoodsSpecPriceDetailVo killGoodsPrice = null;

        //2、如果本地缓存中有，直接返回
        if (null != killgoodsCache.get(killgoodDetail)) {
            log.info(Thread.currentThread().getName() + "---------ehcache缓存中得到数据----------");
            killGoodsPrice = (KillGoodsSpecPriceDetailVo)killgoodsCache.get(killgoodDetail).getObjectValue();
            return killGoodsPrice;
        }

        //3、如果本地缓存中没有，则走redis缓存
        killGoodsPrice = (KillGoodsSpecPriceDetailVo) redisTemplate.opsForValue()
                .get(killgoodDetail);
        if (null != killGoodsPrice) {
            log.info(Thread.currentThread().getName() + "---------redis缓存中得到数据----------");
            return killGoodsPrice;
        }
        //4、本地缓存，redis缓存都没有，走数据库，防止缓存雪崩情况出现，这里加锁
        synchronized (iKillSpecManageService) {
            //2、如果本地缓存中有，直接返回
            if (null != killgoodsCache.get(killgoodDetail)) {
                log.info(Thread.currentThread().getName() + "---------ehcache缓存中得到数据----------");
                killGoodsPrice = (KillGoodsSpecPriceDetailVo)killgoodsCache.get(killgoodDetail).getObjectValue();
                return killGoodsPrice;
            }

            killGoodsPrice = (KillGoodsSpecPriceDetailVo) redisTemplate.opsForValue().get(killgoodDetail);
            if (null != killGoodsPrice) {
                log.info(Thread.currentThread().getName() + "---------redis缓存中得到数据----------");
                return killGoodsPrice;
            }

            killGoodsPrice = iKillSpecManageService.detailById(id);
            if (null != killGoodsPrice) {
                killgoodsCache.putIfAbsent(new Element(killgoodDetail,killGoodsPrice));
                //缓存2天，redis缓存时间比本地缓存长
                redisTemplate.opsForValue().set(killgoodDetail, killGoodsPrice, 2, TimeUnit.DAYS);//set缓存
            }
        }
        return killGoodsPrice;
    }

/*    public KillGoodsSpecPriceDetailVo detail(Integer id) {
        String killgoodDetail = KillConstants.KILLGOOD_DETAIL + id;
        KillGoodsSpecPriceDetailVo killGoodsPrice = (KillGoodsSpecPriceDetailVo) redisTemplate.opsForValue()
                .get(killgoodDetail);
        if (null != killGoodsPrice) {
            log.info(Thread.currentThread().getName() + "---------缓存中得到数据----------");
            return killGoodsPrice;
        }
        synchronized (iKillSpecManageService) {
            killGoodsPrice = (KillGoodsSpecPriceDetailVo) redisTemplate.opsForValue().get(killgoodDetail);
            if (null != killGoodsPrice) {
                log.info(Thread.currentThread().getName() + "---------缓存中得到数据----------");
                return killGoodsPrice;
            }

            killGoodsPrice = iKillSpecManageService.detailById(id);
            if (null != killGoodsPrice) {
                redisTemplate.opsForValue().set(killgoodDetail, killGoodsPrice, 50000, TimeUnit.MILLISECONDS);//set缓存
            }
        }
        return killGoodsPrice;
    }*/

    /**
     * @param killId
     * @param userId
     * @return boolean
     * @throws Exception
     * @author Jack
     * @date 2020/8/3
     * @version
     */
    public boolean kill(String killId, String userId) {

        final String killGoodCount = KillConstants.KILL_GOOD_COUNT + killId;
        try {
            long count = redisTemplate.opsForValue().increment(killGoodCount, -1);
            Object obj = redisTemplate.execute(new SessionCallback() {
                @Override
                public Object execute(RedisOperations operations) throws DataAccessException {

                    operations.watch(killGoodCount);
                    Object val = operations.opsForValue().get(killGoodCount);
                    int valint = Integer.valueOf(val.toString());

                    if (valint > 0) {
                        operations.multi();
                        operations.opsForValue().increment(killGoodCount, -1);
                        Object rs = operations.exec();
                        System.out.println(rs);
                        return rs;
                    }

                    return null;
                }
            });
            if (null != obj) {
                redisTemplate.opsForSet().add(KillConstants.KILLGOOD_USER, killId + userId);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * @param killId
     * @param userId
     * @return boolean
     * @throws Exception
     * @author Jack
     * @date 2020/8/4
     * @version
     */
    public boolean secKill(int killId, String userId) {
//        long is = redisTemplate.opsForSet().add(KillConstants.KILLED_GOOD_USER+killId,userId);
//        if (is == 0){//判断用户已经秒杀过，直接返回当次秒杀失败
////            return false;
//        }
        Boolean member = redisTemplate.opsForSet().isMember(KillConstants.KILLED_GOOD_USER + killId, userId);
        if (member) {
            logger.info("--------userId:" + userId + "--has secKilled");
            return false;
        }

        final String killGoodCount = KillConstants.KILL_GOOD_COUNT + killId;
        if (redisTemplate.opsForValue().increment(killGoodCount, -1) < 0) {
            logger.info("--------Insufficient stock:------------");
            return false;
        }

        //秒杀成功，缓存秒杀用户和商品
        redisTemplate.opsForSet().add(KillConstants.KILLGOOD_USER, killId + userId);
        return true;
    }

    /**
     * 基于数据库的秒杀实现
     *
     * @param
     * @return
     * @throws Exception
     * @author Jack
     * @date 2020/8/5
     * @version
     */
    public boolean secKillByDb(int killId, String userId) {

        //1、先判断有没有库存，没有库存就直接秒杀结束
        KillGoodsPrice kgp = killSpecManageService.selectByPrimaryKey(killId);
        if (kgp.getKillCount() <= 0) {
            logger.info("--------Insufficient stock:------------");
            return false;
        }

        //2、先判断该用户是否已经秒杀
        List<Order> orders = orderService.queryOrderByUserId(userId);
        if (orders != null && orders.size() > 0) {
            logger.info("--------userId:" + userId + "--has secKilled");
            return false;
        }

        KillGoodsPrice killGoodsPrice = new KillGoodsPrice();
        killGoodsPrice.setKillCount(1);
        killGoodsPrice.setId(killId);
        int i = killSpecManageService.updateSecKill(killGoodsPrice);

        //返回为0，秒杀完了
        if (i == 0) {
            logger.info("--------Insufficient stock:------------");
            return false;
        }

        //秒杀成功，缓存秒杀用户和商品
        redisTemplate.opsForSet().add(KillConstants.KILLGOOD_USER, killId + userId);
        return true;
    }

    public boolean chkKillOrder(String killId, String userId) {
        //校验用户和商品是否有缓存，无则表明当前是非法请求
        boolean isKilld = redisTemplate.opsForSet().isMember(KillConstants.KILLGOOD_USER, killId + userId);
        if (isKilld) {
            redisTemplate.opsForSet().remove(KillConstants.KILLGOOD_USER, killId + userId);
        }
        return isKilld;
    }

    public String submitOrder(Long addressId, int killId, String userId) {
        KillGoodsSpecPriceDetailVo killGoods = detail(killId);

        KillOrderVo vo = new KillOrderVo();
        vo.setUserId(userId);
        vo.setKillGoodsSpecPriceDetailVo(killGoods);
        vo.setAddressId(addressId);

        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();

        //订单有效时间3秒
        String kill_order_user = KillConstants.KILL_ORDER_USER + killId + userId;
        valueOperations.set(kill_order_user, KillConstants.KILL_ORDER_USER_UNDO, 3000, TimeUnit.MILLISECONDS);
        /*同步转异步，发送到消息队列*/
        secKillSender.send(vo);

        String orderId = "";
        try {
            while (true) {
                orderId = valueOperations.get(kill_order_user);
                if (null == orderId) {//处理超时，则直接置秒杀失败，取消秒杀订单
                    return null;
                }
                if (!KillConstants.KILL_ORDER_USER_UNDO.equals(orderId)) {//订单已处理成功
                    stringRedisTemplate.delete(kill_order_user);
                    return orderId.toString();//
                }
                Thread.sleep(300l);//300ms轮循1次
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String submitOrderByDb(Long addressId, int killId, String userId) {
        KillGoodsSpecPriceDetailVo killGoods = detail(killId);

        KillOrderVo vo = new KillOrderVo();
        vo.setUserId(userId);
        vo.setKillGoodsSpecPriceDetailVo(killGoods);
        vo.setAddressId(addressId);

        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();

        //订单有效时间3秒
        String kill_order_user = KillConstants.KILL_ORDER_USER + killId + userId;
        valueOperations.set(kill_order_user, KillConstants.KILL_ORDER_USER_UNDO, 3000, TimeUnit.MILLISECONDS);
        /*同步转异步，发送到消息队列*/
//        secKillSender.send(vo);
        Long orderId = orderService.killOrder(vo);

        String flag = valueOperations.get(kill_order_user);
        if (null == flag) {//处理超时，则直接置秒杀失败，取消秒杀订单
            orderService.cancel(orderId);
            stringRedisTemplate.delete(kill_order_user);
            return null;
        }
        return orderId + "";
    }
}
