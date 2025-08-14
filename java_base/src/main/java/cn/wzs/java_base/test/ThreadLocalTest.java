package cn.wzs.java_base.test;

import java.util.concurrent.TimeUnit;


/**
 * InheritableThreadLocal实现原理,在父线程，创建子线程的时候，Thread类内部的inheritableThreadLocals属性就被初始化(如果父线程有值的话)
 * InheritableThreadLocal类获取ThreadLocalMap会使用这个属性，达到子属性访问父属性的目的
 */
public class ThreadLocalTest {

    public static void main(String[] args) {


//        InheritableThreadLocal<Integer> threadLocal = new InheritableThreadLocal<>();  // 可以取出父线程set的值
        ThreadLocal<Integer> threadLocal = new ThreadLocal<>(); // 只能取出自己set的值，父线程set的取不到


        Runnable childTask = () -> {
            System.out.println("child:" + threadLocal.get());
        };

        Runnable parentTask = () -> {

            threadLocal.set(11);
            System.out.println("value set....start child");
            new Thread(childTask).start();
            try {
                TimeUnit.SECONDS.sleep(10L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        new Thread(parentTask).start();
    }


}
