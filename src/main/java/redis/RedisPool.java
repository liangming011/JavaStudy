package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.xml.ws.Service;

public class RedisPool {

    private static JedisPool pool = null;

    private Redis分布式锁 lock = new Redis分布式锁(pool);

    int n = 500;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
        config.setMaxTotal(200);
        // 设置最大空闲数
        config.setMaxIdle(8);
        // 设置最大等待时间
        config.setMaxWaitMillis(1000 * 100);
        // 在borrow一个jedis实例时，是否需要验证，若为true，则所有jedis实例均是可用的
        config.setTestOnBorrow(true);
        pool = new JedisPool(config, "127.0.0.1", 6379, 3000);
    }

    public void seckill() throws Exception {
        Jedis jedis = pool.getResource();
        // 返回锁的value值，供释放锁时候进行判断
        boolean identifier = false;

        while(identifier==false){
            identifier = lock.tryGetDistributedLock(jedis,"key1", "resource",1000);
        }
        System.out.println(Thread.currentThread().getName() + "获得了锁");
        System.out.println(--n);
        boolean unlock = false;
        while (unlock==false){
            unlock = lock.releaseDistributedLock(jedis,"key1", "resource");
        }
        System.out.println(Thread.currentThread().getName() + "解除了锁");
        System.out.println(--n);

    }

    /**
     * 测试
     */

    public static class ThreadA extends Thread {
        private RedisPool redisPool;

        public ThreadA(RedisPool redisPool) {
            this.redisPool = redisPool;
        }

        @Override
        public void run() {
            try {
                redisPool.seckill();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class Test {
        public static void main(String[] args) {
            RedisPool redisPool = new RedisPool();
            for (int i = 0; i < 50; i++) {
                ThreadA threadA = new ThreadA(redisPool);
                threadA.start();
            }
        }
    }

}
