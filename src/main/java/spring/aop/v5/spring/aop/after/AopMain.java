package spring.aop.v5.spring.aop.after;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AopMain {
	public static void main (String [] args) throws Exception{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("v5.spring.aop.after.xml");
		ShapeService shapeService = context.getBean("shapeService", ShapeService.class);
		shapeService.getCircle().getName();
		//shapeService.getCircle().setName("new circle");


		//shapeService.getCircle().setNameWithException("exception");
		shapeService.getTriangle().getName();
		shapeService.getTriangle().getNameWithException();

	}
}
