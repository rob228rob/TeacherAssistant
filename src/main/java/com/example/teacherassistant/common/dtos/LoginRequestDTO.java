package com.example.teacherassistant.common.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class LoginRequestDTO {

    private String phoneNumber;

    private String password;
}
