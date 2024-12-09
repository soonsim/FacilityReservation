package Facility.reservation.DBP.service;

import Facility.reservation.DBP.entity.Reservation;
import Facility.reservation.DBP.entity.Facility;
import Facility.reservation.DBP.repository.FacilityRepository;
import Facility.reservation.DBP.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final FacilityRepository facilityRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(FacilityRepository facilityRepository, ReservationRepository reservationRepository) {
        this.facilityRepository = facilityRepository;
        this.reservationRepository = reservationRepository;
    }

    // Facility ID로 시설 조회
    public Facility getFacilityById(Long facilityId) {
        return facilityRepository.findById(facilityId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Facility ID를 가진 시설이 존재하지 않습니다: " + facilityId));
    }

    // 모든 시설 조회
    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    // 학번을 기준으로 예약 조회
    public List<Reservation> getReservationsByStudentId(Long studentId) {
        return reservationRepository.findByStudentId(studentId);
    }

    // 특정 날짜와 시설의 예약 개수 조회
    public int getCountByFacilityAndDate(Long facilityId, LocalDate date, Integer timeSlot) {
        return reservationRepository.countByFacilityAndDate(facilityId, date, timeSlot);
    }

    // 예약 추가
    public void addReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    // 예약 수정
    public void updateReservation(Reservation reservation) {
        reservationRepository.update(reservation);
    }

    // 예약 삭제
    public void deleteReservation(String reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    // 예약 ID로 예약 조회
    public Reservation getReservationById(String reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약을 찾을 수 없습니다: " + reservationId));
    }

    // location을 기준으로 예약 횟수 반환
    public int getCountByLocation(String location) {
        return reservationRepository.countByLocation(location);
    }

    // 시설 예약 가능 여부 확인
    public boolean isFacilityAvailable(Long facilityId, LocalDate date, Integer timeSlot) {
        int reservationCount = reservationRepository.countByFacilityAndDate(facilityId, date, timeSlot);
        return reservationCount == 0; // 예약이 없으면 true
    }

    // 시설 예약 가능 여부 확인 (시간 범위 포함)
    public boolean isFacilityAvailableWithinTime(Long facilityId, LocalDate date, Integer startTime, Integer endTime) {
        return reservationRepository.findReservationsWithinTimeRange(facilityId, date, startTime, endTime).isEmpty();
    }
}
