package com.example.teacherassistant.controllers;

import com.example.teacherassistant.services.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class SettingsController {

    private final TeacherService teacherService;

    @GetMapping("/settings/account")
    public String account() {
        return "account";
    }
}
