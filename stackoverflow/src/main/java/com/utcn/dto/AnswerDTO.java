package com.utcn.dto;

import com.utcn.model.Answer;

import java.util.Date;

public class AnswerDTO {
    private Long id;
    private String text;
    private Date creationDateTime;
    private String picture;
    private UserDTO author;
    private Long questionId;
    private int voteCount;

    public AnswerDTO() {

    }

    public AnswerDTO(Answer answer) {
        this.id = answer.getId();
        this.text = answer.getText();
        this.creationDateTime = answer.getCreationDateTime();
        this.picture = answer.getPicture();
        this.author = new UserDTO(answer.getAuthor());
        this.questionId = answer.getQuestion().getId(); // Get the associated question ID

        this.voteCount = answer.getVotes().stream()
                .mapToInt(vote -> vote.isUpvote() ? 1 : -1)
                .sum();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
