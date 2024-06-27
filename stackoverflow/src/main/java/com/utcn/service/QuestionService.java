package com.utcn.service;

import com.utcn.model.Question;
import com.utcn.model.Tag;
import com.utcn.model.User;
import com.utcn.repository.QuestionRepository;
import com.utcn.repository.TagRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final TagRepository tagRepository;
    private final TagService tagService;
    private final VoteService voteService;

    public QuestionService(QuestionRepository questionRepository, TagRepository tagRepository,
                           TagService tagService, VoteService voteService) {
        this.questionRepository = questionRepository;
        this.tagRepository = tagRepository;
        this.tagService = tagService;
        this.voteService = voteService;
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll(Sort.by(Sort.Direction.DESC, "creationDateTime"));
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findByIdWithAnswersAndVotes(id)
                .orElseThrow(() -> new NoSuchElementException("Question not found with id " + id));
    }

    public Question createQuestion(Question question) {
        for (Tag tag : question.getTags()) {
            Tag savedTag = tagService.createTagIfNotExists(tag.getName());
            tag.setId(savedTag.getId());
        }
        return questionRepository.save(question);
    }

    public Question updateQuestion(Long id, Question updatedQuestion) {
        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Question not found with id " + id));

        existingQuestion.setTitle(updatedQuestion.getTitle());
        existingQuestion.setText(updatedQuestion.getText());
        existingQuestion.setPicture(updatedQuestion.getPicture());

        List<Tag> updatedTags = new ArrayList<>();
        for (Tag tag : updatedQuestion.getTags()) {
            Tag existingTag = tagRepository.findByName(tag.getName());
            if (existingTag == null) {
                existingTag = tagRepository.save(new Tag(tag.getName()));
            }
            updatedTags.add(existingTag);
        }
        existingQuestion.setTags(updatedTags);

        return questionRepository.save(existingQuestion);
    }

    public String deleteQuestion(Long id){
        try{
            this.questionRepository.deleteById(id);
            return "Question deleted";
        }catch (Exception e){
            return "Failed to delete question " + id;
        }
    }

    public Question upvoteQuestion(Long questionId, User user) {
        Question question = getQuestionById(questionId);
        voteService.upvoteItem(user, question, null);
        return question;
    }

    public Question downvoteQuestion(Long questionId, User user) {
        Question question = getQuestionById(questionId);
        voteService.downvoteItem(user, question, null);
        return question;
    }

    public List<Question> getFilteredQuestions(String tag, String search, Long userId, Long authorId) {
        return questionRepository.findAll(Specification.where(
                (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (tag != null && !tag.isEmpty()) {
                        Join<Question, Tag> tagJoin = root.join("tags");
                        predicates.add(criteriaBuilder.equal(tagJoin.get("name"), tag));
                    }
                    if (search != null && !search.isEmpty()) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + search.toLowerCase() + "%"));
                    }
                    if (userId != null) {
                        predicates.add(criteriaBuilder.equal(root.get("author").get("id"), userId));
                    }
                    if (authorId != null) {
                        predicates.add(criteriaBuilder.equal(root.get("author").get("id"), authorId));
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                })
        );
    }

    public List<Question> filterQuestionsByTag(String tagName) {
        return questionRepository.findAll(Specification.where(
                (root, query, criteriaBuilder) -> {
                    query.distinct(true);
                    Join<Question, Tag> questionTagJoin = root.join("tags");
                    return criteriaBuilder.equal(questionTagJoin.get("name"), tagName);
                }));
    }

    public List<Question> filterQuestionsByText(String searchText) {
        return questionRepository.findByTitleContainingIgnoreCase(searchText);
    }

    public List<Question> filterQuestionsByUser(User user) {
        return questionRepository.findByAuthor(user);
    }

    public List<Question> filterQuestionsByAuthor(User author) {
        return questionRepository.findByAuthor(author);
    }
}
