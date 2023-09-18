package com.workintech.twitter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationUser {
    private String name;
    private String username;
    private Date dateOfBirth;
    private String email;
    private String password;

}
