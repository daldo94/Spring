package learningtest.jdk;

import static org.junit.Assert.assertThat;

import java.lang.reflect.Proxy;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

public class HelloProxyTest {
	@Test
	public void simpleProxy() {
		Hello hello = new HelloTarget();
		assertThat(hello.sayHello("Toby"), is("Hello Toby"));
		assertThat(hello.sayHi("Toby"), is("Hi Toby"));
		assertThat(hello.sayThankyou("Toby"), is("Thank you Toby"));
	}
	
	@Test
	public void HelloUpperCase() {
		//Hello hello = new HelloUppercase(new HelloTarget());
		Hello hello = (Hello)Proxy.newProxyInstance(
				getClass().getClassLoader(), 
				new Class[] {Hello.class}, 
				new UpperCaseHandler(new HelloTarget()));
		
		assertThat(hello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(hello.sayHi("Toby"), is("HI TOBY"));
		assertThat(hello.sayThankyou("Toby"), is("THANK YOU TOBY"));
	}
}
