package spring.aop.v2.spring.aop.wildcards;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Advices with wildcards - single Advice for different points of execution (different methods)
 * @author rgederin
 *
 */
@Aspect
public class LoggingAspect {
	@Before("execution(public String getName())")
	public void loggingAdvice() {
		System.out
				.println("advice run: method public String getName() was called");
	}
	
	@Before("execution(public * get*())")
	public void loggingGettersAdvice() {
		System.out
				.println("advice run: getter method without params was called");
	}
	
	@Before("execution(public * set*(..))")
	public void loggingSettersAdvice() {
		System.out
				.println("advice run: setter method was called");
	}
}
