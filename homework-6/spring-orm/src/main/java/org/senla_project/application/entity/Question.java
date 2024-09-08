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

    @Column(name = "question_id") @Id @GeneratedValue UUID questionId;
    String header;
    String body;
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "author") @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST) User author;
    int interesting;
    @Column(name = "create_time") LocalDate createTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question", cascade = CascadeType.ALL) Set<Answer> answers;

}
