package Facility.reservation.DBP.controller;

import Facility.reservation.DBP.entity.Reservation;
import Facility.reservation.DBP.service.ReservationService;
import Facility.reservation.DBP.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private FacilityService facilityService;

    /**
     * 예약 목록 보기
     */
    @GetMapping
    public String viewReservations(HttpSession session, Model model) {
        Long studentId = (Long) session.getAttribute("loggedInStudentId");
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        List<Reservation> reservations = reservationService.getReservationsByStudentId(studentId);

        reservations.forEach(reservation -> {
            if (reservation.getReservationDate() != null) {
                reservation.setFormattedDate(reservation.getReservationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        });

        model.addAttribute("reservations", reservations);
        model.addAttribute("facilities", reservationService.getAllFacilities());
        return "reservation";
    }

    /**
     * 예약 추가
     */
    @PostMapping("/add")
    public String addReservation(@ModelAttribute Reservation reservation, HttpSession session) {
        Long studentId = (Long) session.getAttribute("loggedInStudentId");
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        reservation.setStudentId(studentId);

        if (reservation.getFacility() == null || reservation.getFacility().getFacilityId() == null) {
            throw new IllegalArgumentException("시설이 선택되지 않았습니다.");
        }

        Long facilityId = reservation.getFacility().getFacilityId();
        //Long facilityId = reservation.getFacilityId();
        reservation.setFacilityId(facilityId);
        Integer startTime = reservation.getStartTime();
        Integer endTime = reservation.getEndTime();
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("시작 시간과 종료 시간을 입력해야 합니다.");
        }
        if (startTime >= endTime) {
            throw new IllegalArgumentException("시작 시간은 종료 시간보다 빠를 수 없습니다.");
        }


        int reservationCount = reservationService.getCountByFacilityAndDate(facilityId, reservation.getReservationDate(), startTime);
        String reservationId = facilityId + reservation.getReservationDate().toString() + startTime;
        reservation.setReservationId(reservationId);
        System.out.println(reservation);
        reservationService.addReservation(reservation);

        return "redirect:/reservations";
    }

    /**
     * 예약 수정 페이지
     */
    @GetMapping("/edit")
    public String editReservationPage(@RequestParam String reservationId, Model model) {
        Reservation reservation = reservationService.getReservationById(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException("예약이 존재하지 않습니다.");
        }

        model.addAttribute("reservation", reservation);
        model.addAttribute("facilities", reservationService.getAllFacilities());

        return "reservation-edit";
    }

    /**
     * 예약 수정
     */
    @PostMapping("/update")
    public String updateReservation(@RequestParam String reservationId,
                                    @RequestParam LocalDate reservationDate,
                                    @RequestParam Integer startTime,
                                    @RequestParam Integer endTime) {
        // 예약 ID로 예약 조회
        Reservation reservation = reservationService.getReservationById(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException("해당 예약이 존재하지 않습니다.");
        }

        if (reservationDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("예약 날짜는 오늘 이후여야 합니다.");
        }

        // 날짜와 시간 검증
        if (startTime >= endTime) {
            throw new IllegalArgumentException("시작 시간은 종료 시간보다 빠를 수 없습니다.");
        }

        // 수정된 예약 정보를 반영
        reservation.setReservationDate(reservationDate);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);

        // 예약 업데이트
        reservationService.updateReservation(reservation);

        return "redirect:/reservations";
    }

    /**
     * 예약 삭제
     */
    @PostMapping("/delete")
    public String deleteReservation(@RequestParam String reservationId) {
        try {
            reservationService.deleteReservation(reservationId);
            return "redirect:/reservations";
        } catch (Exception e) {
            throw new IllegalArgumentException("예약 ID를 찾을 수 없습니다: " + reservationId);
        }
    }

    /**
     * 시설 검색
     */
    @GetMapping("/facility-search")
    public String facilitySearchPage(Model model) {
        model.addAttribute("facilities", facilityService.getAllFacilities());
        return "facility-search";
    }
}