package spring.aop.v4.spring.aop.joinpoints;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggingAspect {

	@Pointcut("within(spring.aop.v4.spring.aop.joinpoints.CircleModel)")
	public void allCircleModelMethods() {
	}

	@Before("args(name)")
	public void allSingleStringArgsAdvice(String name){
		System.out.println("A method that takes String arg has been called. Input string: " + name);
		System.out.println();
	}

	@Before("allCircleModelMethods()")
	public void loggingAdvice(JoinPoint joinPoint) {
		System.out.println("To string: " + joinPoint.toString());
		System.out.println("Get Target: " + joinPoint.getTarget());
		System.out.println();
	}
}
