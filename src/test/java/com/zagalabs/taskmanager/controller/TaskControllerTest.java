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
import com.zagalabs.taskmanager.model.User;
import com.zagalabs.taskmanager.repository.UserRepository;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TaskmanagerApplication.class)
public class TaskControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webAppContext;
	@Autowired
	UserRepository userRepo;

	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
		User user = new User();
		user.setUserName("test");
		user.setPassword("test");
		userRepo.save(user);
	}

	@Test
	public void verifyValidAddTask() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/task/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\n" + 
        		"\"userOwner\":\"test\",\n" + 
        		"\"completionDate\":\"2020-05-13\",\n" + 
        		"\"description\":\"Create Sprint\"\n" +
        		"}")
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.userOwner").value("test"))
		.andExpect(jsonPath("$.completionDate").exists())
		.andExpect(jsonPath("$.description").exists())
		.andExpect(jsonPath("$.status").exists()).andDo(print());
	}

	@Test
	public void verifyMalformedAddTask() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/task/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\n" + 
        		"\"userOwner\":\"test\",\n" + 
        		"\"completionDate\":\"2020-05-13\",\n" + 
        		"\"description\":\"Create Sprint\",\n")
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(400)).andDo(print());
	}

	@Test
	public void verifyMissingFieldsAddTask() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/task/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\n" + 
        		 "\"descrip\":\"Create Sprint\"\n" + 
        		"}")
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(400)).andDo(print());
	}

	@Test
	public void verifyValidGetUserTasks() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/task/test")
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.[0].userOwner").value("test"))
		.andExpect(jsonPath("$.[0].completionDate").exists())
		.andExpect(jsonPath("$.[0].description").exists())
		.andExpect(jsonPath("$.[0].status").exists()).andDo(print());
	}
	
	@Test
	public void verifyValidUpdateTaskStatus() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/task/updateStatus/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content("PENDING")
		.accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
		.andDo(print());
	}
	
	@Test
	public void verifyTaskIdNotFoundUpdateTaskStatus() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/task/updateStatus/100000")
        .contentType(MediaType.APPLICATION_JSON)
        .content("PENDING")
		.accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(404))
		.andDo(print());
	}

	@Test
	public void verifyUserNotFoundGetUserTasks() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/task/root").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(404)).andDo(print());
	}
}
