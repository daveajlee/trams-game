package trams.extra;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestMain {

	public static void main ( String[] args ) {
		ApplicationContext testContext = new ClassPathXmlApplicationContext("trams/spring/context.xml");
		TestRun myTestRun = (TestRun) testContext.getBean("testRun");
		myTestRun.run();
	}
	
}
