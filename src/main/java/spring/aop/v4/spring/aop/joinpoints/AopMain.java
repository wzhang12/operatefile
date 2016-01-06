package spring.aop.v4.spring.aop.joinpoints;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AopMain {
	public static void main (String [] args){
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("v4.spring.aop.joinpoints.xml");
		ShapeService shapeService = context.getBean("shapeService", ShapeService.class);
		shapeService.getCircle().getName();
		shapeService.getCircle().setName("new circle");
		
		//shapeService.getTriangle().getName();
	
	}
}
