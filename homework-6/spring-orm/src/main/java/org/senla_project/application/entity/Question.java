package org.senla_project.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "questions")
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Question {

    @Id @GeneratedValue UUID questionId;
    String header;
    String body;
    @ManyToOne(fetch = FetchType.LAZY) User author;
    int interesting;
    LocalDate createTime;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL) Set<Answer> answers;

}
