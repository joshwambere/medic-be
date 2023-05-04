package com.medic.servlet.user.models;

import com.medic.servlet.db.Database;
import com.medic.servlet.user.dtos.Role;
import com.medic.servlet.utils.ApiResponse;
import com.medic.servlet.utils.TokenUtil;

import javax.naming.AuthenticationException;

public class Pharmacist extends User {
    @Override
    public ApiResponse<User> register() throws Exception {
        if (Database.getUsers().stream().filter(user -> user.getPhone().equals(this.getPhone())).findFirst().orElse(null) != null) {
            throw new Exception("User phone already exists");
        }
        if (!(this.getPassword().length() >= 9 && this.getPassword().length() <= 10)) {
            throw new Exception("Password must be 9 char to 10 char");
        }

        this.encryptPwd();
        Database.addUser(this);
        return new ApiResponse<>("Signup successful", Database.findUser(this.getEmail()));

    }

    @Override
    public ApiResponse<String> login(String phone, String password) throws AuthenticationException {
        User foundUser = Database.findUser(phone);
        if (foundUser == null) throw new AuthenticationException("Invalid credentials");

        //CHECK IF USER IS PHARMACIST
        if (!foundUser.getRole().equals(Role.PHARMACIST)) throw new AuthenticationException("Not Allowed to login with username");

        if (!password.equals(foundUser.decryptPwd())) throw new AuthenticationException("Invalid credentials");
        String token = TokenUtil.generateJWT(foundUser.getUserName(), foundUser.getRole(), foundUser.getId());
        return new ApiResponse<>("Login Successful", token);
    }
}
