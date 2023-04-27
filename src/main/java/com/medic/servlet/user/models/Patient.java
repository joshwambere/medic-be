package com.medic.servlet.user.models;

import com.medic.servlet.db.Database;
import com.medic.servlet.utils.ApiResponse;
import com.medic.servlet.utils.TokenUtil;

import javax.naming.AuthenticationException;


public class Patient extends User {
    @Override
    public ApiResponse<User> register() throws Exception {

        if (Database.findUser(this.getUserName()) != null) {
            throw new Exception("Username Taken");
        }


        if (!(this.getPassword().length() >= 4 && this.getPassword().length() <= 6)) {
            throw new Exception("Password must be 4 char to 6 char");
        }

        this.encryptPwd();
        Database.addUser(this);
        return new ApiResponse<>("Patient saved successfully", Database.findUser(this.getUserName()));
    }


    @Override
    public ApiResponse<String> login(String username, String password) throws AuthenticationException {
        User foundUser = Database.findUser(username);
        if (foundUser == null) throw new AuthenticationException("Invalid credentials");
        if (!password.equals(foundUser.decryptPwd())) throw new AuthenticationException("Invalid credentials");
        String token = TokenUtil.generateJWT(foundUser.getUserName(),foundUser.getRole(), foundUser.getId());
        return new ApiResponse<>("Login Successful", token);
    }
}
