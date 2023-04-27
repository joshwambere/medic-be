package com.medic.servlet.user.servlets;

import com.medic.servlet.user.models.Patient;
import com.medic.servlet.user.models.Pharmacist;
import com.medic.servlet.user.models.User;
import com.medic.servlet.utils.ApiResponse;

import com.medic.servlet.utils.JsonUtil;
import com.medic.servlet.utils.ResponseEntity;

import javax.naming.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new JsonUtil().parseBodyJson(req, Patient.class);
        Patient patient = new Patient();
        Pharmacist pharmacist = new Pharmacist();

        try {
            String patientRegex = "^[a-zA-Z0-9]+$";
            String physicianRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            String pharmacistRegex = "^\\d{10}$";

            if (user.getUserName()!=null && Pattern.matches(physicianRegex, user.getUserName())) {
                ApiResponse<String> result = user.login(user.getUserName(), user.getPassword());
                ResponseEntity.send(resp, result, HttpServletResponse.SC_OK);
            } else if (user.getUserName()!=null && Pattern.matches(patientRegex, user.getUserName())) {
                ApiResponse<String> result = patient.login(user.getUserName(), user.getPassword());
                ResponseEntity.send(resp, result, HttpServletResponse.SC_OK);
            } else if (user.getUserName()!=null && Pattern.matches(pharmacistRegex, user.getUserName())) {
                ApiResponse<String> result = pharmacist.login(user.getUserName(), user.getPassword());
                ResponseEntity.send(resp, result, HttpServletResponse.SC_OK);
            } else {
                ResponseEntity.send(resp, new ApiResponse<>("Something wrong with this function", null), HttpServletResponse.SC_FORBIDDEN);
            }

        } catch (AuthenticationException e) {
            e.printStackTrace();
            ResponseEntity.send(resp, new ApiResponse<>(e.getMessage(), null), HttpServletResponse.SC_FORBIDDEN);
        }

    }
}
