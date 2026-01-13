package org.example.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "exams")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "exam_date", nullable = false)
    private LocalDate examDate;



    @OneToOne(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private ExamDetail examDetail;

    @OneToMany(mappedBy = "exam")
    private List<Candidate> candidates;

}
