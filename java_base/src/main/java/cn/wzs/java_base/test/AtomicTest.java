package cn.wzs.java_base.test;

import java.util.concurrent.atomic.*;

public class AtomicTest {

    Integer atomicUpdateInteger = new Integer(10);

    public static void main(String[] args) {

    }

    /**
     * 通过Unsafe类cas设置指定值，Object包含了其他的所有对象
     */
    public static void testAtomicArray() {
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(8);
        atomicIntegerArray.set(1, 1);

        AtomicReferenceArray<Object> atomicReferenceArray = new AtomicReferenceArray<>(2);
        atomicReferenceArray.set(1, new Object());
    }


    public static void testAtomicReference() {
        // 动态更新对象，可能出现ABA问题，最终结果一致
        AtomicTest test = new AtomicTest();
        AtomicReference reference = new AtomicReference(test);
        test.atomicUpdateInteger = 2;
        reference.set(test);

        // 带有版本号更新，解决ABA问题
        AtomicStampedReference atomicStampedReference = new AtomicStampedReference(test, 1);
        atomicStampedReference.set(test, 2);
        atomicStampedReference.compareAndSet(test,test,3,4);

        // 和AtomicStampedReference类似，处理ABA问题，版本号换成了boolean值
        AtomicMarkableReference atomicMarkableReference = new AtomicMarkableReference(test, false);
        atomicMarkableReference.set(test, true);
    }

    public static void testAtomicFieldUpdate() {
        AtomicTest test = new AtomicTest();
        AtomicIntegerFieldUpdater<AtomicTest> atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(AtomicTest.class, "atomicUpdateInteger");
        atomicIntegerFieldUpdater.set(test, 10);
        // LongFiledUpdater、ReferenceFieldUpdater以此类推
    }


}
