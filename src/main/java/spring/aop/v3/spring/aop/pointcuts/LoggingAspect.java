package spring.aop.v3.spring.aop.pointcuts;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggingAspect {

	@Pointcut("within(spring.aop.v3.spring.aop.pointcuts.TriangleModel)")
	public void allTriangleModelMethods(){}

	//@Pointcut(args("spring.aop.v3.spring.aop.pointcuts.Triangle"))

	@Before("allTriangleModelMethods())")
	public void allTriangleModelAdvice(){
		System.out.println("advice run: allTriangleModelAdvice() ");
	}

	@Pointcut("execution(public String getName())")
	public void allGetters(){
		//dummy method - holds point cut expression
	}

	//@Before("execution(public String getName())")
	@Before("allGetters() || allTriangleModelMethods()")
	public void loggingFirstAdvice() {
		System.out
				.println("advice run (first): method public String getName() was called");
	}

	//@Before("execution(public String getName())")
	@Before("allGetters()")
	public void loggingSecondFirstAdvice() {
		System.out
				.println("advice run (second): method public String getName() was called");
	}
}
