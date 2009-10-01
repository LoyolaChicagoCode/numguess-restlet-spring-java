package edu.luc.cs.laufer.cs433.numguess.restlet;

import org.restlet.Component;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Simple demo application that can be run either as a standalone application
 * (http://localhost:3000/numguess-restlet/) or inside a servlet container
 * (http://localhost:8080/numguess-restlet/).
 */
public class Main {

	public static void main(final String... args) throws Exception {
		// load the Spring application context
		ApplicationContext springContext = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext-router.xml",
						"applicationContext-server.xml" });

		// obtain the Restlet component from the Spring context and start it
		((Component) springContext.getBean("top")).start();
	}

}