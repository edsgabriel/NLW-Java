package com.rocketseat.certification_nlw.modules.students.questions.controllers;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rocketseat.certification_nlw.modules.students.questions.dto.AlternativesResultDTO;
import com.rocketseat.certification_nlw.modules.students.questions.dto.QuestionResultDTO;
import com.rocketseat.certification_nlw.modules.students.questions.entities.AlternativesEntity;
import com.rocketseat.certification_nlw.modules.students.questions.entities.QuestionEntity;
import com.rocketseat.certification_nlw.modules.students.questions.repositories.QuestionRepository;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;
    
    @GetMapping("/technology/{technology}")
    public List<QuestionResultDTO> findByTechnology(@PathVariable String technology){
        System.out.println("TECH === " + technology);
        var result = this.questionRepository.findByTechnology(technology);
        
        var toMap = result.stream().map(question -> mapQuestionToDto(question))
            .collect(Collectors.toList());
        return toMap;
    }

    
    static QuestionResultDTO mapQuestionToDto(QuestionEntity question){
        var questionResultDTO = QuestionResultDTO.builder()
            .id(question.getId())
            .technology(question.getTechnology())
            .description(question.getDescription()).build();

        List<AlternativesResultDTO> alternativesResultDTOs = question.getAlternatives()
            .stream().map(alterntive -> mapAlternativeDTO(alterntive))
            .collect(Collectors.toList());

        questionResultDTO.setAlternatives(alternativesResultDTOs);
        return questionResultDTO;
    }

    static AlternativesResultDTO mapAlternativeDTO(AlternativesEntity alternativesResultDTO){
        return AlternativesResultDTO.builder()
            .id(alternativesResultDTO.getId())
            .description(alternativesResultDTO.getDescription()).build();
    }
}
