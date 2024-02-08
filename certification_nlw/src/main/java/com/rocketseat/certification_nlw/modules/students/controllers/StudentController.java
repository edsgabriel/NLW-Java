package com.rocketseat.certification_nlw.modules.students.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rocketseat.certification_nlw.modules.students.dto.StudentCertificationAnswerDTO;
import com.rocketseat.certification_nlw.modules.students.dto.VerifyHasCertificationDTO;
import com.rocketseat.certification_nlw.modules.students.questions.dto.QuestionResultDTO;
import com.rocketseat.certification_nlw.modules.students.questions.entities.QuestionEntity;
import com.rocketseat.certification_nlw.modules.students.useCases.StudentCertificationAnswersUseCase;
import com.rocketseat.certification_nlw.modules.students.useCases.VerifiyIfHasCertificationUseCase;

@RestController
@RequestMapping ("/students")
public class StudentController {
    
    // Precisar usar o USECASE
    @Autowired
    private VerifiyIfHasCertificationUseCase VerifyIfHasCertificationUseCase;

    @Autowired
    private StudentCertificationAnswersUseCase studentCertificationAnswersUseCase;

    @PostMapping("/verifyIfHasCertification")
    public String verifyIfHasCertification(@RequestBody VerifyHasCertificationDTO verifyHasCertificationDTO){
        // email
        // technology
        var result = this.VerifyIfHasCertificationUseCase.execute(verifyHasCertificationDTO);
        if(result){
            return "Usuario ja fez a prova";
        }
        System.out.println(verifyHasCertificationDTO);;
        return "Usuario pode fazer a prova";
    }

    @PostMapping("/certification/answer")
    public ResponseEntity<Object> certificationAnswer(
            @RequestBody StudentCertificationAnswerDTO studentCertificationAnswerDTO) {
       try {
            var result = studentCertificationAnswersUseCase.execute(studentCertificationAnswerDTO);
            return ResponseEntity.ok().body(result);
        }catch(Exception e){
        return ResponseEntity.badRequest().body((e.getMessage()));
       }
        
    }
}
