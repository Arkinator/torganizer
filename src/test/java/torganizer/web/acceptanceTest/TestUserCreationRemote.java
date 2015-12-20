package torganizer.web.acceptanceTest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.UUID;

import javax.ws.rs.core.Application;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;

import torganizer.actor.AddUserCommand;
import torganizer.core.entities.Player;
import torganizer.core.persistance.objectservice.GlobalObjectService;
import torganizer.web.data.UserInformation;

/**
 * @author Josh Long
 */
@RunWith(SpringJUnit4ClassRunner.class)
// @SpringApplicationConfiguration(classes = Application.class, locations = {
// "/spring-module.xml" })
@WebAppConfiguration
@ContextConfiguration(locations = { "/spring-module.xml" })
public class TestUserCreationRemote {
	private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;
	private final String username = RandomStringUtils.randomAlphanumeric(20);
	private HttpMessageConverter mappingJackson2HttpMessageConverter;
	private final Gson gson = new Gson();

	@Autowired
	private GlobalObjectService globalObjectService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	void setConverters(final HttpMessageConverter<?>[] converters) {
		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();
		Assert.assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
	}

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
		final UserInformation userInfo = new UserInformation();
		userInfo.username = username;
		new AddUserCommand(userInfo, globalObjectService).execute();
	}

	@Test
	public void userNotFound() throws Exception {
		mockMvc.perform(get("/user/" + UUID.randomUUID())).andExpect(status().isNotFound());
	}

	// @Test
	// public void readSingleBookmark() throws Exception {
	// mockMvc.perform(get("/" + userName + "/bookmarks/" +
	// this.bookmarkList.get(0).getId())).andExpect(status().isOk()).andExpect(content().contentType(contentType))
	// .andExpect(jsonPath("$.id",
	// is(this.bookmarkList.get(0).getId().intValue()))).andExpect(jsonPath("$.uri",
	// is("http://bookmark.com/1/" + userName)))
	// .andExpect(jsonPath("$.description", is("A description")));
	// }

	// @Test
	// public void readBookmarks() throws Exception {
	// mockMvc.perform(get("/" + userName +
	// "/bookmarks")).andExpect(status().isOk()).andExpect(content().contentType(contentType)).andExpect(jsonPath("$",
	// hasSize(2)))
	// .andExpect(jsonPath("$[0].id",
	// is(this.bookmarkList.get(0).getId().intValue()))).andExpect(jsonPath("$[0].uri",
	// is("http://bookmark.com/1/" + userName)))
	// .andExpect(jsonPath("$[0].description", is("A
	// description"))).andExpect(jsonPath("$[1].id",
	// is(this.bookmarkList.get(1).getId().intValue())))
	// .andExpect(jsonPath("$[1].uri", is("http://bookmark.com/2/" +
	// userName))).andExpect(jsonPath("$[1].description", is("A description")));
	// }

	@Test
	public void addUser() throws Exception {
		final String testUsername = RandomStringUtils.randomAlphabetic(20);
		final UserInformation userInfo = new UserInformation();
		userInfo.username = testUsername;
		final String playerJson = json(userInfo);
		this.mockMvc.perform(post("/user/").contentType(contentType).content(playerJson).accept(contentType))//
				.andExpect(content().contentType(contentType)) //
				.andExpect(status().isCreated())//
				.andExpect(jsonPath("$.username", is(testUsername)));

		final Player p = globalObjectService.getPlayerByName(testUsername);
		assertEquals(testUsername, p.getName());
		//
		// final List<UserInformation> infoList = (List<UserInformation>)
		// mockMvc.perform(get("/user/")).andExpect(status().isOk()).andReturn().getResponse().;
		// infoList.stream().filter(o -> username.equals(o.username));
		// mockMvc.perform(get("/user/" + infoList.get(0).uid))//
		// .andExpect(status().isOk()) //
		// .andExpect(jsonPath("$.username", is(username)));

	}

	//
	// protected String json(final Object o) throws IOException {
	// return gson.toJson(o);
	// }
	protected String json(final Object o) throws IOException {
		final MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
}