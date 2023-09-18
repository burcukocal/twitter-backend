package com.workintech.twitter.repository;

import com.workintech.twitter.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TweetRepository extends JpaRepository<Tweet, Integer> {
}
