package com.medic.servlet.user.servlets;

import com.medic.servlet.db.Database;
import com.medic.servlet.user.dtos.ConsultationDto;
import com.medic.servlet.user.dtos.Role;
import com.medic.servlet.user.models.Consultation;
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
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@WebServlet("/physicians")
public class PhysicianServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = Database.GetUserByRole(Role.PHYSICIAN);
        users.sort(Comparator.comparing(User::getFirstName));
        ResponseEntity.send(resp, new ApiResponse<>("Success", users), HttpServletResponse.SC_OK);

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ConsultationDto consultationDto = new JsonUtil().parseBodyJson(req, ConsultationDto.class);
        List<User> users = Database.getUsers();
        User patient = users.stream().filter(user -> user.getId().equals(consultationDto.patientId)).findFirst().orElse(null);

        Instant instant = Instant.now();

        System.out.println(instant);

        if (patient != null) {
            if (patient.getPermissions().stream().filter(permission -> permission.equals(consultationDto.physicianId)).findFirst().orElse(null) == null) {
                ResponseEntity.send(res, new ApiResponse<>("Unauthorized", null), HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                Consultation consultation = new Consultation();
                consultation.setPatientId(consultationDto.patientId);
                consultation.setPhysicianId(consultationDto.physicianId);
                consultation.setDiagnosis(consultationDto.diagnosis);
                consultation.setDate(instant);

                Database.addConsultation(consultation);
                ResponseEntity.send(res, new ApiResponse<>("Consultation Succeeded", consultation), HttpServletResponse.SC_OK);
            }

        }
        ResponseEntity.send(res, new ApiResponse<>("Something went wrong", null), HttpServletResponse.SC_FORBIDDEN);

    }
}
