package com.utcn.repository;

import com.utcn.model.Answer;
import com.utcn.model.Question;
import com.utcn.model.User;
import com.utcn.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    @Query("SELECT v FROM Vote v WHERE v.user = :user AND (v.question = :question OR v.answer = :answer)")
    Vote findByUserAndQuestionOrAnswer(User user, Question question, Answer answer);
}
