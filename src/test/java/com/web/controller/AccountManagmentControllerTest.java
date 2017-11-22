package com.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.web.service.AuthorizationSessionService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountManagmentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AuthorizationSessionService sessionService;

	@Before
	public void resetAccountService() {
		sessionService.reset();
	}
	
	@Test
	public void successfulAddNewUserAccount() throws Exception{
		mockMvc.perform(post("/accountManagment/addNewUserAccount")
				.param("emailAddress", "new@user.com")
				.param("password", "newPassword"))
		.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string("true"));
	}
	
	@Test
	public void checkAccountAllreadyExistWhenAddingNewUserAccount() throws Exception{
		mockMvc.perform(post("/accountManagment/addNewUserAccount")
				.param("emailAddress", "amitays@msn.com")
				.param("password", "newPassword"))
		.andExpect(status().is(HttpStatus.CONFLICT.value()));
	}
	
	@Test
	public void addNewUserAccountWithInvalidParameter() throws Exception{
		mockMvc.perform(post("/accountManagment/addNewUserAccount")
				.param("emailAddress", "amitays@msn.com")
				.param("password", ""))
		.andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()));
	}
	
	@Test
	public void getUserAccountWithInvalidToken() throws Exception{
		mockMvc.perform(post("/accountManagment/getUserAccount")
				.param("emailAddress", "amitays@msn.com")
				.param("sessionToken", "SomeToken"))
		.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
	}
	
	@Test
	public void getUserAccountWithLogedInNonAdminToken() throws Exception{
		String sessionToken = mockMvc.perform(post("/logInService/logIn")
				.param("emailAddress", "user@domain.com")
				.param("password", "void")
				.header("User-Agent", "SomeUserAgent"))
		.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		mockMvc.perform(post("/accountManagment/getUserAccount")
				.param("emailAddress", "amitays@msn.com")
				.param("sessionToken", sessionToken))
		.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
	}
	
	@Test
	public void getUserAccountWithLogedInAdminToken() throws Exception{
		String sessionToken = mockMvc.perform(post("/logInService/logIn")
				.param("emailAddress", "amitays@msn.com")
				.param("password", "password")
				.header("User-Agent", "SomeUserAgent"))
		.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		mockMvc.perform(post("/accountManagment/getUserAccount")
				.param("emailAddress", "user@domain.com")
				.param("sessionToken", sessionToken))
		.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content()
				.string("{\"emailAddress\":\"user@domain.com\",\"password\":\"void\",\"addressVerified\":true,\"name\":\"John Doe\",\"address\":\"LA, CA\",\"capabilitiesList\":[{\"level\":1,\"name\":\"User\"}]}"));
	}
	
	@Test
	public void _getUserAccountWithLogedInAdminToken() throws Exception{
		String sessionToken = mockMvc.perform(post("/logInService/logIn")
				.param("emailAddress", "user@domain.com")
				.param("password", "void")
				.header("User-Agent", "SomeUserAgent"))
		.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		mockMvc.perform(post("/accountManagment/getUserAccount")
				.param("emailAddress", "user@domain.com")
				.param("sessionToken", sessionToken))
		.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content()
				.string("{\"emailAddress\":\"user@domain.com\",\"password\":\"void\",\"addressVerified\":true,\"name\":\"John Doe\",\"address\":\"LA, CA\",\"capabilitiesList\":[{\"level\":1,\"name\":\"User\"}]}"));
	}

}