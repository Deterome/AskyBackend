package org.senla_project.application.util;

import lombok.experimental.UtilityClass;
import org.senla_project.application.dto.*;
import org.senla_project.application.entity.*;

import java.time.LocalDate;
import java.util.Set;

@UtilityClass
public class TestData {

    public Role getRole() {
        return Role.builder()
                .roleName("user")
                .build();
    }
    public Role getUpdatedRole() {
        Role role = getRole();
        role.setRoleName("client");
        return role;
    }
    public RoleCreateDto getRoleCreateDto() {
        Role role = getRole();
        return RoleCreateDto.builder()
                .roleName(role.getRoleName())
                .build();
    }
    public RoleCreateDto getUpdatedRoleCreateDto() {
        Role updatedRole = getUpdatedRole();
        return RoleCreateDto.builder()
                .roleName(updatedRole.getRoleName())
                .build();
    }

    public User getUser() {
        return User.builder()
                .username("Alex")
                .password("1q2w3e")
                .roles(Set.of(getRole()))
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
    public UserCreateDto getUpdatedUserCreateDto() {
        User updatedUser = getUpdatedUser();
        return UserCreateDto.builder()
                .username(updatedUser.getUsername())
                .password(updatedUser.getPassword())
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
                .userName(profile.getUser().getUsername())
                .bio(profile.getBio())
                .avatarUrl(profile.getAvatarUrl())
                .firstname(profile.getFirstname())
                .surname(profile.getSurname())
                .birthday(profile.getBirthday().toString())
                .build();
    }
    public ProfileCreateDto getUpdatedProfileCreateDto() {
        Profile updatedProfile = getUpdatedProfile();
        return ProfileCreateDto.builder()
                .userName(updatedProfile.getUser().getUsername())
                .bio(updatedProfile.getBio())
                .avatarUrl(updatedProfile.getAvatarUrl())
                .firstname(updatedProfile.getFirstname())
                .surname(updatedProfile.getSurname())
                .birthday(updatedProfile.getBirthday().toString())
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
    public CollaborationCreateDto getCollaborationCreateDto() {
        Collaboration collab = getCollaboration();
        return CollaborationCreateDto.builder()
                .collabName(collab.getCollabName())
                .build();
    }
    public CollaborationCreateDto getUpdatedCollaborationCreateDto() {
        Collaboration updatedCollab = getUpdatedCollaboration();
        return CollaborationCreateDto.builder()
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
                .authorName(question.getAuthor().getUsername())
                .header(question.getHeader())
                .body(question.getBody())
                .build();
    }
    public QuestionCreateDto getUpdatedQuestionCreateDto() {
        Question updatedQuestion = getUpdatedQuestion();
        return QuestionCreateDto.builder()
                .authorName(updatedQuestion.getAuthor().getUsername())
                .header(updatedQuestion.getHeader())
                .body(updatedQuestion.getBody())
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
                .authorName(answer.getAuthor().getUsername())
                .body(answer.getBody())
                .questionId(answer.getQuestion().getQuestionId())
                .build();
    }
    public AnswerCreateDto getUpdatedAnswerCreateDto() {
        Answer updatedAnswer = getUpdatedAnswer();
        return AnswerCreateDto.builder()
                .authorName(updatedAnswer.getAuthor().getUsername())
                .body(updatedAnswer.getBody())
                .questionId(updatedAnswer.getQuestion().getQuestionId())
                .build();
    }

}
