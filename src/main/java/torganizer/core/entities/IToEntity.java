package torganizer.core.entities;

import java.util.UUID;

public interface IToEntity {
	/**
	 * Notification: The sender has changed. Reaction to a callback placed on
	 * the sender
	 *
	 * @param sender
	 */
	void callback(IToEntity sender);

	UUID getUid();
}
