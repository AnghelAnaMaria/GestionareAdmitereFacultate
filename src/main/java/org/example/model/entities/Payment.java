package org.example.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "payments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private LocalDate paymentDate;

    @OneToOne(optional = false) // fk
    @JoinColumn(name = "candidate_id", nullable = false, unique = true)
    private Candidate candidate;

    @OneToMany(mappedBy = "payment")
    private List<Notification> notifications;

}
