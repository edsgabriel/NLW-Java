package com.rocketseat.certification_nlw.modules.students.useCases;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rocketseat.certification_nlw.modules.students.dto.StudentCertificationAnswerDTO;
import com.rocketseat.certification_nlw.modules.students.dto.VerifyHasCertificationDTO;
import com.rocketseat.certification_nlw.modules.students.entities.AnswersCertificationsEntity;
import com.rocketseat.certification_nlw.modules.students.entities.CertificationStudentEntity;
import com.rocketseat.certification_nlw.modules.students.questions.entities.QuestionEntity;
import com.rocketseat.certification_nlw.modules.students.questions.repositories.QuestionRepository;
import com.rocketseat.certification_nlw.modules.students.repositories.CertificationStudentRepository;
import com.rocketseat.certification_nlw.modules.students.repositories.StudentRepository;

import lombok.var;


@Service
public class StudentCertificationAnswersUseCase {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CertificationStudentRepository certificationStudentRepository;

    @Autowired
    private VerifiyIfHasCertificationUseCase verifiyIfHasCertificationUseCase;

    public CertificationStudentEntity execute(StudentCertificationAnswerDTO dto) throws Exception {
        
        var hasCertification = this.verifiyIfHasCertificationUseCase.execute(new VerifyHasCertificationDTO(dto.getEmail(),dto.getTechnology()));

        if(hasCertification){
            throw new Exception("Você já tirou sua certificação!");
        }

        List<QuestionEntity> questionsEntity = questionRepository.findByTechnology(dto.getTechnology());
        List<AnswersCertificationsEntity> answersCertifications = new ArrayList<>();

        AtomicInteger correctAnswers = new AtomicInteger(0);

        dto.getQuestionsAnswers()
            .stream().forEach(questionAnswers -> {
                var question = questionsEntity.stream()
                    .filter(q -> q.getId().equals(questionAnswer.getQuestionID())).findFirst().get();
        
            var findCorrectAlternative = question.getAlternatives().stream()
                .filter(alternative -> alternative.isCorrect()).findFirst().get();
        
            if(findCorrectAlternative.getId().equals(questionAnswer.getAlternativeID())) {
                questionAnswer.setCorrect(isCorrect:true);
                correctAnswers.incrementAndGet();
            } else{
                questionAnswer.setCorrect(isCorrect:false);
            }

            var answersCertificationsEntity = AnswersCertificationsEntity.builder()
                .answerID(questionAnswer.getAlternativeID())
                .questionID(questionAnswer.getQuestionID())
                .isCorrect(questionAnswer.isCorrect()).build();

            answersCertifications.add();
        });


        var student = studentRepository.findByEmail(dto.getEmail());

        UUID studentID;
        if(student.isEmpty()){
            var studentCreated = StudentEntity.builder().email(dto.getEmail()).build();
            studentCreated = studentRepository.save(studentCreated);
            studentID = studentCreated.getId();
        }else{
            studentID() = student.get().getId();
        }
        
        List<AnswersCertificationsEntity> answersCertifications = new ArrayList<>();

        CertificationStudentEntity certificationStudentEntity = CertificationStudentEntity.builder()
            .technology(dto.getTechnology())
            .studentID(studentID)
            .grade(correctAnswers.get())
            .build();

        var certificationStudentCreated = certificationStudentRepository.save(certificationStudentEntity);

        answersCertifications.stream().forEach(answerCertification ->{
            answerCertification.setCertificationID(certificationStudentEntity.getId());
            answerCertification.setCertificationStudentEntity(certificationStudentEntity);
        });

        certificationStudentEntity.setAnswersCertificationsEntities(answerCertification);

        certificationStudentRepository.save(certificationStudentEntity);

        return certificationStudentCreated;
    }
}
