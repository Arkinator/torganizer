package torganizer.web.data;

import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import torganizer.core.entities.Player;
import torganizer.core.persistance.orm.PlayerOrm;

@XmlRootElement
public class UserInformation {
	@XmlElement
	public String username;
	@XmlElement
	public UUID uid;

	public UserInformation(final PlayerOrm player) {
		this.username = player.getEntity().getName();
		this.uid = player.getEntity().getUuid();
	}

	public UserInformation() {
	}

	public UserInformation(final Player p) {
		this.username = p.getName();
		this.uid = p.getUid();
	}
}
