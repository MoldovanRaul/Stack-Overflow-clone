package com.utcn.repository;

import com.utcn.model.Question;
import com.utcn.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {
    @Query("SELECT q FROM Question q LEFT JOIN FETCH q.answers LEFT JOIN FETCH q.votes WHERE q.id = :id")
    Optional<Question> findByIdWithAnswersAndVotes(Long id);
    List<Question> findByTitleContainingIgnoreCase(String searchText);
    List<Question> findByAuthor(User user);
    List<Question> findAll(Specification<Question> spec);
}
