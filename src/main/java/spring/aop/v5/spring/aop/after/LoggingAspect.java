package spring.aop.v5.spring.aop.after;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggingAspect {


	@Before("args(name)")
	public void allSingleStringArgsBeforeAdvice(String name){
		System.out.println("Before advice: a method that takes String arg has been called. Input string: " + name);
		System.out.println();
	}

	@After("args(name)")
	public void allSingleStringArgsAfterAdvice(String name){
		System.out.println("After advice: a method that takes String arg has been called. Input string: " + name);
		System.out.println();
	}

	/**
	 * Only if target method was executed successfully 
	 * @param name
	 */
	@AfterReturning("args(name)")
	public void allSingleStringArgsAfterReturningAdvice(String name){
		System.out.println("AfterReturning advice: a method that takes String arg has been called. Input string: " + name);
		System.out.println();
	}

	/**
	 * Only if target method throwed exception
	 * @param name
	 */
	@AfterThrowing("args(name)")
	public void allSingleStringArgsAfterThrowingAdvice(String name){
		System.out.println("AfterThrowing advice: a method that takes String arg has been called. Input string: " + name);
		System.out.println();
	}

	/**
	 * Capturing return value
	 * @param returningString
	 */
	@AfterReturning(pointcut="execution(public String spring.aop.v5.spring.aop.after.TriangleModel.getName())", returning="returningString")
	public void loggingTriangleWithReturnValueAdvice(String returningString) {
		System.out.println("Method TriangleModel.getName() returns next string: " + returningString);
	}

	/**
	 * Capturing return value
	 * @param
	 */
	@AfterThrowing(pointcut="execution(public String spring.aop.v5.spring.aop.after.TriangleModel.getNameWithException())", throwing="ex")
	public void loggingTriangleWithExceptionAdvice(Exception ex) {
		System.out.println("Method TriangleModel.getName()throws next exception: " + ex.toString());
	}
}
