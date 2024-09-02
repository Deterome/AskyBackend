package org.senla_project.application;

import org.senla_project.application.config.ApplicationConfig;
import org.senla_project.application.controller.*;
import org.senla_project.application.dto.*;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.UUID;

public class Application {

    static RoleController roleController;
    static UserController userController;
    static ProfileController profileController;
    static CollaborationController collaborationController;
    static CollaborationsJoiningController collaborationsJoiningController;
    static QuestionController questionController;
    static AnswerController answerController;

    public static void main(String[] args) {

        ConfigurableApplicationContext application = new AnnotationConfigApplicationContext(ApplicationConfig.class);

        roleController = application.getBean(RoleController.class);
        userController = application.getBean(UserController.class);
        profileController = application.getBean(ProfileController.class);
        collaborationController = application.getBean(CollaborationController.class);
        collaborationsJoiningController = application.getBean(CollaborationsJoiningController.class);
        questionController = application.getBean(QuestionController.class);
        answerController = application.getBean(AnswerController.class);

        try {
            addElements();
            outputElements();
//        deleteLastElements();
//        outputElements();
//        updateFirstElementsAndOutput();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        application.close();
    }

    private static void addElements() throws InterruptedException {
        System.out.println();
        System.out.println("Adding elements");
        addRoles(roleController);
        Thread.sleep(100);
        addUsers(userController);
        Thread.sleep(100);
        addProfiles(profileController);
        addCollaborations(collaborationController);
        Thread.sleep(100);
        addCollaborationsJoining(collaborationsJoiningController);
        addQuestions(questionController);
        Thread.sleep(100);
        addAnswers(answerController, questionController);
    }

    private static void outputElements() {
        System.out.println(roleController.getAllElements());
        System.out.println(userController.getAllElements());
        System.out.println(profileController.getAllElements());
        System.out.println(collaborationController.getAllElements());
        System.out.println(collaborationsJoiningController.getAllElements());
        System.out.println(questionController.getAllElements());
        System.out.println(answerController.getAllElements());
    }

    private static void deleteLastElements() {
        System.out.println();
        System.out.println("Deleting elements");
        roleController.deleteElement(roleController
                .findRoleId("god").orElseThrow(() -> new EntityNotFoundException("Role not found")));
        userController.deleteElement(userController
                .findUserId("Bob").orElseThrow(() -> new EntityNotFoundException("User not found")));
        profileController.deleteElement(profileController
                .findProfileId("Bob").orElseThrow(() -> new EntityNotFoundException("Profile not found")));
        collaborationController.deleteElement(collaborationController
                .findCollabId("Girls").orElseThrow(() -> new EntityNotFoundException("Collab not found")));
        collaborationsJoiningController.deleteElement(collaborationsJoiningController
                .findCollaborationJoinId("Nick", "Boys")
                .orElseThrow(() -> new EntityNotFoundException("Collab join not found")));
        answerController.deleteElement(answerController
                .findAnswerId("Alex",
                        questionController.findQuestionId("When?", "Do you know when?", "Nick")
                                .orElseThrow(() -> new EntityNotFoundException("Question not found")),
                        "When?")
                .orElseThrow(() -> new EntityNotFoundException("Answer not found")));
        questionController.deleteElement(questionController
                .findQuestionId("When?", "Do you know when?", "Nick")
                .orElseThrow(() -> new EntityNotFoundException("Question not found")));
    }

    private static void updateFirstElementsAndOutput() {
        System.out.println();
        System.out.println("Updating elements");

        UUID firstRoleId = roleController.findRoleId("admin")
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        System.out.println(roleController.getElementById(firstRoleId));
        roleController.updateElement(firstRoleId, RoleDto.builder().roleName("god").build());
        System.out.println(roleController.getElementById(firstRoleId));
        System.out.println();

        UUID firstUserId = userController.findUserId("Alex")
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        System.out.println(userController.getElementById(firstUserId));
        userController.updateElement(firstUserId, UserCreateDto.builder().roleName("user").nickname("Bob").password("123").build());
        System.out.println(userController.getElementById(firstUserId));
        System.out.println();

        UUID firstProfileId = profileController.findProfileId("Bob")
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));
        System.out.println(profileController.getElementById(firstProfileId));
        profileController.updateElement(firstProfileId, ProfileDto.builder().userName("Bob").bio("I'm Bob").firstname("Bob").surname("Lock").birthday("2000-03-01").avatarUrl("org").rating(8).build());
        System.out.println(profileController.getElementById(firstProfileId));
        System.out.println();

        UUID firstCollabId = collaborationController.findCollabId("Bros")
                .orElseThrow(() -> new EntityNotFoundException("Collab not found"));
        System.out.println(collaborationController.getElementById(firstCollabId));
        collaborationController.updateElement(firstCollabId, CollaborationDto.builder().collabName("Girls").createTime("2024-02-01").build());
        System.out.println(collaborationController.getElementById(firstCollabId));
        System.out.println();

        UUID firstCollabJoinId = collaborationsJoiningController.findCollaborationJoinId("Bob", "Boys")
                .orElseThrow(() -> new EntityNotFoundException("Collab join not found"));
        System.out.println(collaborationsJoiningController.getElementById(firstCollabJoinId));
        collaborationsJoiningController.updateElement(firstCollabJoinId, CollaborationsJoiningDto.builder().userName("Bob").collabName("Girls").joinDate("2024-05-03").build());
        System.out.println(collaborationsJoiningController.getElementById(firstCollabJoinId));
        System.out.println();

