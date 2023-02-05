package cn.wzs.book.concurrentProgrammingPractice.common.threadpool;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工厂
 */
public class ThreadPoolUtils {

    public static ThreadPoolExecutor TASK_EXECUTE = new ThreadPoolExecutor(
            10,
            10,
            0L,
            TimeUnit.MILLISECONDS
            , new SynchronousQueue<>());
}
