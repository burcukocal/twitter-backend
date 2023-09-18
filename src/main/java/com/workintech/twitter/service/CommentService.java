package com.workintech.twitter.service;

import com.workintech.twitter.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment findByComment(int commentId);
    void write(int tweetId, Comment comment);
    void delete(int commentId);
}
