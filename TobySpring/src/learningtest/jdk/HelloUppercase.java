package learningtest.jdk;

public class HelloUppercase implements Hello {
	Hello hello;
	public HelloUppercase(Hello hello) {
		// TODO Auto-generated constructor stub
		this.hello = hello;
	}
	
	@Override
	public String sayHello(String name) {
		// TODO Auto-generated method stub
		return hello.sayHello(name).toUpperCase();
	}

	@Override
	public String sayHi(String name) {
		// TODO Auto-generated method stub
		return hello.sayHi(name).toUpperCase();
	}

	@Override
	public String sayThankyou(String name) {
		// TODO Auto-generated method stub
		return hello.sayThankyou(name).toUpperCase();
	}
	
}
