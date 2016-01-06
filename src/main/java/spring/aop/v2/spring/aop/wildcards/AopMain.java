package spring.aop.v2.spring.aop.wildcards;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AopMain {
	public static void main (String [] args){
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("v2.spring.aop.wildcards.xml");
		ShapeService shapeService = context.getBean("shapeService", ShapeService.class);
		shapeService.getCircle().getName();
		//shapeService.getTriangle().getName();
		shapeService.getCircle().setName("Super Circle");
	}
}
