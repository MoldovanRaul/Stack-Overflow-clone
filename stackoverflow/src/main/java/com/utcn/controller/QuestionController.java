package com.utcn.controller;

import com.utcn.dto.QuestionDTO;
import com.utcn.model.Question;
import com.utcn.model.Tag;
import com.utcn.model.User;
import com.utcn.service.QuestionService;
import com.utcn.service.TagService;
import com.utcn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;
    private final TagService tagService;
    private final UserService userService;

    @Autowired
    public QuestionController(QuestionService questionService, TagService tagService, UserService userService) {
        this.questionService = questionService;
        this.tagService = tagService;
        this.userService = userService;
    }

    @GetMapping
    public List<QuestionDTO> getAllQuestions(
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "userId", required = false) Long userId,
            @RequestParam(name = "authorId", required = false) Long authorId
    ) {
        List<Question> questions = questionService.getFilteredQuestions(tag, search, userId, authorId);
        return questions.stream().map(QuestionDTO::new).toList();
    }



    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long id) {
        try {
            Question question = questionService.getQuestionById(id);
            return ResponseEntity.ok(new QuestionDTO(question));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionDTO createQuestion(@RequestBody Question question, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByName(username);
        question.setAuthor(user);

        List<Tag> savedTags = new ArrayList<>();
        for (Tag tag : question.getTags()) {
            Tag savedTag = tagService.createTagIfNotExists(tag.getName());
            savedTags.add(savedTag);
        }
        question.setTags(savedTags);

        return new QuestionDTO(questionService.createQuestion(question));
    }

    @PutMapping("{id}")
    @ResponseBody
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Long id, @RequestBody Question question) {
        Question updatedQuestion = questionService.updateQuestion(id, question);
        return ResponseEntity.ok(new QuestionDTO(updatedQuestion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filterByTag/{tagName}")
    public List<QuestionDTO> filterQuestionsByTag(@PathVariable String tagName) {
        return questionService.filterQuestionsByTag(tagName).stream().map(QuestionDTO::new).toList();
    }

    @GetMapping("/filterByText/{searchText}")
    public List<QuestionDTO> filterQuestionsByText(@PathVariable String searchText) {
        return questionService.filterQuestionsByText(searchText).stream().map(QuestionDTO::new).toList();
    }

    @PostMapping("/{id}/upvote")
    public ResponseEntity<QuestionDTO> upvoteQuestion(@PathVariable Long id, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByName(username);

        Question question = questionService.upvoteQuestion(id, user);
        return ResponseEntity.ok(new QuestionDTO(question));
    }

    @PostMapping("/{id}/downvote")
    public ResponseEntity<QuestionDTO> downvoteQuestion(@PathVariable Long id, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByName(username);

        Question question = questionService.downvoteQuestion(id, user);
        return ResponseEntity.ok(new QuestionDTO(question));
    }
}

