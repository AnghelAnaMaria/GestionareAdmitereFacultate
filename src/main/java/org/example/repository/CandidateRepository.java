package org.example.repository;


import org.example.model.entities.Candidate;
import org.example.model.entities.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    List<Candidate> findByExamId(Long id);
    Candidate findByEmail(String email);

    //JPQL query
    @Query("SELECT c FROM Candidate c WHERE c.exam.id = :examId")
    List<Candidate> findCandidatesByExamId(@Param("examId") Long examId);

    @Query("SELECT c FROM Candidate c WHERE c.exam.name = :examName")
    List<Candidate> findCandidatesByExamName(@Param("examName") String examName);

    @Query("SELECT c.name FROM Candidate c WHERE c.exam.id = :examId")
    List<String> findCandidateNamesByExamId(@Param("examId") Long examId);

    @Query("SELECT c FROM Candidate c JOIN c.exam e JOIN e.examDetail d WHERE d.examType = :examType")
    List<Candidate> findCandidatesByExamType(ExamType examType);



}
