package com.medic.servlet.user.models;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Prescription {
    private String id;
    private String patientId;
    private String pharmacistId;
    @NonNull
    private String medName;
    @NonNull
    private String medPrice;
    @NonNull
    private Date medExpDate;

    public Prescription() {
        this.id = UUID.randomUUID().toString();
    }
}
