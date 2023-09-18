package com.workintech.twitter.service;

import com.workintech.twitter.entity.Retweet;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.repository.RetweetRepository;
import com.workintech.twitter.repository.TweetRepository;
import com.workintech.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetweetServiceImpl implements RetweetService{

    private RetweetRepository retweetRepository;
    private TweetRepository tweetRepository;
    private UserRepository userRepository;

    @Autowired
    public RetweetServiceImpl(RetweetRepository retweetRepository, TweetRepository tweetRepository, UserRepository userRepository) {
        this.retweetRepository = retweetRepository;
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }
    @Override
    public void retweet(int tweetId, int userId) {
        Tweet tweet = tweetRepository.findById(tweetId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (tweet != null && user != null) {
            Retweet retweet = new Retweet();
            retweet.setTweet(tweet);
            retweet.setUser(user);
            retweetRepository.save(retweet);
        }

    }
}
