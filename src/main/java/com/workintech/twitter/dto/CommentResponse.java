package com.workintech.twitter.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private int commentId;
    private int tweetId;
    private int userId;
    private String text;
    private String email;

}
