package org.senla_project.application.repository;

import org.senla_project.application.entity.Answer;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface AnswerRepository extends PagingAndSortingRepository<Answer, UUID>, ListCrudRepository<Answer, UUID>, CustomizedAnswerRepository {
}
