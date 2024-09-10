package org.senla_project.application.util;

import lombok.experimental.UtilityClass;
import org.senla_project.application.entity.*;

import java.time.LocalDate;
import java.util.UUID;

@UtilityClass
public class TestData {

    public Role getRole() {
        return Role.builder()
                .roleName("user")
                .build();
    }
    public Role getUpdatedRole() {
        return Role.builder()
                .roleName("client")
                .build();
    }

    public User getUser() {
        return User.builder()
                .nickname("Alex")
                .password("1q2w3e")
                .build();
    }
    public User getUpdatedUser() {
        return User.builder()
                .nickname("Alex")
                .password("123456789")
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
        return Profile.builder()
                .user(getUser())
                .bio("Hello world!")
                .avatarUrl("./here/ava.png")
                .firstname("Alex")
                .surname("Clock")
                .rating(100)
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
    }

    public Collaboration getCollaboration() {
        return Collaboration.builder()
                .collabName("Boys")
                .createTime(LocalDate.of(2015, 1, 1))
                .build();
    }
    public Collaboration getUpdatedCollaboration() {
        return Collaboration.builder()
                .collabName("Bros")
                .createTime(LocalDate.of(2015, 1, 1))
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
        return CollaborationsJoining.builder()
                .collab(getCollaboration())
                .user(getUser())
                .joinDate(LocalDate.of(2020, 1, 1))
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
        return Question.builder()
                .header("What?")
                .body("What?")
                .author(getUser())
                .interesting(15)
                .createTime(LocalDate.of(2024, 3, 3))
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
        return Answer.builder()
                .body("I don't know what is it!")
                .usefulness(1)
                .author(getUser())
                .question(getQuestion())
                .createTime(LocalDate.of(2024, 3, 4))
                .build();
    }

}
