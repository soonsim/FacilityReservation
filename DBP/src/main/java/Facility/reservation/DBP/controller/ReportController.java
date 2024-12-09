package Facility.reservation.DBP.controller;

import Facility.reservation.DBP.entity.Report;
import Facility.reservation.DBP.entity.Reservation;
import Facility.reservation.DBP.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReservationService reservationService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 신고 작성 처리
    @PostMapping("/reports")
    public String createReport(@RequestParam String reservationId,
                               @RequestParam String reportContent,
                               @RequestParam String processingStatus) {
        // 신고 작성 처리, studentId가 필요하면 세션에서 가져오기
        reportService.createReport(reservationId, reportContent, processingStatus);
        return "redirect:/reports";
    }

    // 예약번호로 신고 조회
    @GetMapping("/reports/{reservationId}")
    public String getReportsByReservationId(@PathVariable String reservationId, Model model) {
        List<Report> reports = reportService.getReportsByReservationId(reservationId);
        model.addAttribute("reports", reports);
        return "report/list";  // 리포트 목록 페이지로 이동
    }

    @GetMapping
    public String viewReports(HttpSession session, Model model) {
        Long studentId = (Long) session.getAttribute("loggedInStudentId");
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        List<Report> reports = reportService.getReportsByStudentId(studentId);
        model.addAttribute("reports", reports);

        return "report-list"; // 신고 목록 페이지
    }

    @GetMapping("/write")
    public String writeReportPage(HttpSession session, Model model) {
        Long studentId = (Long) session.getAttribute("loggedInStudentId");
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        // 사용자의 예약 목록을 가져와서 모델에 추가
        List<Reservation> reservations = reservationService.getReservationsByStudentId(studentId);
        model.addAttribute("reservations", reservations);

        return "report-write"; // 신고 작성 페이지
    }


    @PostMapping("/submit")
    public String submitReport(@RequestParam String reservationId,
                               @RequestParam String reportContent,
                               HttpSession session) {
        Long studentId = (Long) session.getAttribute("loggedInStudentId");
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        // processingStatus를 넘기지 않아도 트리거에서 자동으로 '처리 대기'로 처리됨
        reportService.createReport(reservationId, reportContent, null);  // null을 넘기면 트리거가 처리
        return "redirect:/report"; // 신고 목록으로 리다이렉트
    }

}
