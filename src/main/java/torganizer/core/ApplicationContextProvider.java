package torganizer.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import torganizer.core.persistance.objectservice.GlobalObjectService;

public class ApplicationContextProvider implements ApplicationContextAware {
	public static class SpringContextNotProperlyInitializedException extends RuntimeException {
		private static final long serialVersionUID = 3311172861184475241L;
	}

	private static ApplicationContext context;
	private static GlobalObjectService globalObjectService;

	public static ApplicationContext getApplicationContext() {
		if (context == null) {
			throw new SpringContextNotProperlyInitializedException();
		}
		return context;
	}

	@Override
	public void setApplicationContext(final ApplicationContext ac) throws BeansException {
		context = ac;
	}

	public static void setGlobalObjectService(final GlobalObjectService gos) {
		globalObjectService = gos;
	}

	public static GlobalObjectService getGlobalObjectService() {
		if (globalObjectService == null) {
			globalObjectService = getApplicationContext().getBean(GlobalObjectService.class);
		}
		return globalObjectService;
	}
}
