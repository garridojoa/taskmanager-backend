package com.zagalabs.taskmanager.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.zagalabs.taskmanager.TaskmanagerApplication;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TaskmanagerApplication.class)
public class UserControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webAppContext;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();

	}
	
	@Test
	public void verifyValidCreateUser() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/user/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\n" + 
        		"\"userName\":\"test\",\n" + 
        		"\"password\":\"test\"\n" + 
        		"}")
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.userName").exists())
		.andExpect(jsonPath("$.password").exists())
		.andDo(print());
	}

	@Test
	public void verifyMalformedCreateUser() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/user/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\n" + 
        		"\"userName\":\"test\",\n" + 
        		"\"password\":\"test\"\n")
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(400))		
		.andDo(print());
	}

	@Test
	public void verifyMissingFieldsCreateUser() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/task/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\n" + 
        		"\"user\":\"test\"\n" + 
        		"}")
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(400)).andDo(print());
	}

	@Test
	public void verifyValidGetUser() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user/test")
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.userName").value("test"))
		.andExpect(jsonPath("$.password").exists())
		.andExpect(jsonPath("$.tasks").exists()).andDo(print());
	}

	@Test
	public void verifyNotFoundGetUser() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user/root").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(404)).andDo(print());
	}
}
