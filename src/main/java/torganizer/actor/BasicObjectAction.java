package torganizer.actor;

import torganizer.core.persistance.objectservice.GlobalObjectService;

public abstract class BasicObjectAction<T> extends BasicAction<T> {
	private final GlobalObjectService globalObjectService;

	BasicObjectAction(final GlobalObjectService globalObjectService) {
		this.globalObjectService = globalObjectService;
	}

	public GlobalObjectService getGlobalObjectService() {
		return globalObjectService;
	}
}
