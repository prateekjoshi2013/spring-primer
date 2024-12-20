package com.prateek.web.springrestdemo.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CustomerDTO {
    private String customerName;
    private UUID id;
    private String email;
    private Integer version;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
