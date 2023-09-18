package com.workintech.twitter.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponse {
    private int likeId;
    private int tweetId;
    private int userId;
    private String email;
}
