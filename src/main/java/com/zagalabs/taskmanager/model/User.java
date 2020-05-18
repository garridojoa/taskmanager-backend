package com.zagalabs.taskmanager.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * User model
 * @author jgarrido
 */
@Entity
@Table(name = "WEB_USER")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private String userName;
	@NotNull
	private String password;
	@NotNull
    @OneToMany(mappedBy = "userOwner", fetch = FetchType.EAGER)
    private Set<Task> tasks = new HashSet<Task>();
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}
}
