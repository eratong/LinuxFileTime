package com.example.demo.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * Description: 单机锁
 * User: jun.lei@counect.com
 * Date: 2018-01-10
 * Time: 10:56
 */
public class LockUtil {
    
    private static final ConcurrentHashMap<Object, ReentrantLock> lockConcurrentHashMap = new ConcurrentHashMap<>();
    
    public static boolean isLock(Object key) {
        return lockConcurrentHashMap.containsKey(key) && lockConcurrentHashMap.get(key).isLocked();
    }
    
    public static boolean tryLock(Object key,
                                  long time,
                                  TimeUnit timeUnit) {
        ReentrantLock lock = getLock(key);
        if (null == lock) {
            lock = lockConcurrentHashMap.get(key);
        }
        try {
            return lock.tryLock(time, timeUnit);
        } catch (Exception e) {
            return false;
        }
    }
    
    public static void lock(Object key) throws InterruptedException {
        ReentrantLock lock = getLock(key);
        if (null == lock) {//lock为null，说明锁是自己放到map中的
            lock = lockConcurrentHashMap.get(key);
        }
        if(lock.tryLock(2, TimeUnit.SECONDS)) {//尝试获得锁，如果2秒内没有获得锁，则放弃
//            System.out.println("是被自己线程锁的，什么也不做");
        }else {
        	throw new InterruptedException(Thread.currentThread().getName()+"尝试对"+key+"上锁，但失败");
        } 
    }
    
    public static void unlock(Object key) {
        ReentrantLock lock = getLock(key);
        if (null != lock) {
            lock.unlock();
            if (!lock.hasQueuedThreads()) {
                lockConcurrentHashMap.remove(key);
            }
        }
    }
    
    private static ReentrantLock getLock(Object key) {
    	ReentrantLock lock = new ReentrantLock();//默认非公平锁，公平锁指的是线程获取锁的顺序是按照加锁顺序来的，而非公平锁指的是抢锁机制，先排队的线程不一定先获得锁。
        return lockConcurrentHashMap.putIfAbsent(key, lock);//putIfAbsent的作用是，如果key存在，则返回key对应的值，如果key不存在，则放置key和value到map中并返回null。
    }
}
