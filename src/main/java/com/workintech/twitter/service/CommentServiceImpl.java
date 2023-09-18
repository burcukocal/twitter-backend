package com.workintech.twitter.service;

import com.workintech.twitter.entity.Comment;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.exceptions.TweetErrorException;
import com.workintech.twitter.repository.CommentRepository;
import com.workintech.twitter.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService{
    private CommentRepository commentRepository;
    private TweetRepository tweetRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, TweetRepository tweetRepository) {
        this.commentRepository = commentRepository;
        this.tweetRepository = tweetRepository;
    }

    @Override
    public Comment findByComment(int commentId) {
        Optional<Comment> foundReply = commentRepository.findById(commentId);

        if (foundReply.isPresent()) {
            return foundReply.get();
        }
        throw new TweetErrorException("Comment with given id is not valid: " , HttpStatus.NOT_FOUND);
    }

    @Override
    public void write(int tweetId, Comment comment) {
        Optional<Tweet> tweet = tweetRepository.findById(tweetId);
        if (tweet.isPresent()) {
            comment.setTweet(tweet.get());
            commentRepository.save(comment);
        }
        throw new TweetErrorException("There are no tweets matching the specified tweet ID", HttpStatus.BAD_REQUEST);
    }

    @Override
    public void delete(int commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            commentRepository.deleteById(commentId);
        }
    }
}
