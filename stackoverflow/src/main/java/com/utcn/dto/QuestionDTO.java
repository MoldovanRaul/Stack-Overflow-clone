package com.utcn.dto;

import com.utcn.model.Answer;
import com.utcn.model.Question;
import com.utcn.model.Tag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionDTO {
    private Long id;
    private String title;
    private String text;
    private Date creationDateTime;
    private String picture;
    private UserDTO author;
    private List<TagDTO> tags;
    private List<AnswerDTO> answers;
    private int voteCount;

    public QuestionDTO() {
    }

    public QuestionDTO(Question question) {
        this.id = question.getId();
        this.title = question.getTitle();
        this.text = question.getText();
        this.creationDateTime = question.getCreationDateTime();
        this.picture = question.getPicture();
        this.author = new UserDTO((question.getAuthor()));
        this.tags = question.getTags().stream()
                .map(TagDTO::new)
                .collect(Collectors.toList());
        this.answers = question.getAnswers().stream()
                .map(AnswerDTO::new)
                .collect(Collectors.toList());
        this.voteCount = question.getVotes().stream()
                .mapToInt(vote -> vote.isUpvote() ? 1 : -1)
                .sum();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
