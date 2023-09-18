package com.workintech.twitter.service;

import com.workintech.twitter.entity.Like;

public interface LikeService {
    void like(int tweetId, int userId);
    void unlike(int likeId);
    Like findByLike(int likeId);
}
