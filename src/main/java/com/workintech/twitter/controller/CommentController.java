package com.workintech.twitter.controller;

import com.workintech.twitter.dto.CommentRequest;
import com.workintech.twitter.dto.CommentResponse;
import com.workintech.twitter.entity.Comment;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.service.CommentService;
import com.workintech.twitter.service.TweetService;
import com.workintech.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tweet/reply")
public class CommentController {
    private CommentService commentService;
    private TweetService tweetService;
    private UserService userService;

    @Autowired
    public CommentController(CommentService commentService, TweetService tweetService, UserService userService) {
        this.commentService = commentService;
        this.tweetService = tweetService;
        this.userService = userService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<CommentResponse> createReply(@PathVariable int id, @RequestBody CommentRequest commentRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Tweet foundTweet= tweetService.findById(id);
        User user = userService.findUserByEmail(userEmail);
        Comment comment = new Comment();

        comment.setTweet(foundTweet);
        comment.setPost(commentRequest.getText());
        comment.setUser(user);
        commentService.write(foundTweet.getId(), comment);

        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setTweetId(foundTweet.getId());
        commentResponse.setUserId(user.getId());
        commentResponse.setText(commentRequest.getText());
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReply(@PathVariable int id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Comment comment = commentService.findByComment(id);
        if (comment.getUser().getEmail().equals(userEmail)) {
            commentService.delete(comment.getId());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}
