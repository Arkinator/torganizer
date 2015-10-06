package torganizer.web;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class WebApplication extends Application {
	private final Set<Object> singletons = new HashSet<Object>();
	private final Set<Class<?>> empty = new HashSet<Class<?>>();

	public WebApplication() {
		this.singletons.add(new UserRestInterface());
	}

	@Override
	public Set<Class<?>> getClasses() {
		return this.empty;
	}

	@Override
	public Set<Object> getSingletons() {
		return this.singletons;
	}
}