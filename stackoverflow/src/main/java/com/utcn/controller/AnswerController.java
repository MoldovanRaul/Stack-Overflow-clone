package com.utcn.controller;

import com.utcn.dto.AnswerDTO;
import com.utcn.model.Answer;
import com.utcn.model.User;
import com.utcn.service.AnswerService;
import com.utcn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/answers")
public class AnswerController {

    @Autowired
    private final AnswerService answerService;
    private final UserService userService;

    @Autowired
    public AnswerController(AnswerService answerService, UserService userService) {
        this.answerService = answerService;
        this.userService = userService;
    }

    @GetMapping("/getAll")
    @ResponseBody
    public List<Answer> getAllAnswers() {
        return this.answerService.getAllAnswers();
    }

    @GetMapping("/question/{questionId}")
    public List<AnswerDTO> getAnswersByQuestionId(@PathVariable Long questionId) {
        List<Answer> answers = answerService.getAnswersByQuestionId(questionId);
        return answers.stream().map(AnswerDTO::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnswerDTO> getAnswerById(@PathVariable Long id) {
        Answer answer = answerService.getAnswerById(id);
        return ResponseEntity.ok(new AnswerDTO(answer));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnswerDTO createAnswer(@RequestBody Answer answer, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByName(username);
        answer.setAuthor(user);
        return new AnswerDTO(answerService.createAnswer(answer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnswerDTO> updateAnswer(@PathVariable Long id, @RequestBody Answer answer) {
        Answer updatedAnswer = answerService.updateAnswer(id, answer);
        return ResponseEntity.ok(new AnswerDTO(updatedAnswer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id) {
        answerService.deleteAnswer(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/upvote")
    public ResponseEntity<AnswerDTO> upvoteAnswer(@PathVariable Long id, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByName(username);

        Answer answer = answerService.upvoteAnswer(id, user);
        return ResponseEntity.ok(new AnswerDTO(answer));
    }

    @PostMapping("/{id}/downvote")
    public ResponseEntity<AnswerDTO> downvoteAnswer(@PathVariable Long id, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByName(username);

        Answer answer = answerService.downvoteAnswer(id, user);
        return ResponseEntity.ok(new AnswerDTO(answer));
    }
}
