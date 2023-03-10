package cn.wzs.book.concurrentProgrammingPractice.chapter_1;

import cn.wzs.book.concurrentProgrammingPractice.common.annotations.NotThreadSafe;
import cn.wzs.book.concurrentProgrammingPractice.common.annotations.ThreadSafe;
import cn.wzs.book.concurrentProgrammingPractice.common.threadpool.ThreadPoolUtils;

/**
 * 安全性问题demo
 */
public class UnsafeSequence {

    private int value;

    /*
    不安全方法
     */
    @NotThreadSafe
    public int getNext() {
        return value++;
    }

    /*
    安全方法
     */
    @ThreadSafe
    public synchronized int getSafeNext() {
        return value++;
    }

    public static void main(String[] args) {
        UnsafeSequence unsafeSequence = new UnsafeSequence();
        for (int i = 0; i < 10; i++) {
            ThreadPoolUtils.TASK_EXECUTE.submit(() -> {
                unsafeSequence.getNext();
            });
        }
        ThreadPoolUtils.TASK_EXECUTE.shutdown();
        /**
         * 返回结果不固定,可能是10，也可能是5、7、8
         * 有多线程并发问题
         */
        System.out.println(unsafeSequence.getNext());
    }
}
