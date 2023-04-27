package com.medic.servlet.user.servlets;

import com.medic.servlet.db.Database;
import com.medic.servlet.user.dtos.PrescriptionDto;
import com.medic.servlet.user.dtos.Role;
import com.medic.servlet.user.models.Prescription;
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
import java.util.Comparator;
import java.util.List;

@WebServlet("/pharmacist")
public class PharmacistServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = Database.GetUserByRole(Role.PHARMACIST);
        users.sort(Comparator.comparing(User::getAge));
        ResponseEntity.send(resp, new ApiResponse<>("Success", users), HttpServletResponse.SC_OK);

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrescriptionDto consultationDto = new JsonUtil().parseBodyJson(req, PrescriptionDto.class);
        List<User> users = Database.getUsers();
        User patient = users.stream().filter(user -> user.getId().equals(consultationDto.patientId)).findFirst().orElse(null);

        if (patient != null) {
            if (patient.getPermissions().stream().filter(permission -> permission.equals(consultationDto.pharmacistId)).findFirst().orElse(null) == null) {
                ResponseEntity.send(res, new ApiResponse<>("Unauthorized", null), HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                Prescription prescription = new Prescription();
                prescription.setPatientId(consultationDto.patientId);
                prescription.setPharmacistId(consultationDto.pharmacistId);
                prescription.setMedName(consultationDto.medName);
                prescription.setMedPrice(consultationDto.medPrice);
                prescription.setMedExpDate(consultationDto.medExpDate);

                Database.addPrescription(prescription);
                ResponseEntity.send(res, new ApiResponse<>("Consultation Succeeded", prescription), HttpServletResponse.SC_OK);
            }

        } else {
            ResponseEntity.send(res, new ApiResponse<>("Something went wrong", null), HttpServletResponse.SC_FORBIDDEN);
        }


    }
}
