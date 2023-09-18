package com.workintech.twitter.service;

import com.workintech.twitter.entity.Tweet;

import java.util.List;

public interface TweetService {
    List<Tweet> findAll();
    Tweet findById(int id);
    Tweet save(Tweet tweet);
    Tweet update(int id, Tweet tweet);
    Tweet delete(int id);
}
