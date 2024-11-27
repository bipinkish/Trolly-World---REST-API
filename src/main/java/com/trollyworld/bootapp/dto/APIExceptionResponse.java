package com.trollyworld.bootapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIExceptionResponse {
    private String message;
    private boolean status;
}
