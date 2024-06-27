package com.utcn.service;

import com.utcn.model.Answer;
import com.utcn.model.Question;
import com.utcn.model.User;
import com.utcn.model.Vote;
import com.utcn.repository.VoteRepository;
import org.springframework.stereotype.Service;

@Service
public class VoteService {
    private final VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Vote upvoteItem(User user, Question question, Answer answer) {
        return castVote(user, question, answer, true);
    }

    public Vote downvoteItem(User user, Question question, Answer answer) {
        return castVote(user, question, answer, false);
    }

    private Vote castVote(User user, Question question, Answer answer, boolean isUpvote) {
        Vote existingVote = voteRepository.findByUserAndQuestionOrAnswer(user, question, answer);
        if (existingVote != null) {
            existingVote.setUpvote(isUpvote);
            return voteRepository.save(existingVote);
        } else {
            Vote newVote;
            if (question != null) {
                newVote = new Vote(user, question, isUpvote);
            } else {
                newVote = new Vote(user, answer, isUpvote);
            }
            return voteRepository.save(newVote);
        }
    }
}
