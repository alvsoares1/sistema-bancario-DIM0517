package com.example.banco.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record ApiError(
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime timestamp,
    Integer code,
    String status,
    List<String> errors) {}
