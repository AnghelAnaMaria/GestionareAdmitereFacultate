package org.example.model.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private LocalDate notificationDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "payment_id", nullable = false) // fk
    private Payment payment;
}
