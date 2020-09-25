package cn.enjoy.mall.web.service;

import cn.enjoy.mall.constant.KillConstants;
import cn.enjoy.mall.lock.RedisLock;
import cn.enjoy.mall.model.KillGoodsPrice;
import cn.enjoy.mall.service.manage.IKillSpecManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * @Classname KillQueueUtil
 * @Description TODO
 * @Author Jack
 * Date 2020/9/15 22:03
 * Version 1.0
 */
@Slf4j
@Component
public class KillQueueUtil {

    @Autowired
    private KillGoodsService killGoodsService;

    @Autowired
    private IKillSpecManageService iKillSpecManageService;

    @Resource
    private RedisTemplate redisTemplate;

    private final int QUEUE_LENGTH = 10000;

    /**
     * 是一个适用于高并发场景下的队列，通过无所的方式，实现了高并发状态下的高性能，
     * 通常ConcurrentLinkedQueue性能好于BlockingQueue。
     * 他是一个基于连接节点的无界线程安全队列。
     */
    private Queue<KUBean> queue = new ConcurrentLinkedQueue<KUBean>();

    private ScheduledExecutorService ses = Executors.newScheduledThreadPool(4);

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public void addQueue(KUBean kuBean) {
        queue.offer(kuBean);
    }

    public KillQueueUtil() {
        execute();
    }

    private void execute() {
        ses.scheduleWithFixedDelay(() -> {
            KUBean kuBean = queue.poll();
            if (kuBean != null) {
                stock(kuBean.getKillId(), kuBean.getUserId());
            }
        }, 0, 1, TimeUnit.MILLISECONDS);
    }

    private void stock(Integer killId, String userId) {
        final String killGoodCount = KillConstants.KILL_GOOD_COUNT + killId;
        long stock = killGoodsService.stock(killGoodCount, 1,killGoodsService.STOCK_LUA);
        // 初始化库存
        if (stock == killGoodsService.UNINITIALIZED_STOCK) {
            RedisLock redisLock = new RedisLock(redisTemplate, "stock:lock");
            try {
                // 获取锁
                if (redisLock.tryLock()) {
                    // 双重验证，避免并发时重复回源到数据库
                    stock = killGoodsService.stock(killGoodCount, 1,killGoodsService.STOCK_LUA);
                    if (stock == killGoodsService.UNINITIALIZED_STOCK) {
                        // 获取初始化库存
                        KillGoodsPrice killGoodsPrice = iKillSpecManageService.selectByPrimaryKey(killId);
                        // 将库存设置到redis
                        redisTemplate.opsForValue().set(killGoodCount, killGoodsPrice.getKillCount().intValue(), 60 * 60, TimeUnit.SECONDS);
                        // 调一次扣库存的操作
                        stock = killGoodsService.stock(killGoodCount, 1,killGoodsService.STOCK_LUA);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                redisLock.unlock();
            }
        }

        if (stock >= 0) {
            log.info("------秒杀成功--------");
        } else {
            log.info("------秒杀失败--------");
        }
    }
}
