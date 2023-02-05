package cn.wzs.book.concurrentProgrammingPractice.chapter_2;


import cn.wzs.book.concurrentProgrammingPractice.common.annotations.NotThreadSafe;
import cn.wzs.book.concurrentProgrammingPractice.common.annotations.ThreadSafe;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class UnsafeCacheFactor extends HttpServlet {


    private AtomicInteger lastReq = new AtomicInteger(0);
    private AtomicInteger lastResult = new AtomicInteger(0);

    @NotThreadSafe
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Integer.parseInt(req.getParameter("i")) == lastReq.get()) {
            resp.getWriter().write(lastResult.get());
        } else {
            // 保存作计算
            lastReq.set(Integer.parseInt(req.getParameter("i")));
            lastResult.set(0);
        }
        super.service(req, resp);
    }

    /*
    枷锁保证线程安全
     */
    @ThreadSafe
    protected synchronized void safeService(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Integer.parseInt(req.getParameter("i")) == lastReq.get()) {
            resp.getWriter().write(lastResult.get());
        } else {
            // 保存作计算
            lastReq.set(Integer.parseInt(req.getParameter("i")));
            lastResult.set(0);
        }
        super.service(req, resp);
    }
}
