package com.workintech.twitter.service;

import com.workintech.twitter.entity.User;
import com.workintech.twitter.exceptions.TweetErrorException;
import com.workintech.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User is not valid"));
    }

    public User findUserById(int userId){
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            return foundUser.get();
        }
        throw new TweetErrorException("User does not exist with given id: ", HttpStatus.NOT_FOUND);

    }

    public User findUserByEmail(String email){
        Optional<User> foundUser = userRepository.findUserByEmail(email);
        if (foundUser.isPresent()) {
            return foundUser.get();
        }
        throw new TweetErrorException("User does not exist with given email: ", HttpStatus.NOT_FOUND);

    }

}
