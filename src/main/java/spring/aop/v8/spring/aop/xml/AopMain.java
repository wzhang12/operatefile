package spring.aop.v8.spring.aop.xml;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AopMain {
	public static void main (String [] args) throws Exception{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("v8.spring.aop.xml.xml");
		ShapeService shapeService = context.getBean("shapeService", ShapeService.class);
				
	
		shapeService.getTriangle().getName();
		
	}
}
