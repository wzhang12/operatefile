package spring.aop.v1.spring.aop.beforeexecution;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class LoggingAspect {
	@Before("execution(public String getName())")
	public void loggingAdvice() {
		System.out
				.println("advice run: method public String getName() was called");
	}

	@Before("execution(public String spring.aop.v1.spring.aop.beforeexecution.TriangleModel.getName())")
	public void loggingTriangleAdvice() {
		System.out
				.println("advice run: method public String spring.aop.v1.spring.aop.beforeexecution.TriangleModel.getName() was called");
	}
}
