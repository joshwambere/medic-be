package com.medic.servlet.user.models;

import com.medic.servlet.utils.ApiResponse;
import com.medic.servlet.user.dtos.Gender;
import com.medic.servlet.user.dtos.Role;
import com.google.gson.annotations.Expose;
import lombok.*;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public abstract class User {
    private String id;
    @NonNull
    private String name;
    private  String userName;
    @NonNull
    private String email;
    @NonNull
    @Expose(serialize = false)
    private String password;
    private Gender gender;
    private Integer age;
    private Long phone;
    private Role role;
    private List<String> permissions = new ArrayList<>();

    public User() {
        this.id = UUID.randomUUID().toString();
    }

    public abstract ApiResponse<User> register() throws Exception;

    public abstract ApiResponse<String> login(String email, String password) throws AuthenticationException;

    public void encryptPwd() {
        String encryptedPassword = "";
        for (int i = 0; i < this.password.length(); i++) {
            encryptedPassword = this.password.charAt(i) + encryptedPassword;
        }
        encryptedPassword = "**" + encryptedPassword + "<>"+ this.age + "**";
        this.setPassword(encryptedPassword);
    }

    public String decryptPwd() {
        String decryptedPwd = "";
        String reversedPwd = this.password.substring(2, this.password.length() - 2).split("<>")[0];
        for (int i = 0; i < reversedPwd.length(); i++) {
            decryptedPwd = reversedPwd.charAt(i) + decryptedPwd;
        }
        return decryptedPwd;
    }

}
