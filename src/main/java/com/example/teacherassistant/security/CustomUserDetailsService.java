package com.example.teacherassistant.security;

import com.example.teacherassistant.entities.Teacher;
import com.example.teacherassistant.myExceptions.TeacherNotFoundException;
import com.example.teacherassistant.services.TeacherService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final TeacherService teacherService;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        try {
            Teacher teacherByPhone = teacherService.findTeacherByPhone(phoneNumber);

            return User.builder()
                    .username(teacherByPhone.getPhoneNumber())
                    .password(teacherByPhone.getPassword())
                    .roles("TEACHER")
                    .build();
        } catch (TeacherNotFoundException e) {
            throw new UsernameNotFoundException("Teacher with phone: " + phoneNumber + " not found");
        }


    }
}
