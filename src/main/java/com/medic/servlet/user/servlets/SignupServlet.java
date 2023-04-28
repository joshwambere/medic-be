package com.medic.servlet.user.servlets;

import com.medic.servlet.user.dtos.Role;
import com.medic.servlet.user.models.Patient;
import com.medic.servlet.user.models.Pharmacist;
import com.medic.servlet.user.models.Physician;
import com.medic.servlet.user.models.User;
import com.medic.servlet.utils.ApiResponse;
import com.medic.servlet.utils.JsonUtil;
import com.medic.servlet.utils.ResponseEntity;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new JsonUtil().parseBodyJson(req, Physician.class);
        try {
            ApiResponse<User> result;

            Role userType = user.getRole();

            User newUser;

            switch (userType) {
                case PATIENT:
                    newUser = new Patient();
                    break;
                case PHYSICIAN:
                    newUser = new Physician();
                    break;
                default:
                    newUser = new Pharmacist();
                    break;
            }

            newUser.setName(user.getName());
            newUser.setAge(user.getAge());
            newUser.setUserName(user.getUserName());
            newUser.setRole(user.getRole());
            newUser.setPhone(user.getPhone());
            newUser.setEmail(user.getEmail());
            newUser.setPassword(user.getPassword());
            newUser.setGender(user.getGender());

            result = newUser.register();

            ResponseEntity.send(resp, result, HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseEntity.send(resp, new ApiResponse<>(e.getMessage(), null), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
