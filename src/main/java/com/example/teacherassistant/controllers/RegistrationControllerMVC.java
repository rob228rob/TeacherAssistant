package com.example.teacherassistant.controllers;

import com.example.teacherassistant.dtos.RegisterTeacherDTO;
import com.example.teacherassistant.services.TeacherService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RegistrationControllerMVC {

    private final TeacherService teacherService;

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("registerTeacherDTO", new RegisterTeacherDTO());
        return "signup";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @PostMapping("/register")
    public String registerTeacher(@RequestBody RegisterTeacherDTO registerTeacherDTO) {
        if (teacherService.existsByPhoneNumber(registerTeacherDTO.getPhoneNumber())) {
            log.debug("Teacher with phone number: {} already exists", registerTeacherDTO.getPhoneNumber());
            return "signup";  // Вернем обратно форму с ошибкой
        }

        log.info("Teacher with phone number: {} saved successfully", registerTeacherDTO.getPhoneNumber());
        teacherService.saveTeacher(registerTeacherDTO);
        return "redirect:/home";  // После успешной регистрации перенаправим на страницу входа
    }
}