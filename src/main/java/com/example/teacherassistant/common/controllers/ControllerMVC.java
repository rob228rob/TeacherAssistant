package com.example.teacherassistant.common.controllers;

import com.example.teacherassistant.common.dtos.RegisterTeacherDTO;
import com.example.teacherassistant.teacherPackage.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ControllerMVC {

    private final TeacherService teacherService;

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("registerTeacherDTO", new RegisterTeacherDTO());
        return "signup";
    }

    @GetMapping("/about")
    public String about() {
        return "about-me";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        return "settings";
    }

    @GetMapping("/profile/info")
    public String profileInfo(Model model) {
        return "profile-info";
    }

    @GetMapping("/lessons/add-new/{studentId}")
    public String addNewLesson(@PathVariable Long studentId) {
        return "add-new-lesson";
    }

    @GetMapping("/error")
    public String error() {
        return "error-page";
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