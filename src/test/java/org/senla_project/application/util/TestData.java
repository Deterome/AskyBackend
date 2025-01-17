package org.senla_project.application.util;

import lombok.experimental.UtilityClass;
import org.senla_project.application.dto.answer.AnswerCreateDto;
import org.senla_project.application.dto.answer.AnswerUpdateDto;
import org.senla_project.application.dto.collabJoin.CollaborationsJoiningCreateDto;
import org.senla_project.application.dto.collabRole.CollabRoleCreateDto;
import org.senla_project.application.dto.collaboration.CollabCreateDto;
import org.senla_project.application.dto.collaboration.CollabUpdateDto;
import org.senla_project.application.dto.profile.ProfileCreateDto;
import org.senla_project.application.dto.profile.ProfileUpdateDto;
import org.senla_project.application.dto.question.QuestionCreateDto;
import org.senla_project.application.dto.question.QuestionUpdateDto;
import org.senla_project.application.dto.role.RoleCreateDto;
import org.senla_project.application.dto.role.RoleUpdateDto;
import org.senla_project.application.dto.user.UserCreateDto;
import org.senla_project.application.dto.user.UserUpdateDto;
import org.senla_project.application.dto.userCollaborationCollabRole.UserCollaborationCollabRoleCreateDto;
import org.senla_project.application.entity.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@UtilityClass
public class TestData {

    public final String AUTHORIZED_USER_NAME = "Alex";
    public final String USER_ROLE = "user";
    public final String ADMIN_ROLE = "admin";

    public Role getRole() {
        return Role.builder()
                .roleName(USER_ROLE)
                .build();
    }

    public Role getUpdatedRole() {
        Role role = getRole();
        role.setRoleName(ADMIN_ROLE);
        return role;
    }

    public RoleCreateDto getRoleCreateDto() {
        Role role = getRole();
        return RoleCreateDto.builder()
                .roleName(role.getRoleName())
                .build();
    }

    public RoleUpdateDto getRoleUpdateDto() {
        Role updatedRole = getUpdatedRole();
        return RoleUpdateDto.builder()
                .roleName(updatedRole.getRoleName())
                .build();
    }

    public User getAuthenticatedUser() {
        User user = getUser();
        user.setRoles(new HashSet<>(List.of(getRole())));
        return user;
    }

    public User getUpdatedAuthenticatedUser() {
        User user = getAuthenticatedUser();
        user.setPassword("123456789");
        return user;
    }

    public User getUser() {
        return User.builder()
                .username("Alex")
                .password("1q2w3e")
                .build();
    }

    public User getUpdatedUser() {
        User user = getUser();
        user.setPassword("123456789");
        return user;
    }

