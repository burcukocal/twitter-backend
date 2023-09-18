package com.workintech.twitter.service;

import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.exceptions.TweetErrorException;
import com.workintech.twitter.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TweetServiceImpl implements TweetService{

    private TweetRepository tweetRepository;

    @Autowired
    public TweetServiceImpl(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @Override
    public List<Tweet> findAll() {
        return tweetRepository.findAll();
    }

    @Override
    public Tweet findById(int id) {
        Optional<Tweet> tweet= tweetRepository.findById(id);

        if(tweet.isPresent()) {
            return tweet.get();
        }
        throw new TweetErrorException("Tweet not found!", HttpStatus.NOT_FOUND);
    }

    @Override
    public Tweet save(Tweet tweet) {
        return tweetRepository.save(tweet);
    }

    @Override
    public Tweet update(int id, Tweet tweet) {
        Tweet updatedTweet = findById(id);
        updatedTweet.setPost(tweet.getPost());
        updatedTweet.setId(id);
        return tweetRepository.save(updatedTweet);

    }

    @Override
    public Tweet delete(int id) {
        Optional<Tweet> tweet = tweetRepository.findById(id);
        if(tweet.isPresent()){
            tweetRepository.delete(tweet.get());
            return tweet.get();
        }
        throw new TweetErrorException("Tweet with given id does not exits: " , HttpStatus.NOT_FOUND);
    }
}
