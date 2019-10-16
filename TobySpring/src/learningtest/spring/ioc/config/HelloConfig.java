package learningtest.spring.ioc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import learningtest.spring.ioc.bean.Hello;
import learningtest.spring.ioc.bean.Printer;
import learningtest.spring.ioc.bean.StringPrinter;


//Singleton Example(printer())
@Configuration
public class HelloConfig {
	@Bean
	public Hello hello() {
		Hello hello = new Hello();
		hello.setName("Spring");
		hello.setPrinter(printer());
		return hello;
	}
	
	@Bean
	public Hello hello2() {
		Hello hello = new Hello();
		hello.setName("Spring2");
		hello.setPrinter(printer());
		return hello;
	}
	
	@Bean
	public Printer printer() {
		return new StringPrinter();
	}
}
