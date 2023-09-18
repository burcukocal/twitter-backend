package com.workintech.twitter.controller;

import com.workintech.twitter.dto.RetweetResponse;
import com.workintech.twitter.entity.Retweet;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.service.RetweetService;
import com.workintech.twitter.service.TweetService;
import com.workintech.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tweet/retweet")
public class RetweetController {
    private RetweetService retweetService;
    private UserService userService;
    private TweetService tweetService;

    @Autowired
    public RetweetController(RetweetService retweetService, UserService userService, TweetService tweetService) {
        this.retweetService = retweetService;
        this.userService = userService;
        this.tweetService = tweetService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<RetweetResponse> createRetweet(@PathVariable int id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Tweet tweet = tweetService.findById(id);
        User user = userService.findUserByEmail(userEmail);
        RetweetResponse retweetResponse = new RetweetResponse();
        retweetResponse.setTweetId(tweet.getId());
        retweetResponse.setUserId(user.getId());
        retweetResponse.setEmail(user.getEmail());
        Retweet retweet = new Retweet();
        retweet.setTweet(tweet);
        retweet.setUser(user);
        retweetService.retweet(tweet.getId(), user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(retweetResponse);
    }
}

