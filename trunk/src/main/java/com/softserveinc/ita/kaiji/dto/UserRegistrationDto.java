package com.softserveinc.ita.kaiji.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

/**
 * @author Vladyslav Shelest
 * @version 1.0
 * @since 22.04.14.
 */
@Component
public class UserRegistrationDto {
    @NotEmpty(message = "{NotEmpty.userregistrationdto.name}")
    private String name;
    @NotEmpty(message = "{NotEmpty.userregistrationdto.nickname}")
    private String nickname;
    @NotEmpty(message = "{NotEmpty.userregistrationdto.email}")
    @Email(message = "{Email.userregistrationdto.email}")
    private String email;
    @NotEmpty(message = "{NotEmpty.userregistrationdto.password}")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserRegistrationDto{" +
                "name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
