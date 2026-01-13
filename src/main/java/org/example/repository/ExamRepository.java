package org.example.repository;


import jakarta.transaction.Transactional;
import org.example.model.entities.Exam;
import org.example.model.entities.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam,Long> {

    @Modifying
    @Transactional
    @Query("update Exam e set e.examDate= :examDate where e.id= :id")
    void updateExamDateById(Long id, LocalDate examDate);

    @Query("select e from Exam e order by e.examDate")
    List<Exam> findAllSortedByDate();


    List<Exam> findAllByExamDetail_ExamType(ExamType examType);


    @Query("SELECT e.name, COUNT(c) FROM Exam e LEFT JOIN e.candidates c GROUP BY e.name")
    List<Object[]> countCandidatesPerExam();

    @Query("SELECT e FROM Exam e JOIN e.examDetail d WHERE d.examType = :examType")
    List<Exam> findExamsByType(ExamType examType);

    @Query(value = """
    SELECT * FROM exams e
    WHERE e.id = (
        SELECT c.exam_id FROM candidates c
        GROUP BY c.exam_id
        ORDER BY COUNT(*) DESC
        LIMIT 1
    )
""", nativeQuery = true)
    Exam findExamWithMostCandidates();



    @Query("""
        SELECT e FROM Exam e
        WHERE (:examType IS NULL OR e.examDetail.examType = :examType)
        AND (:durationMinutes IS NULL OR e.examDetail.durationMinutes = :durationMinutes)
        AND (:examDate IS NULL OR e.examDate = :examDate)
    """)
    List<Exam> findByFilters(@Param("examType") ExamType examType, @Param("durationMinutes") Integer durationMinutes, @Param("examDate") LocalDate examDate);

    @Query("""
           SELECT e FROM Exam e
           JOIN e.examDetail ed
           JOIN Resource r ON r.examDetail = ed
           GROUP BY e ORDER BY COUNT(r) DESC
           """)
    Exam findExamWithMostResources();



}
