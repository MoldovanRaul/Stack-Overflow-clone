package com.utcn.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "answers")
public class Answer {
    @Id
    @Column(name = "answer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "answer_text")
    private String text;
    @Column(name = "answer_date_time")
    @CreationTimestamp
    private Date creationDateTime;
    @Column(name = "answer_pic")
    private String picture;

    @ManyToOne
    private User author;

    @ManyToOne
    private Question question;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL)
    private Set<Vote> votes = new HashSet<>();

    public Answer(){

    }

    public Answer(Long id, String text, Date creationDateTime, String picture){
        this.id = id;
        this.text = text;
        this.creationDateTime = creationDateTime;
        this.picture = picture;
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

}
