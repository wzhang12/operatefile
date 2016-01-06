package spring.aop.v8.spring.aop.xml;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;


public class LoggingAspect {
	
	@Pointcut("within(v8.spring.aop.xml.TriangleModel)")
	public void allTriangleModelMethods(){}
	
	
	@Around("allTriangleModelMethods()")
	public Object aroundAdvice(ProceedingJoinPoint joinPoint){
		Object retObject = null;
		try {
			System.out.println("before advice: before call" );
			retObject = joinPoint.proceed();
			System.out.println("after returning advice: after succsesful call");
		} catch (Throwable e) {
			System.out.println("after throwing advice: after un-succsesful call");
		}
		System.out.println("after advice: after any call");
		return retObject;
	}
}
