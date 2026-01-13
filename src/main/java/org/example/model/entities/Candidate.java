package org.example.model.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidates")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;


    //relatie bidirectionala
    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;
}
