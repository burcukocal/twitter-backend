package com.workintech.twitter.controller;

import com.workintech.twitter.dto.LikeResponse;
import com.workintech.twitter.entity.Like;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.service.LikeService;
import com.workintech.twitter.service.TweetService;
import com.workintech.twitter.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tweet/like")
public class LikeController {
    private TweetService tweetService;
    private UserService userService;
    private LikeService likeService;

    public LikeController(TweetService tweetService, UserService userService, LikeService likeService) {
        this.tweetService = tweetService;
        this.userService = userService;
        this.likeService = likeService;
    }

    @PostMapping("/{tweetId}")
    public ResponseEntity<LikeResponse> createLike(@PathVariable int tweetId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();


        Tweet tweet= tweetService.findById(tweetId);
        User user = userService.findUserByEmail(userEmail);

        LikeResponse likeResponse = new LikeResponse();
        likeResponse.setTweetId(tweet.getId());
        likeResponse.setUserId(user.getId());
        likeResponse.setEmail(user.getEmail());
        likeService.like(tweet.getId(),user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(likeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unlike(@PathVariable int id) {
        Like like = likeService.findByLike(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        if (like.getUser().getEmail().equals(userEmail)) {
            likeService.unlike(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
