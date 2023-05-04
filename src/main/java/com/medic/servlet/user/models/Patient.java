package com.medic.servlet.user.models;

import com.medic.servlet.db.Database;
import com.medic.servlet.user.dtos.ConsultationDto;
import com.medic.servlet.user.dtos.PhyisicianDto;
import com.medic.servlet.user.dtos.Role;
import com.medic.servlet.utils.ApiResponse;
import com.medic.servlet.utils.TokenUtil;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;


public class Patient extends User {
    @Override
    public ApiResponse<User> register() throws Exception {

        if (Database.findUser(this.getUserName()) != null) {
            throw new Exception("Username Taken");
        }


        if (!(this.getPassword().length() >= 4 && this.getPassword().length() <= 6)) {
            throw new Exception("Password must be 4 char to 6 char");
        }

        this.encryptPwd();
        Database.addUser(this);
        return new ApiResponse<>("Patient saved successfully", Database.findUser(this.getUserName()));
    }


    @Override
    public ApiResponse<String> login(String username, String password) throws AuthenticationException {
        User foundUser = Database.findUser(username);
        if (foundUser == null) throw new AuthenticationException("Invalid credentials");

        if (!(foundUser.getRole().equals(Role.PATIENT) || foundUser.getRole().equals(Role.PHARMACIST))) throw new AuthenticationException("Not Allowed to login with username");

        if (!password.equals(foundUser.decryptPwd())) throw new AuthenticationException("Invalid login");
        String token = TokenUtil.generateJWT(foundUser.getUserName(),foundUser.getRole(), foundUser.getId());
        return new ApiResponse<>("Login Successful", token);
    }



    //GRANT PERMISSION TO PRACTITIONER
    public boolean grantPermission(Role role, String practitionerId, String token) throws IOException {
        User user = TokenUtil.getUserFromToken(token);
        if (user == null) {
            return false;
        }

        List<User> users = Database.getUsers();
        User targetUser = users.stream()
                .filter(u -> u.getId().equals(user.getId()))
                .findFirst()
                .orElse(null);

        if (targetUser == null) {
            return false;
        }

        List<String> permissions = targetUser.getPermissions();
        permissions.add(practitionerId);
        targetUser.setPermissions(permissions);

        return true;
    }

    public boolean grantPermission(Role role, String practitionerId, String token, PhyisicianDto phyisicianDto) throws IOException {
        User user = TokenUtil.getUserFromToken(token);
        if (user == null) {
            return false;
        }

        List<User> users = Database.getUsers();
        User targetUser = users.stream()
                .filter(u -> u.getId().equals(user.getId()))
                .findFirst()
                .orElse(null);

        if (targetUser == null) {
            return false;
        }

        List<String> permissions = targetUser.getPermissions();
        permissions.add(practitionerId);
        targetUser.setPermissions(permissions);

        if (role == Role.PHYSICIAN && phyisicianDto != null) {
            Consultation newConsultation = new Consultation();
            newConsultation.setPatientId(user.getId());
            newConsultation.setPhysicianId(practitionerId);
            newConsultation.setSymptoms(phyisicianDto.symptoms);
            newConsultation.setDate(Instant.now());
            Database.addConsultation(newConsultation);
        }

        return true;
    }

}
