package com.interswitch.user_management.model.response;

import com.interswitch.user_management.model.entity.User;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse implements Serializable {

     private static final long serialVersionUID = 1L;

    private final String token;
    private final Long expiresIn;

    private LoggedInUserResponse user;

    public LoginResponse(String token, Long expiresIn, User user){
        this.token = token;
        this.expiresIn = expiresIn;
        this.user = new LoggedInUserResponse(user);
    }
}
