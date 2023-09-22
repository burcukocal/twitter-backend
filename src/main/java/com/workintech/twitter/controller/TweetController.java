package com.workintech.twitter.controller;

import com.workintech.twitter.dto.*;
import com.workintech.twitter.entity.*;
import com.workintech.twitter.service.TweetService;
import com.workintech.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tweet")
public class TweetController {
    private TweetService tweetService;
    private UserService userService;

    @Autowired
    public TweetController(TweetService tweetService, UserService userService) {
        this.tweetService = tweetService;
        this.userService = userService;
    }

    private TweetResponse createTweetResponse(Tweet tweet){
        TweetResponse tweetResponse = new TweetResponse();
        tweetResponse.setTweetId(tweet.getId());
        tweetResponse.setText(tweet.getPost());
        tweetResponse.setUserId(tweet.getUser().getId());
        tweetResponse.setEmail(tweet.getUser().getEmail());
        return tweetResponse;
    }
    private LikeResponse createLikeResponse(Tweet tweet, Like like){
        LikeResponse likeResponse = new LikeResponse();
        likeResponse.setTweetId(tweet.getId());
        likeResponse.setUserId(like.getUser().getId());
        likeResponse.setEmail(like.getUser().getEmail());
        likeResponse.setLikeId(like.getId());
        return likeResponse;
    }

    private RetweetResponse createRetweetResponse(Retweet retweet) {
        RetweetResponse retweetResponse = new RetweetResponse();
        retweetResponse.setTweetId(retweet.getTweet().getId());
        retweetResponse.setUserId(retweet.getUser().getId());
        retweetResponse.setEmail(retweet.getUser().getEmail());
        return retweetResponse;
    }

    private CommentResponse createCommentResponse(Tweet tweet, Comment comment) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setTweetId(tweet.getId());
        commentResponse.setUserId(comment.getUser().getId());
        commentResponse.setText(comment.getText());
        commentResponse.setEmail(comment.getUser().getEmail());
        commentResponse.setCommentId(comment.getId());
        return commentResponse;
    }
    @GetMapping("/")
    public List<TweetResponse> getAllTweets() {
        List<Tweet> tweets = tweetService.findAll();
        List<TweetResponse> tweetResponses = new ArrayList<>();

        for (Tweet tweet : tweets) {
            TweetResponse tweetResponse = createTweetResponse(tweet);

            List<LikeResponse> likeResponses = new ArrayList<>();
            for (Like like : tweet.getLikes()) {
               LikeResponse likeResponse = createLikeResponse(tweet, like);
               likeResponses.add(likeResponse);
            }
            tweetResponse.setLikes(likeResponses);

            List<RetweetResponse> retweetResponses = new ArrayList<>();
            for (Retweet retweet : tweet.getRetweets()) {
                RetweetResponse retweetResponse = createRetweetResponse(retweet);
                retweetResponses.add(retweetResponse);
            }
            tweetResponse.setRetweets(retweetResponses);

            List<CommentResponse> commentResponses = new ArrayList<>();
            for (Comment comment : tweet.getComments()) {
                CommentResponse commentResponse = createCommentResponse(tweet, comment);
                commentResponses.add(commentResponse);
            }
            tweetResponse.setComments(commentResponses);
            tweetResponses.add(tweetResponse);
        }
        return tweetResponses;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TweetResponse> getTweetById(@PathVariable int id) {
        Tweet tweet = tweetService.findById(id);
        TweetResponse tweetResponse = createTweetResponse(tweet);

        List<LikeResponse> likeResponses = new ArrayList<>();
        for (Like like : tweet.getLikes()) {
            LikeResponse likeResponse = createLikeResponse(tweet, like);
            likeResponses.add(likeResponse);
        }
        tweetResponse.setLikes(likeResponses);

        List<RetweetResponse> retweetResponses = new ArrayList<>();
        for (Retweet retweet : tweet.getRetweets()) {
            RetweetResponse retweetResponse = createRetweetResponse(retweet);
            retweetResponses.add(retweetResponse);
        }
        tweetResponse.setRetweets(retweetResponses);

        List<CommentResponse> commentResponses = new ArrayList<>();
        for (Comment comment : tweet.getComments()) {
            CommentResponse commentResponse = createCommentResponse(tweet, comment);
            commentResponses.add(commentResponse);
        }
        tweetResponse.setComments(commentResponses);
        return ResponseEntity.status(HttpStatus.OK).body(tweetResponse);
    }

    @PostMapping("/")
    public ResponseEntity<TweetResponse> createTweet(@RequestBody TweetRequest tweetRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userService.findUserByEmail(userEmail);

        Tweet newTweet = new Tweet();
        newTweet.setUser(user);
        newTweet.setPost(tweetRequest.getText());

        Tweet createdTweet = tweetService.save(newTweet);

        TweetResponse tweetResponse = createTweetResponse(createdTweet);

        return ResponseEntity.status(HttpStatus.CREATED).body(tweetResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<TweetResponse> updateTweet(@PathVariable int id, @RequestBody TweetRequest updatedTweet) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Tweet tweet = tweetService.findById(id);

        if (tweet.getUser().getEmail().equals(userEmail)) {
            tweet.setPost(updatedTweet.getText());

            Tweet updated = tweetService.update(tweet.getId(), tweet);

            TweetResponse tweetResponse = createTweetResponse(updated);

            List<LikeResponse> likeResponses = new ArrayList<>();
            for (Like like : tweet.getLikes()) {
                LikeResponse likeResponse = createLikeResponse(updated, like);
                likeResponses.add(likeResponse);
            }
            tweetResponse.setLikes(likeResponses);

            List<RetweetResponse> retweetResponses = new ArrayList<>();
            for (Retweet retweet : tweet.getRetweets()) {
                RetweetResponse retweetResponse = createRetweetResponse(retweet);
                retweetResponses.add(retweetResponse);
            }
            tweetResponse.setRetweets(retweetResponses);

            List<CommentResponse> commentResponses = new ArrayList<>();
            for (Comment comment : tweet.getComments()) {
                CommentResponse commentResponse = createCommentResponse(tweet, comment);
                commentResponses.add(commentResponse);
            }
            tweetResponse.setComments(commentResponses);

            return ResponseEntity.status(HttpStatus.CREATED).body(tweetResponse);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Tweet foundTweet = tweetService.findById(id);
        if (foundTweet.getUser().getEmail().equals(userEmail)) {
            tweetService.delete(foundTweet.getId());

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}