    public UserCreateDto getUserCreateDto() {
        User user = getUser();
        return UserCreateDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

    public UserUpdateDto getUserUpdateDto() {
        User updatedUser = getUpdatedAuthenticatedUser();
        return UserUpdateDto.builder()
                .username(updatedUser.getUsername())
                .password(updatedUser.getPassword())
                .roles(updatedUser.getRoles().stream().map(Role::getRoleName).toList())
                .build();
    }

    public Profile getProfile() {
        return Profile.builder()
                .user(getUser())
                .bio("Hello, my name is Alex!")
                .avatarUrl("./here/ava.png")
                .firstname("Alex")
                .surname("Clock")
                .rating(100)
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
    }

    public Profile getUpdatedProfile() {
        Profile profile = getProfile();
        profile.setBio("Hello world!");
        return profile;
    }

    public ProfileCreateDto getProfileCreateDto() {
        Profile profile = getProfile();
        return ProfileCreateDto.builder()
                .bio(profile.getBio())
                .avatarUrl(profile.getAvatarUrl())
                .firstname(profile.getFirstname())
                .surname(profile.getSurname())
                .birthday(profile.getBirthday().toString())
                .build();
    }

    public ProfileUpdateDto getProfileUpdateDto() {
        Profile updatedProfile = getUpdatedProfile();
        return ProfileUpdateDto.builder()
                .bio(updatedProfile.getBio())
                .firstname(updatedProfile.getFirstname())
                .surname(updatedProfile.getSurname())
                .birthday(updatedProfile.getBirthday().toString())
                .avatarUrl(updatedProfile.getAvatarUrl())
                .rating(updatedProfile.getRating())
                .build();
    }


    public Collaboration getCollaboration() {
        return Collaboration.builder()
                .collabName("Boys")
                .createTime(LocalDate.of(2015, 1, 1))
                .build();
    }

    public Collaboration getUpdatedCollaboration() {
        Collaboration collab = getCollaboration();
        collab.setCollabName("Bros");
        return collab;
    }

    public CollabCreateDto getCollaborationCreateDto() {
        Collaboration collab = getCollaboration();
        return CollabCreateDto.builder()
                .collabName(collab.getCollabName())
                .build();
    }

    public CollabUpdateDto getCollaborationUpdateDto() {
        Collaboration updatedCollab = getUpdatedCollaboration();
        return CollabUpdateDto.builder()
                .collabName(updatedCollab.getCollabName())
                .build();
    }

    public CollaborationsJoining getCollabJoining() {
        return CollaborationsJoining.builder()
                .collab(getCollaboration())
                .user(getUser())
                .joinDate(LocalDate.of(2016, 1, 1))
                .build();
    }

    public CollaborationsJoining getUpdatedCollabJoining() {
        CollaborationsJoining collabJoin = getCollabJoining();
        collabJoin.setJoinDate(LocalDate.of(2020, 1, 1));
        return collabJoin;
    }

    public CollaborationsJoiningCreateDto getCollabJoiningCreateDto() {
        CollaborationsJoining collabJoin = getCollabJoining();
        return CollaborationsJoiningCreateDto.builder()
                .collabName(collabJoin.getCollab().getCollabName())
                .userName(collabJoin.getUser().getUsername())
                .build();
    }

    public CollaborationsJoiningCreateDto getUpdatedCollabJoiningCreateDto() {
        CollaborationsJoining updatedCollabJoin = getUpdatedCollabJoining();
        return CollaborationsJoiningCreateDto.builder()
                .collabName(updatedCollabJoin.getCollab().getCollabName())
                .userName(updatedCollabJoin.getUser().getUsername())
                .build();
    }

    public Question getQuestion() {
        return Question.builder()
                .header("What?")
                .body("What is it?")
                .author(getUser())
                .interesting(10)
                .createTime(LocalDate.of(2024, 3, 3))
                .build();
    }

    public Question getUpdatedQuestion() {
        Question question = getQuestion();
        question.setBody("What?");
        return question;
    }

    public QuestionCreateDto getQuestionCreateDto() {
        Question question = getQuestion();
        return QuestionCreateDto.builder()
                .header(question.getHeader())
                .body(question.getBody())
                .build();
    }

    public QuestionUpdateDto getQuestionUpdateDto() {
        Question updatedQuestion = getUpdatedQuestion();
        return QuestionUpdateDto.builder()
                .header(updatedQuestion.getHeader())
                .body(updatedQuestion.getBody())
                .interesting(updatedQuestion.getInteresting())
                .build();
    }

    public Answer getAnswer() {
        return Answer.builder()
                .body("Now I know what is it!")
                .usefulness(20)
                .author(getUser())
                .question(getQuestion())
                .createTime(LocalDate.of(2024, 3, 4))
                .build();
    }

    public Answer getUpdatedAnswer() {
        Answer answer = getAnswer();
        answer.setBody("I don't know what is it!");
        return answer;
    }

    public AnswerCreateDto getAnswerCreateDto() {
        Answer answer = getAnswer();
        return AnswerCreateDto.builder()
                .body(answer.getBody())
                .questionId(answer.getQuestion().getQuestionId())
                .build();
    }

    public AnswerUpdateDto getAnswerUpdateDto() {
        Answer updatedAnswer = getUpdatedAnswer();
        return AnswerUpdateDto.builder()
                .answerId(String.valueOf(updatedAnswer.getAnswerId()))
                .body(updatedAnswer.getBody())
                .usefulness(updatedAnswer.getUsefulness())
                .build();
    }

    public CollabRole getCollabRole() {
        return CollabRole.builder()
                .collabRoleName("participant")
                .build();
    }

    public CollabRole getUpdatedCollabRole() {
        CollabRole collabRole = getCollabRole();
        collabRole.setCollabRoleName("god");
        return collabRole;
    }

    public CollabRoleCreateDto getCollabRoleCreateDto() {
        CollabRole collabRole = getCollabRole();
        return CollabRoleCreateDto.builder()
                .collabRoleName(collabRole.getCollabRoleName())
                .build();
    }

    public UserCollaborationCollabRole getUserCollaborationCollabRole() {
        return UserCollaborationCollabRole.builder()
                .user(getUser())
                .collab(getCollaboration())
                .collabRole(getCollabRole())
                .build();
    }

    public UserCollaborationCollabRole getUpdatedUserCollaborationCollabRole() {
        UserCollaborationCollabRole userCollaborationCollabRole = getUserCollaborationCollabRole();;
        userCollaborationCollabRole.setCollabRole(getUpdatedCollabRole());
        return userCollaborationCollabRole;
    }

    public UserCollaborationCollabRoleCreateDto getUserCollaborationCollabRoleCreateDto() {
        UserCollaborationCollabRole userCollaborationCollabRole = getUserCollaborationCollabRole();
        return UserCollaborationCollabRoleCreateDto.builder()
                .username(userCollaborationCollabRole.getUser().getUsername())
                .collabName(userCollaborationCollabRole.getCollab().getCollabName())
                .collabRoleName(userCollaborationCollabRole.getCollabRole().getCollabRoleName())
                .build();
    }

}
