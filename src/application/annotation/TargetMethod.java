package application.annotation;

import java.lang.reflect.Method;

public class TargetMethod {
	private Method method;
	private Object subscriber;
	private String tag;
	private Mode mode;

	public TargetMethod(Method method, Object subscriber, String tag, Mode mode) {
		this.method = method;
		this.subscriber = subscriber;
		this.tag = tag;
		this.mode = mode;
	}

	public Method getMethod() {
		return method;
	}

	public Object getSubscriber() {
		return subscriber;
	}

	public String getTag() {
		return tag;
	}

	public Mode getMode() {
		return mode;
	}
}
