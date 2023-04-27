package com.medic.servlet.user.servlets;

import com.medic.servlet.db.Database;
import com.medic.servlet.utils.ApiResponse;
import com.medic.servlet.user.models.User;
import com.medic.servlet.utils.ResponseEntity;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = Database.findUser(req.getParameter("id"));
        if (user == null) {
            ResponseEntity.send(resp, new ApiResponse<>("User not found", null), HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        ResponseEntity.send(resp, new ApiResponse<>("Profile retrieved", user), HttpServletResponse.SC_OK);
    }
}
