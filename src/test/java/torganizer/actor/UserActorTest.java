package torganizer.actor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import torganizer.core.persistance.interfaces.PlayerDao;
import torganizer.core.persistance.objectservice.GlobalObjectService;
import torganizer.web.data.UserInformation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-module.xml" })
@Transactional
public class UserActorTest {
	@Autowired
	private GlobalObjectService globalObjectService;
	@Autowired
	private PlayerDao playerDao;

	@Test
	public void addUserTest() {
		final int numUsers = new ListUsersAction(playerDao).execute().size();
		final UserInformation userInfo = new UserInformation();
		userInfo.username = RandomStringUtils.randomAlphanumeric(20);
		assertTrue(new AddUserCommand(userInfo, globalObjectService).execute());
		final int newUserNumber = new ListUsersAction(playerDao).execute().size();
		assertEquals(numUsers + 1, newUserNumber);
	}

	@Test
	public void editUserTest() {
		UserInformation userInfo = new UserInformation();
		final String firstName = RandomStringUtils.randomAlphanumeric(20);
		final String secondName = RandomStringUtils.randomAlphanumeric(20);
		userInfo.username = firstName;
		assertTrue(new AddUserCommand(userInfo, globalObjectService).execute());

		userInfo = new GetUserInfoCommand(firstName, globalObjectService).execute();
		assertEquals(firstName, userInfo.username);

		userInfo.username = secondName;
		assertTrue(new EditUserCommand(userInfo.uid, userInfo, globalObjectService).execute());

		userInfo = new GetUserInfoCommand(userInfo.uid, globalObjectService).execute();
		assertEquals(secondName, userInfo.username);
	}
}
