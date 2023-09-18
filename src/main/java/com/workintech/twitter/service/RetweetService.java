package com.workintech.twitter.service;

import com.workintech.twitter.entity.Retweet;

import java.util.List;

public interface RetweetService {
    void retweet(int tweetId, int userId);
}
