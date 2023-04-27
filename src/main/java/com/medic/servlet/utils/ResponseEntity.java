package com.medic.servlet.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseEntity {
    public static void send(HttpServletResponse response, ApiResponse payload, int status) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.setStatus(status);
            if (payload != null) {
                OutputStream outputStream = response.getOutputStream();
                outputStream.write(new JsonUtil().toJson(payload).getBytes());
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
