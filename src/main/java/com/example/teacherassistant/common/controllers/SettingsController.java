package com.example.teacherassistant.common.controllers;

import com.example.teacherassistant.teacherPackage.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SettingsController {

    private final TeacherService teacherService;

    @GetMapping("/settings/account")
    public String account() {
        return "account";
    }
}
