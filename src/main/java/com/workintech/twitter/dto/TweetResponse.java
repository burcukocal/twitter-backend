package com.workintech.twitter.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetResponse {
    private int tweetId;
    private int userId;
    private String text;
    private String email;
    private List<LikeResponse> likes;
    private List<RetweetResponse> retweets;
    private List<CommentResponse> comments;
}
