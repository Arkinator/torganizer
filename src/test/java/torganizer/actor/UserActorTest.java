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
public class UserActorTest {
	@Autowired
	private GlobalObjectService globalObjectService;
	@Autowired
	private PlayerDao playerDao;

	@Test
	@Transactional
	public void addUserTest() {
		final int numUsers = new ListUsersAction(playerDao).execute().size();
		final UserInformation userInfo = new UserInformation();
		userInfo.username = RandomStringUtils.randomAlphanumeric(20);
		assertTrue(new AddUserCommand(userInfo, globalObjectService).execute());
		final int newUserNumber = new ListUsersAction(playerDao).execute().size();
		assertEquals(numUsers + 1, newUserNumber);
	}
}
