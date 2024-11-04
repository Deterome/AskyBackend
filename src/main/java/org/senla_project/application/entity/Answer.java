package org.senla_project.application.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "answers")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Answer {

    @Column(name = "answer_id")
    @Id
    @GeneratedValue
    private UUID answerId;

    private String body;

    @JoinColumn(name = "author")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User author;

    @JoinColumn(name = "question_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Question question;

    private int usefulness;

    @Column(name = "create_time")
    @CreatedDate
    private LocalDate createTime;

}
