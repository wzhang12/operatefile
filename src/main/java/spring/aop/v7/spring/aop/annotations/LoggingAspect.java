package spring.aop.v7.spring.aop.annotations;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class LoggingAspect {
	@Before("@annotation(spring.aop.v7.spring.aop.annotations.Loggable)")
	public void annotationAdvice(JoinPoint joinPoint) {
		System.out.println("method with @Loggable annotation was called: " + joinPoint.getSignature());
	}
}
