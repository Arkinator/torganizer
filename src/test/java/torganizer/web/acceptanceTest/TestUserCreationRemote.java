package torganizer.web.acceptanceTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import torganizer.utils.CryptoUtils;
import torganizer.web.IUserRestInterface;
import torganizer.web.data.UserInformation;

public class TestUserCreationRemote {
	private static final String username = "newUser";
	private static final String password = "password1234geheim";
	private static ResteasyClient client;
	private static ResteasyWebTarget target;
	private static IUserRestInterface token;

	@Test
	public void testGetSaltCommand() {
		final Long salt = Long.decode(getToken().getLoginSalt("fjdksa"));
		assertNotNull(salt);
	}

	@Test
	public void testUserCreation() {
		final UserInformation userInfo = new UserInformation();
		userInfo.username = username;
		final String passwordSalt = getToken().getLoginSalt(username);
		userInfo.passwordHash = CryptoUtils.getSha256Hash(passwordSalt + password);
		getToken().createUser(userInfo);
		final UserInformation loginInfo = getToken().doLogin(userInfo);
		assertEquals(username, loginInfo.username);
		assertEquals(username, getToken().getUserInfo().username);
		getToken().doLogout();
		assertNull(getToken().getUserInfo());
	}

	private IUserRestInterface getToken() {
		return token;
	}

	@BeforeClass
	public static void startup() {
		client = new ResteasyClientBuilder().build();
		target = client.target("http://localhost:8080/torganizer/user");
		token = target.proxy(IUserRestInterface.class);
	}

	@AfterClass
	public static void shutdown() {
		client.close();
	}

}
