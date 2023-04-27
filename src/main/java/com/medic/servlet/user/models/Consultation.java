package com.medic.servlet.user.models;

import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Consultation {
    private String id;
    private String patientId;
    private String physicianId;
    private Instant date;
    @NonNull
    private String diagnosis;
    private List<Medicine> medicines;
    private String symptoms;

    public Consultation() {
        this.id = UUID.randomUUID().toString();
    }
}
