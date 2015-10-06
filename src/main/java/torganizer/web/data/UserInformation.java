package torganizer.web.data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserInformation {
	@XmlElement
	public String username;
	@XmlElement
	public byte[] passwordHash;
}
