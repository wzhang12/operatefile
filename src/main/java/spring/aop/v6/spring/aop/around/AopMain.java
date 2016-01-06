package spring.aop.v6.spring.aop.around;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AopMain {
	public static void main (String [] args) throws Exception{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("v6.spring.aop.around.xml");
		ShapeService shapeService = context.getBean("shapeService", ShapeService.class);
		shapeService.getCircle().getName();
		//shapeService.getCircle().setName("new circle");
		
		
		//shapeService.getCircle().setNameWithException("exception");
		shapeService.getTriangle().getName();
		//shapeService.getTriangle().getNameWithException();
	
	}
}
