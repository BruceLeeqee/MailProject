package cn.enjoy.mall.web;

import cn.enjoy.FrontApp;
import cn.enjoy.mall.web.service.KillGoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;

/**
 * @Classname MyTest
 * @Description TODO
 * @Author Jack
 * Date 2020/9/17 13:58
 * Version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {FrontApp.class})
public class MyTest {

    private Logger logger = LoggerFactory.getLogger(getClass());


    private static Integer count = 100;

    private CountDownLatch cdl = new CountDownLatch(count);

    @Autowired
    private KillGoodsService killGoodsService;

    @Test
    public void test1() {
        for (Integer i = 0; i < count; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        cdl.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        killGoodsService.redissonIncr();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    logger.info("===========>" + killGoodsService.getI());
                }
            }).start();
            cdl.countDown();
        }

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        killGoodsService.secKillByRedissonLock(24,"b9702959ebc44774912a59a012d6abfe");
    }

    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void test3() {
        redisTemplate.opsForValue().set("fdasfd",90);
    }
}
