package com.medic.servlet.user.servlets;


import com.medic.servlet.user.models.Medicine;
import com.medic.servlet.utils.ApiResponse;
import com.medic.servlet.utils.CSVUtil;
import com.medic.servlet.utils.JsonUtil;
import com.medic.servlet.utils.ResponseEntity;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/medicine")
public class MedicineServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Medicine> meds = CSVUtil.readCSV("medicines.csv");
        ResponseEntity.send(resp, new ApiResponse<>("Success", meds), HttpServletResponse.SC_OK);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Medicine medicine = new JsonUtil().parseBodyJson(req, Medicine.class);
        List<Medicine> medicines = new ArrayList<Medicine>();
        medicines.add(medicine);
        CSVUtil.writeCSV("medicines.csv", medicines);
        ResponseEntity.send(resp, new ApiResponse<>("Success", null), HttpServletResponse.SC_OK);
    }
}
