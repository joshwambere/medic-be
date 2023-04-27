package com.medic.servlet.utils;


import com.medic.servlet.user.models.Medicine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {
    // Read data from CSV file
    public static List<Medicine> readCSV(String filePath) {
        List<Medicine> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length == 3) {
                    String medName = row[0].trim();
                    double medPrice = Double.parseDouble(row[1].trim());
                    String medExpDate = row[2].trim();
                    Medicine medicine = new Medicine();
                    medicine.setMedName(medName);
                    medicine.setMedPrice(medPrice);
                    medicine.setMedExpDate(medExpDate);
                    data.add(medicine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    // Write data to CSV file
    public static void writeCSV(String filePath, List<Medicine> data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Create parent directories if necessary
            File file = new File(filePath);
            System.out.println(file.getAbsolutePath());
            System.out.println(filePath);


            for (Medicine medicine : data) {
                StringBuilder sb = new StringBuilder();
                sb.append(medicine.getMedName()).append(",");
                sb.append(medicine.getMedPrice()).append(",");
                sb.append(medicine.getMedExpDate());
                writer.write(sb.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
