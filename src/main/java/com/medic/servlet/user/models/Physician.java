package com.medic.servlet.user.models;

import com.medic.servlet.db.Database;
import com.medic.servlet.user.dtos.Role;
import com.medic.servlet.utils.ApiResponse;
import com.medic.servlet.utils.TokenUtil;
import lombok.AllArgsConstructor;

import javax.naming.AuthenticationException;

@AllArgsConstructor
public class Physician extends User {
    @Override
    public ApiResponse<User> register() throws Exception {
        if (Database.findUser(this.getUserName()) != null) {
            throw new Exception("User already exists");
        }

        if (!(this.getPassword().length() >= 7 && this.getPassword().length() <= 8)) {
            throw new Exception("Password must be 7 char to 8 char");
        }

        this.encryptPwd();
        Database.addUser(this);
        return new ApiResponse<>("Signup successful", Database.findUser(this.getEmail()));
    }

    @Override
    public ApiResponse<String> login(String email, String password) throws AuthenticationException {
        User foundUser = Database.findUser(email);
        if (foundUser == null) throw new AuthenticationException("Invalid credentials");

        if (!foundUser.getRole().equals(Role.PHYSICIAN)) throw new AuthenticationException("Not Allowed to login with email");

        if (!password.equals(foundUser.decryptPwd())) throw new AuthenticationException("Invalid credentials");
        String token = TokenUtil.generateJWT(foundUser.getUserName(), foundUser.getRole(), foundUser.getId());
        return new ApiResponse<>("Login Successful", token);

    }
}
