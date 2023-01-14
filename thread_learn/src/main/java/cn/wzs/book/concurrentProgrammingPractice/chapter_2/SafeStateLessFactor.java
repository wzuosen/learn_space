package cn.wzs.book.concurrentProgrammingPractice.chapter_2;

import cn.wzs.book.concurrentProgrammingPractice.common.annotations.ThreadSafe;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

public class SafeStateLessFactor extends HttpServlet {


    /**
     * 不涉及到共享状态,只在单个servlet中做计算，是线程安全的
     * @param servletRequest
     * @param servletResponse
     * @throws ServletException
     * @throws IOException
     */
    @ThreadSafe
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        int i = Integer.parseInt(servletRequest.getParameter("i"));
        int[] factor = factor(i);
        servletResponse.getWriter().write(factor.length);
    }

    int[] factor(int i){
        return new int[0];
    }
}
