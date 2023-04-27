package com.medic.servlet.user.models;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Medicine {
    private String id;
    @NonNull
    private String medName;
    @NonNull
    private double medPrice;
    @NonNull
    private String medExpDate;

    public Medicine() {
        this.id = UUID.randomUUID().toString();
    }
}
