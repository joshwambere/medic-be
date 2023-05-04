package com.medic.servlet.user.servlets;

import com.medic.servlet.db.Database;
import com.medic.servlet.user.dtos.PatientDto;
import com.medic.servlet.user.dtos.PhyisicianDto;
import com.medic.servlet.user.dtos.Role;
import com.medic.servlet.user.models.Patient;
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
import java.util.List;


@WebServlet("/patients")
public class PatientServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = Database.GetUserByRole(Role.PATIENT);
        ResponseEntity.send(resp, new ApiResponse<>("Success", users), HttpServletResponse.SC_OK);
    }

    // GRANT PERMISSION TO PHYSICIAN
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String token = req.getHeader("Authorization");
        PhyisicianDto phyisicianDto = new JsonUtil().parseBodyJson(req, PhyisicianDto.class);

        Patient patient = new Patient();
        boolean granted = patient.grantPermission(Role.PHYSICIAN,phyisicianDto.id,token, phyisicianDto );

       if(granted) {
           ResponseEntity.send(res, new ApiResponse<>("Success", null), HttpServletResponse.SC_OK);
       }else {
           ResponseEntity.send(res, new ApiResponse<>("Something went wrong", null), HttpServletResponse.SC_FORBIDDEN);
       }
    }


    // GRANT PERMISSIONS TO PHARMACIST
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        Patient patient = new Patient();
        String token = req.getHeader("Authorization");
        PatientDto patientDto = new JsonUtil().parseBodyJson(req, PatientDto.class);
        boolean granted = patient.grantPermission(Role.PHARMACIST,patientDto.id,token);

        if(granted) {
            ResponseEntity.send(res, new ApiResponse<>("Pharmacist granted Permission", null), HttpServletResponse.SC_OK);
        }else {
            ResponseEntity.send(res, new ApiResponse<>("Unauthorized", null), HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
