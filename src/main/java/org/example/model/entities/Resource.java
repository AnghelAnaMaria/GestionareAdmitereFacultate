package org.example.model.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resources")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    private String description;

    @ManyToOne
    @JoinColumn(name = "exam_detail_id", nullable = false) //FK obligatoriu
    private ExamDetail examDetail;
}
