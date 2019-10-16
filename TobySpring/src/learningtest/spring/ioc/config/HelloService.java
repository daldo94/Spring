package learningtest.spring.ioc.config;

import org.springframework.context.annotation.Bean;

import learningtest.spring.ioc.bean.Hello;
import learningtest.spring.ioc.bean.Printer;
import learningtest.spring.ioc.bean.StringPrinter;

//Not Singleton
//For Singleton, using DI(own bean)
public class HelloService {
	private Printer printer;
	
	public void setPrinter(Printer printer) {
		this.printer = printer;
	}
	
	@Bean
	public Hello hello() {
		Hello hello = new Hello();
		hello.setName("Spring");
		hello.setPrinter(this.printer);
		return hello;
	}
	
	@Bean
	public Hello hello2() {
		Hello hello = new Hello();
		hello.setName("Spring2");
		hello.setPrinter(this.printer);
		return hello;
	}
	
	@Bean
	public Printer printer() {
		return new StringPrinter();
	}
}
