package org.example.repository;


import org.example.model.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findByPaymentId(Long paymentId);

    @Query(
            value = """
            SELECT n.message
            FROM notifications n
            JOIN payments p ON n.payment_id = p.id
            WHERE p.amount >= :amount
        """, nativeQuery = true)
    List<String> findMessagesByPaymentAmountGreaterThanEqual(@Param("amount") Double amount);
}
