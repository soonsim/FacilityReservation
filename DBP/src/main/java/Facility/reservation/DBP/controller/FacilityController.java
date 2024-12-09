package Facility.reservation.DBP.controller;

import Facility.reservation.DBP.entity.Facility;
import Facility.reservation.DBP.service.FacilityService;
import Facility.reservation.DBP.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/facilities")
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @Autowired
    private ReservationService reservationService;

    /**
     * 시설 검색 및 필터링
     *
     * @param location  시설 위치 (검색 조건)
     * @param date      예약 날짜 (검색 조건)
     * @param timeSlot  예약 시간대 (검색 조건)
     * @param model     뷰 모델
     * @return 검색 결과 페이지
     */
    @GetMapping("/search")
    public String searchFacilities(@RequestParam(required = false) String facilityId,
                                   @RequestParam(required = false) String location,
                                   @RequestParam(required = false) LocalDate date,
                                   @RequestParam(required = false) Integer timeSlot,
                                   Model model) {


        // 모든 시설 목록 가져오기
        List<Facility> facilities = facilityService.getAllFacilities();

        if (facilityId != null && !facilityId.isEmpty()) {
            long facilityIdLong = Long.parseLong(facilityId); // String -> long 변환
            facilities = facilities.stream()
                    .filter(f -> f.getFacilityId() == facilityIdLong)
                    .collect(Collectors.toList());
        }


        // 위치로 필터링
        if (location != null && !location.isEmpty()) {
            facilities = facilities.stream()
                    .filter(f -> f.getLocation() != null && f.getLocation().contains(location))
                    .collect(Collectors.toList());
        }

        // 날짜로 필터링
        if (date != null) {
            facilities = facilities.stream()
                    .filter(f -> reservationService.isFacilityAvailable(f.getFacilityId(), date, timeSlot))
                    .collect(Collectors.toList());
        }

         //각 시설의 예약 가능 여부를 트리거로 관리하므로, 예약 횟수와 예약 가능 여부를 트리거에서 처리하게 하므로 이를 삭제
         facilities.forEach(facility -> {
             int reservationCount = reservationService.getCountByLocation(facility.getLocation());
             facility.setReservationCount(reservationCount);

             boolean isAvailable = reservationService.isFacilityAvailable(facility.getFacilityId(), date, timeSlot);
             facility.setAvailable(isAvailable);
         });

        // 정렬 추가
        facilities = facilities.stream()
                .sorted(Comparator.comparingLong(Facility::getFacilityId)) // 오름차순 정렬
                .collect(Collectors.toList());
        // 필터링된 시설 목록을 모델에 추가
        model.addAttribute("facilities", facilities);

        return "facility-search";
    }

    /**
     * 시설 상세 보기
     *
     * @param facilityId 시설 ID
     * @param model      뷰 모델
     * @return 상세 보기 페이지
     */
    @GetMapping("/details")
    public String viewFacilityDetails(@RequestParam Long facilityId, Model model) {
        // 시설 정보 가져오기
        Facility facility = facilityService.getFacilityById(facilityId);

        // 시설이 존재하지 않으면 예외 처리
        if (facility == null) {
            throw new IllegalArgumentException("해당 시설이 존재하지 않습니다: " + facilityId);
        }

        // 시설 정보 모델에 추가
        model.addAttribute("facility", facility);

        // 시설 상세 페이지로 이동
        return "facility-details";  // 템플릿 이름
    }
}

