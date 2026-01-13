package org.example.model.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exam_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExamDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    private ExamType  examType;

    @OneToOne(optional = false)
    @JoinColumn(name = "exam_id", nullable = false)  // FK obligatoriu
    private Exam exam;
}
