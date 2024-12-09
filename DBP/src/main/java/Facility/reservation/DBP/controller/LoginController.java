package Facility.reservation.DBP.controller;

import Facility.reservation.DBP.service.LoginService;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html 파일 렌더링
    }

    @PostMapping("/login")
    public String login(@RequestParam Long studentId, @RequestParam String password, HttpSession session) {
        if (loginService.authenticate(studentId, password)) {
            session.setAttribute("loggedInStudentId", studentId); // 세션에 학번 저장
            return "redirect:/reservations"; // 예약 내역 페이지로 이동
        } else {
            return "login"; // 로그인 실패 시 다시 로그인 페이지
        }
    }

}
