package cn.wzs.book.concurrentProgrammingPractice.common.annotations;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * 线程不安全注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value={METHOD, TYPE})
public @interface NotThreadSafe {

}
