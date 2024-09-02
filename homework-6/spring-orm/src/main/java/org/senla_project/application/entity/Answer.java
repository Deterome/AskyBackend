package org.senla_project.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "answers")
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Answer {

    @Id @GeneratedValue UUID answerId;
    String body;
    @ManyToOne(fetch = FetchType.LAZY) User author;
    @Column(name = "question_id") @ManyToOne(fetch = FetchType.LAZY) Question question;
    int usefulness;
    LocalDate createTime;

}


