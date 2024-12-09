package Facility.reservation.DBP.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String showHomePage() {
        return "home"; // home.html 템플릿 반환
    }
}

