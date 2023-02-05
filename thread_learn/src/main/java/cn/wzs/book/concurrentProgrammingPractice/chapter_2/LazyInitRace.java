package cn.wzs.book.concurrentProgrammingPractice.chapter_2;

import cn.wzs.book.concurrentProgrammingPractice.common.annotations.NotThreadSafe;
import cn.wzs.book.concurrentProgrammingPractice.common.annotations.ThreadSafe;

/**
 * 单例模式的延迟初始化
 * 双重校验锁
 */
public class LazyInitRace {

    private LazyInitRace instance = null;

    @NotThreadSafe
    public LazyInitRace getInstance() {
        /**
         * 存在竟态条件
         * 线程A、B同时进入到instance == null,
         * 同时进行初始化，导致两个线程获取的数据不一致
         */
        if (instance == null) {
            instance = new LazyInitRace();
        }
        return instance;
    }

    @ThreadSafe
    public LazyInitRace safeGetInstance() {
        if (instance == null) {
            synchronized (this) {
                if (instance == null) {
                    instance = new LazyInitRace();
                }
            }
        }
        return instance;
    }
}
