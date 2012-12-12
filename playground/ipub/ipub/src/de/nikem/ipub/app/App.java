package de.nikem.ipub.app;

import javax.servlet.ServletContext;

/**
 * Web Application
 * 
 * @author andreas
 * 
 */
public class App {

	public static SingletonFactory getSingletonFactory(ServletContext context) {
		SingletonFactory sf = (SingletonFactory) context.getAttribute(SingletonFactory.class.getName());
		if (sf == null) {
			sf = new SingletonFactory();
			context.setAttribute(SingletonFactory.class.getName(), sf);
		}
		return sf;
	}

	public static void refresh(ServletContext context)
	{
		context.setAttribute(SingletonFactory.class.getName(), null);
	}
}
