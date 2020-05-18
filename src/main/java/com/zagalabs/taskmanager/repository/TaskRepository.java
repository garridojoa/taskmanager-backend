package com.zagalabs.taskmanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zagalabs.taskmanager.model.Task;

/**
 * Repository to handle Task records.
 * @author jgarrido
 */
public interface TaskRepository extends JpaRepository<Task, Long> {
	  Optional<Task> findById(Long taskId);

	  Optional<List<Task>> findByUserOwnerOrderByCompletionDateAsc(String userOwner);
}
