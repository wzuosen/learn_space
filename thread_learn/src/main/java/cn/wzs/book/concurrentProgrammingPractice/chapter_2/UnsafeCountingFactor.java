package cn.wzs.book.concurrentProgrammingPractice.chapter_2;

import cn.wzs.book.concurrentProgrammingPractice.common.annotations.NotThreadSafe;
import cn.wzs.book.concurrentProgrammingPractice.common.annotations.ThreadSafe;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public class UnsafeCountingFactor extends HttpServlet {

    private Long count;

    private AtomicLong atomicLong = new AtomicLong(0);

    /**
     * 共享可变count变量，且未做同步措施，线程不安全
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    @NotThreadSafe
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /*
        竟态条件
         */
        count++;
        super.service(req, resp);
    }


    @ThreadSafe
    protected void safeService(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 原子性操作
        atomicLong.getAndIncrement();
    }
}
