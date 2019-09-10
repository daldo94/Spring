package learningtest.jdk.proxy;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.util.PatternMatchUtils;

public class NameMatchClassMethodPointcut extends NameMatchMethodPointcut {

	public void setMappedClassName(String mappedClassName) {
		this.setClassFilter(new SimpleClassFilter(mappedClassName));
	}
	
	static class SimpleClassFilter implements ClassFilter{
		String mappedName;
		
		private SimpleClassFilter(String mappedName) {
			// TODO Auto-generated constructor stub
			this.mappedName = mappedName;
		}
		
		@Override
		public boolean matches(Class<?> clazz) {
			// TODO Auto-generated method stub
			return PatternMatchUtils.simpleMatch(mappedName, clazz.getSimpleName());
		}
		
	}
}
