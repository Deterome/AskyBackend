package org.senla_project.application.repository;

import org.senla_project.application.entity.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends PagingAndSortingRepository<Profile, UUID>, ListCrudRepository<Profile, UUID>, CustomizedProfileRepository {
}
