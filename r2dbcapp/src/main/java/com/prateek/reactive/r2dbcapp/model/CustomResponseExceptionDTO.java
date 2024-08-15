package com.prateek.reactive.r2dbcapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomResponseExceptionDTO {
    private String status;
    private String path;
    private String error;

}
