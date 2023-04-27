package com.medic.servlet.user.servlets;

import com.medic.servlet.db.Database;
import com.medic.servlet.user.dtos.PhyisicianDto;
import com.medic.servlet.user.dtos.Role;
import com.medic.servlet.user.models.Consultation;
import com.medic.servlet.user.models.User;
import com.medic.servlet.utils.ApiResponse;
import com.medic.servlet.utils.JsonUtil;
import com.medic.servlet.utils.ResponseEntity;
import com.medic.servlet.utils.TokenUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/patients")
public class PatientServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = Database.GetUserByRole(Role.PATIENT);
        ResponseEntity.send(resp, new ApiResponse<>("Success", users), HttpServletResponse.SC_OK);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String token = req.getHeader("Authorization");
        PhyisicianDto phyisicianDto = new JsonUtil().parseBodyJson(req, PhyisicianDto.class);
        User user = TokenUtil.getUserFromToken(token);

        // CHECK IF USER IS LOGGED IN
        if (user == null) {
            ResponseEntity.send(res, new ApiResponse<>("Unauthorized", null), HttpServletResponse.SC_UNAUTHORIZED);
        }

        List<User> users = Database.getUsers();
        List<String> permissions = null;
        if (user != null) {
            permissions = user.getPermissions();
            permissions.add(phyisicianDto.id);

            //CREATE CONSULTATION OBJECT
            Consultation consultation = new Consultation();
            consultation.setPatientId(user.getId());
            consultation.setPhysicianId(phyisicianDto.id);
            Database.addConsultation(consultation);

            // ADD PERMISSIONS TO USER
            for (User u : users) {
                if (u.getId().equals(phyisicianDto.id)) {
                    u.setPermissions(permissions);
                    ResponseEntity.send(res, new ApiResponse<>("Success", u), HttpServletResponse.SC_OK);
                }
            }
        }


    }
}
