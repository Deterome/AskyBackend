package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.repository.CollaborationRepository;
import org.senla_project.application.repository.CollaborationsJoiningRepository;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.dto.CollaborationsJoiningDto;
import org.senla_project.application.entity.Entity;
import org.senla_project.application.mapper.CollaborationsJoiningMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CollaborationsJoiningService implements ServiceInterface<CollaborationsJoiningDto, CollaborationsJoiningDto> {

    @Autowired
    private CollaborationsJoiningRepository collaborationsJoiningRepository;
    @Autowired
    private CollaborationRepository collaborationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CollaborationsJoiningMapper collaborationsJoiningMapper;

    @Override
    public void execute() {}

    @Transactional
    @Override
    public void addElement(@NonNull CollaborationsJoiningDto element) {
        collaborationsJoiningRepository.create(collaborationsJoiningMapper.toEntity(element));
    }

    @Transactional
    @Override
    public void updateElement(@NonNull UUID id, @NonNull CollaborationsJoiningDto updatedElement) {
        collaborationsJoiningRepository.update(id, collaborationsJoiningMapper.toEntity(updatedElement));
    }

    @Transactional
    @Override
    public void deleteElement(@NonNull UUID id) {
        collaborationsJoiningRepository.deleteById(id);
    }

    @Transactional
    @Override
    public List<CollaborationsJoiningDto> getAllElements() {
        return collaborationsJoiningMapper.toDtoList(collaborationsJoiningRepository.findAll());
    }

    @Transactional
    @Override
    public Optional<CollaborationsJoiningDto> getElementById(@NonNull UUID id) {
        return collaborationsJoiningRepository.findById(id)
                .map(collaborationsJoiningMapper::toDto);
    }

    @Transactional
    public Optional<UUID> findCollaborationJoinId(String username, String collaboration) {
        return collaborationsJoiningRepository.findCollaborationJoin(username, collaboration).map(Entity::getId);
    }

}
