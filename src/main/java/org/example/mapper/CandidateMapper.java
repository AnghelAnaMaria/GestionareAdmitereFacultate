package org.example.mapper;


import org.example.model.dto.CandidateResponse;
import org.example.model.dto.CreateCandidateRequest;
import org.example.model.entities.Candidate;
import org.springframework.stereotype.Component;

@Component
public class CandidateMapper {

    public CandidateResponse fromEntityToDto(Candidate candidate){
        CandidateResponse candidateResponse=new CandidateResponse();
        candidateResponse.setId(candidate.getId());
        candidateResponse.setName(candidate.getName());
        candidateResponse.setEmail(candidate.getEmail());

        if (candidate.getExam() != null) {
            candidateResponse.setExamName(candidate.getExam().getName());
        }
        return candidateResponse;
    }

    public Candidate fromDtoToEntity(CreateCandidateRequest request){
        Candidate candidate=new Candidate();
        candidate.setName(request.name());
        candidate.setEmail(request.email());
        //Exam punem in service
        return candidate;
    }

}
