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
public class LoginControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private AuthorizationSessionService sessionService;

	@Before
	public void resetAccountService() {
		sessionService.reset();
	}
	
	@Test
	public void checkSuccessfulLogInOfExistingUser() throws Exception{
		mockMvc.perform(post("/logInService/logIn")
				.param("emailAddress", "amitays@msn.com")
				.param("password", "password")
				.header("User-Agent", "SomeUserAgent"))
		.andExpect(status().isOk());
	}
	
	
	@Test
	public void checkLogInOfExistingUserWithBadPassword() throws Exception{
		mockMvc.perform(post("/logInService/logIn")
				.param("emailAddress", "amitays@msn.com")
				.param("password", "badPassword")
				.header("User-Agent", "SomeUserAgent"))
		.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
	}
	
	@Test
	public void checkLogInOfUnKnownUser() throws Exception{
		mockMvc.perform(post("/logInService/logIn")
				.param("emailAddress", "badEmail@msn.com")
				.param("password", "badPassword")
				.header("User-Agent", "SomeUserAgent"))
		.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
	}
	
	@Test
	public void checkDoubleLogInOfExistingUser() throws Exception{
		mockMvc.perform(post("/logInService/logIn")
				.param("emailAddress", "amitays@msn.com")
				.param("password", "password")
				.header("User-Agent", "SomeUserAgent"))
		.andExpect(status().isOk());
		mockMvc.perform(post("/logInService/logIn")
				.param("emailAddress", "amitays@msn.com")
				.param("password", "password")
				.header("User-Agent", "SomeUserAgent"))
		.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
	}
	
	@Test
	public void checkLogOutOfLogedInUser() throws Exception{
		String sessionToken = mockMvc.perform(post("/logInService/logIn")
				.param("emailAddress", "amitays@msn.com")
				.param("password", "password")
				.header("User-Agent", "SomeUserAgent"))
		.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		mockMvc.perform(post("/logInService/logOut")
				.param("emailAddress", "amitays@msn.com")
				.param("sessionToken", sessionToken)
				.header("User-Agent", "SomeUserAgent"))
		.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string("true"));
	}
	
	@Test
	public void checkLogOutOfNonLogedInUser() throws Exception{
		String sessionToken = mockMvc.perform(post("/logInService/logIn")
				.param("emailAddress", "amitays@msn.com")
				.param("password", "password")
				.header("User-Agent", "SomeUserAgent"))
		.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		mockMvc.perform(post("/logInService/logOut")
				.param("emailAddress", "nonexistinguser@msn.com")
				.param("sessionToken", sessionToken)
				.header("User-Agent", "SomeUserAgent"))
		.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
	}
}