        UUID firstAnswerId = answerController.findAnswerId("Bob", questionController.findQuestionId("How?", "Do you know how?", "Bob").orElseThrow(() -> new EntityNotFoundException("Question not found")), "How?")
                .orElseThrow(() -> new EntityNotFoundException("Answer not found"));
        System.out.println(answerController.getElementById(firstAnswerId));
        answerController.updateElement(firstAnswerId, AnswerDto.builder().authorName("Bob").body("I know how!").questionId(questionController.findQuestionId("How?", "Do you know how?", "Bob").orElseThrow(() -> new EntityNotFoundException("Question not found"))).usefulness(0).createTime("2024-06-01").build());
        System.out.println(answerController.getElementById(firstAnswerId));
        System.out.println();

        UUID firstQuestionId = questionController.findQuestionId("How?", "Do you know how?", "Bob")
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));
        System.out.println(questionController.getElementById(firstQuestionId));
        questionController.updateElement(firstQuestionId, QuestionDto.builder().authorName("Bob").header("How?!").body("Do you know how?!").createTime("2024-06-01").interesting(1000).build());
        System.out.println(questionController.getElementById(firstQuestionId));
        System.out.println();
    }

    private static void addRoles(RoleController roleController) {
        new Thread(() -> roleController.addElement(RoleDto.builder().roleName("admin").build())).start();
        new Thread(() -> roleController.addElement(RoleDto.builder().roleName("user").build())).start();
        new Thread(() -> roleController.addElement(RoleDto.builder().roleName("god").build())).start();
    }

    private static void addUsers(UserController userController) {
        new Thread(() -> userController.addElement(UserCreateDto.builder().roleName("admin").nickname("Alex").password("123").build())).start();
        new Thread(() -> userController.addElement(UserCreateDto.builder().roleName("user").nickname("Nick").password("123").build())).start();
        new Thread(() -> userController.addElement(UserCreateDto.builder().roleName("user").nickname("Bob").password("123").build())).start();
    }

    private static void addProfiles(ProfileController profileController) {
        new Thread(() -> profileController.addElement(ProfileDto.builder().userName("Alex").bio("I'm Alex").firstname("Alex").surname("Hock").birthday("2000-01-01").avatarUrl("org").rating(10).build())).start();
        new Thread(() -> profileController.addElement(ProfileDto.builder().userName("Nick").bio("I'm Nick").firstname("Nick").surname("Mock").birthday("2000-02-01").avatarUrl("org").rating(9).build())).start();
        new Thread(() -> profileController.addElement(ProfileDto.builder().userName("Bob").bio("I'm Bob").firstname("Bob").surname("Lock").birthday("2000-03-01").avatarUrl("org").rating(8).build())).start();
    }

    private static void addCollaborations(CollaborationController collaborationController) {
        new Thread(() -> collaborationController.addElement(CollaborationDto.builder().collabName("Bros").createTime("2024-05-01").build())).start();
        new Thread(() -> collaborationController.addElement(CollaborationDto.builder().collabName("Boys").createTime("2024-01-01").build())).start();
        new Thread(() -> collaborationController.addElement(CollaborationDto.builder().collabName("Girls").createTime("2024-02-01").build())).start();
    }

    private static void addCollaborationsJoining(CollaborationsJoiningController collaborationsJoiningController) {
        new Thread(() -> collaborationsJoiningController.addElement(CollaborationsJoiningDto.builder().userName("Alex").collabName("Boys").joinDate("2024-05-01").build())).start();
        new Thread(() -> collaborationsJoiningController.addElement(CollaborationsJoiningDto.builder().userName("Bob").collabName("Boys").joinDate("2024-05-02").build())).start();
        new Thread(() -> collaborationsJoiningController.addElement(CollaborationsJoiningDto.builder().userName("Nick").collabName("Boys").joinDate("2024-05-03").build())).start();
    }

    private static void addQuestions(QuestionController questionController) {
        new Thread(() -> questionController.addElement(QuestionDto.builder().authorName("Alex").header("How?").body("Do you know how?").createTime("2024-06-01").interesting(1000).build())).start();
        new Thread(() -> questionController.addElement(QuestionDto.builder().authorName("Bob").header("Why?").body("Do you know why?").createTime("2024-07-01").interesting(1100).build())).start();
        new Thread(() -> questionController.addElement(QuestionDto.builder().authorName("Nick").header("When?").body("Do you know when?").createTime("2024-08-01").interesting(1200).build())).start();
    }

    private static void addAnswers(AnswerController answerController, QuestionController questionController) {
        new Thread(() -> answerController.addElement(AnswerDto.builder().authorName("Bob").body("How?").questionId(questionController.findQuestionId("How?", "Do you know how?", "Alex").orElseThrow(() -> new EntityNotFoundException("Question not found"))).usefulness(0).createTime("2024-06-01").build())).start();
        new Thread(() -> answerController.addElement(AnswerDto.builder().authorName("Nick").body("Why?").questionId(questionController.findQuestionId("Why?", "Do you know why?", "Bob").orElseThrow(() -> new EntityNotFoundException("Question not found"))).usefulness(0).createTime("2024-06-01").build())).start();
        new Thread(() -> answerController.addElement(AnswerDto.builder().authorName("Alex").body("When?").questionId(questionController.findQuestionId("When?", "Do you know when?", "Nick").orElseThrow(() -> new EntityNotFoundException("Question not found"))).usefulness(0).createTime("2024-06-01").build())).start();
    }

}