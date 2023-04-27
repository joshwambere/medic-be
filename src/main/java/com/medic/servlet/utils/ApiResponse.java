package com.medic.servlet.utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApiResponse<T> {
    String message;
    T payload;
}
