package com.zagalabs.taskmanager.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zagalabs.taskmanager.exceptions.BadRequestException;
import com.zagalabs.taskmanager.exceptions.ResourceNotFoundException;
import com.zagalabs.taskmanager.model.StatusEnum;
import com.zagalabs.taskmanager.model.Task;
import com.zagalabs.taskmanager.repository.TaskRepository;

/**
 * Task Rest Api Controller
 * @author jgarrido
 */
@RestController
@RequestMapping("/task")
public class TaskController {
	@Autowired
	TaskRepository taskRepo;

	@GetMapping("/{userName}")
	public ResponseEntity<List<Task>> getUserTasks(@PathVariable String userName) {
		if (StringUtils.isEmpty(userName)) {
			throw new BadRequestException(String.format("Tasks retrieve failed. NULL or empty values are allowed: %s", userName));
		} else {
			return new ResponseEntity<List<Task>>(taskRepo.findByUserOwnerOrderByCompletionDateAsc(userName)
					.orElseThrow(() -> new ResourceNotFoundException(String.format("There is no tasks for user %s.", userName))), HttpStatus.OK);
		}
	}

	@GetMapping("/id/{taskId}")
	public ResponseEntity<Task> getTaskById(@PathVariable long taskId) {
		if (taskId == 0) {
			throw new BadRequestException("Task retrieve failed. Should provide a Task Id greater than 0.");
		} else {
			return new ResponseEntity<Task>(taskRepo.findById(taskId)
					.orElseThrow(() -> new ResourceNotFoundException(String.format("There is no task with id %d.", taskId))), HttpStatus.OK);
		}
	}

	@PutMapping("/add")
	public ResponseEntity<Task> addTask(@RequestBody Task task) {
		if (task.getDescription() == null) {
			throw new BadRequestException("Failed adding task: should have valid Description.");
		}
		if (task.getCompletionDate() == null) {
			throw new BadRequestException("Failed adding task: should have valid CompletionDate.");
		}
		if (task.getUserOwner() == null) {
			throw new BadRequestException("Failed adding task: should have valid UserOwner.");
		}

		@Valid Task addedTask = null;
		try {
			task.setStatus(StatusEnum.CREATED);
			addedTask = taskRepo.save(task);
		} catch (Exception e) {
			throw new BadRequestException("Task add failed: " + e.getMessage());
		}
		return new ResponseEntity<Task>(addedTask, HttpStatus.CREATED);
	}

	@PostMapping("/updateStatus/{taskId}")
	public ResponseEntity<?> updateTaskStatus(@Valid @PathVariable long taskId, @Valid @RequestBody String status) {
		return taskRepo.findById(taskId).map(task -> {
			try {
				StatusEnum statusEnum = StatusEnum.valueOf(status);
				task.setStatus(statusEnum);
				taskRepo.save(task);
			} catch (IllegalArgumentException iaExp) {
				throw new ResourceNotFoundException(String.format("There is no status named: %s.", status));
			}
			return ResponseEntity.ok().build();				
		}).orElseThrow(() -> new ResourceNotFoundException(String.format("There is no task for id: %d.", taskId)));
	}
}
