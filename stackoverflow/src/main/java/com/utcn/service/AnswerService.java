package com.utcn.service;

import com.utcn.model.Answer;
import com.utcn.model.User;
import com.utcn.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AnswerService {
    @Autowired
    private final AnswerRepository answerRepository;
    private VoteService voteService;

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }

    public Answer getAnswerById(Long id) {
        return this.answerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Answer not found with id " + id));
    }

    public Answer createAnswer(Answer answer){
        return answerRepository.save(answer);
    }

    public Answer updateAnswer(Long id, Answer updatedAnswer) {
        Answer existingAnswer = answerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Answer not found with id " + id));

        existingAnswer.setText(updatedAnswer.getText());
        existingAnswer.setPicture(updatedAnswer.getPicture());

        return answerRepository.save(existingAnswer);
    }


    public String deleteAnswer(Long id){
        try {
            answerRepository.deleteById(id);
            return "Answer deleted";
        }catch (Exception e){
            return "Failed to delete answer " + id;
        }
    }

    public Answer upvoteAnswer(Long answerId, User user) {
        Answer answer = getAnswerById(answerId);
        voteService.upvoteItem(user, null, answer);
        return answer;
    }

    public Answer downvoteAnswer(Long answerId, User user) {
        Answer answer = getAnswerById(answerId);
        voteService.downvoteItem(user, null, answer);
        return answer;
    }

    public List<Answer> getAnswersByQuestionId(Long questionId) {
        return answerRepository.findByQuestionId(questionId, Sort.by(Sort.Direction.DESC, "creatioDateTime"));
    }
}
