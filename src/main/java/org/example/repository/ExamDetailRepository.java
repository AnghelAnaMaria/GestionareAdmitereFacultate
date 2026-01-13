package org.example.repository;


import org.example.model.entities.ExamDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamDetailRepository extends JpaRepository<ExamDetail,Long> {

}
