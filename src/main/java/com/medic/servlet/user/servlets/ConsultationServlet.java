package com.medic.servlet.user.servlets;


import com.medic.servlet.db.Database;
import com.medic.servlet.user.dtos.PatientDto;
import com.medic.servlet.user.models.Consultation;
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

@WebServlet("/consultation")
public class ConsultationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PatientDto patient = new JsonUtil().parseBodyJson(req, PatientDto.class);
        List<Consultation> cons = Database.getConsultations(patient.id);

        ResponseEntity.send(resp, new ApiResponse<>("Success", cons), HttpServletResponse.SC_OK);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PatientDto patient = new JsonUtil().parseBodyJson(req, PatientDto.class);
        List<Consultation> cons = Database.getConsultations(patient.id);

        List<Consultation> filteredCons = cons.stream()
                .filter(consultation -> !consultation.getMedicines().isEmpty())
                .collect(java.util.stream.Collectors.toList());

        ResponseEntity.send(resp, new ApiResponse<>("Success", filteredCons), HttpServletResponse.SC_OK);
    }


}