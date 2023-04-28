package com.medic.servlet.db;

import com.medic.servlet.user.dtos.Role;
import com.medic.servlet.user.models.Consultation;
import com.medic.servlet.user.models.Prescription;
import com.medic.servlet.user.models.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Database {
    private static Map<String, User> users = new LinkedHashMap<>();
    private static Map<String, Consultation> consultations = new LinkedHashMap<>();
    private static Map<String, Prescription> prescriptions = new LinkedHashMap<>();

    public static void addUser(User user) {
        users.put(user.getEmail(), user);
    }


    public static User findUser(String email) {
        User user = users.get(email);
        if (user == null) {
            // If user is not found by email, try finding by username
            for (User u : users.values()) {
                if (u.getUserName().equals(email)) {
                    user = u;
                    break;
                }
                if (u.getEmail().equals(email)){
                    user = u;
                    break;
                }
            }
        }
        return user;
    }
    public static List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public static List<User> GetUserByRole(Role role) {
        List<User> usersByRole = new ArrayList<>();

        for (User user : users.values()) {
            if (user.getRole().equals(role)) {
                usersByRole.add(user);
            }
        }

        return usersByRole;
    }


    public static void addConsultation(Consultation consultation) {
        consultations.put(consultation.getId(), consultation);
    }

    public static List<Consultation> getConsultations(String patientId) {
        List<Consultation> cons = new ArrayList<>();

        for (Consultation consultation : consultations.values()) {
            if (consultation.getPatientId().equals(patientId)) {
                cons.add(consultation);
            }
        }
        return cons;
    }

    public static List<Consultation> getConsultation() {
        return new ArrayList<>(consultations.values());
    }

    public static void addPrescription(Prescription prescription) {
        prescriptions.put(prescription.getMedName(), prescription);
    }
    public List<Prescription> getPrescriptions(String patientId) {
        List<Prescription> meds = new ArrayList<>();

        for (Prescription prescription : prescriptions.values()) {
            if (prescription.getPatientId().equals(patientId)) {
                meds.add(prescription);
            }
        }
        return meds;
    }
}

