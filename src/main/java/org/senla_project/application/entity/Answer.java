package org.senla_project.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "answers")
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Answer {

    @Column(name = "answer_id")
    @Id
    @GeneratedValue
    UUID answerId;

    String body;

    @JoinColumn(name = "author")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    User author;

    @JoinColumn(name = "question_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Question question;

    int usefulness;

    @Column(name = "create_time")
    LocalDate createTime;

}


