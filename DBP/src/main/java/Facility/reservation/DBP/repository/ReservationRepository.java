package Facility.reservation.DBP.repository;

import Facility.reservation.DBP.entity.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 학번을 기준으로 예약 조회
    public List<Reservation> findByStudentId(Long studentId) {
        String sql = "SELECT * FROM RESERVATION WHERE STUDENT_ID = ?";
        return jdbcTemplate.query(sql, new Object[]{studentId}, reservationRowMapper());
    }

    // 예약 ID로 예약 조회
    public Optional<Reservation> findById(String reservationId) {
        String sql = "SELECT * FROM RESERVATION WHERE RESERVATION_ID = ?";
        List<Reservation> reservations = jdbcTemplate.query(sql, new Object[]{reservationId}, reservationRowMapper());
        return reservations.stream().findFirst();
    }

    // 예약 추가
    public void save(Reservation reservation) {
        String sql = "INSERT INTO RESERVATION (RESERVATION_ID, STUDENT_ID, FACILITY_ID, RESERVATION_DATE, START_TIME, END_TIME) VALUES (?,?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, reservation.getReservationId(), reservation.getStudentId(),reservation.getFacility().getFacilityId(), reservation.getReservationDate(), reservation.getStartTime(), reservation.getEndTime());
    }

    // 예약 수정
    public void update(Reservation reservation) {
        String sql = "UPDATE RESERVATION SET STUDENT_ID = ?, RESERVATION_DATE = ?, START_TIME = ?, END_TIME = ? WHERE RESERVATION_ID = ?";
        jdbcTemplate.update(sql, reservation.getStudentId(), reservation.getReservationDate(), reservation.getStartTime(), reservation.getEndTime(), reservation.getReservationId());
    }

    // 예약 삭제
    public void deleteById(String reservationId) {
        String sql = "DELETE FROM RESERVATION WHERE RESERVATION_ID = ?";
        jdbcTemplate.update(sql, reservationId);
    }

    // 특정 날짜와 시설의 예약 개수 조회
      public int countByFacilityAndDate(Long facilityId, LocalDate date, Integer timeSlot) {
          String sql = "SELECT COUNT(*) FROM RESERVATION WHERE FACILITY_ID = ? AND RESERVATION_DATE = ? AND START_TIME <= ? AND END_TIME >= ?";
          return jdbcTemplate.queryForObject(sql, new Object[]{facilityId, date, timeSlot, timeSlot}, Integer.class);
      }

    public int countByLocation(String location) {
        //예약 개수 조회
        String sql = "SELECT COUNT(*) FROM RESERVATION r JOIN FACILITY f ON r.FACILITY_ID = f.FACILITY_ID WHERE f.LOCATION = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{location}, Integer.class);
    }

    // 시설 예약 가능 여부 확인 (시간 범위 포함)
    public List<Reservation> findReservationsWithinTimeRange(Long facilityId, LocalDate date, Integer startTime, Integer endTime) {
        String sql = "SELECT * FROM RESERVATION WHERE FACILITY_ID = ? AND RESERVATION_DATE = ? AND ((START_TIME BETWEEN ? AND ?) OR (END_TIME BETWEEN ? AND ?))";
        return jdbcTemplate.query(sql, new Object[]{facilityId, date, startTime, endTime, startTime, endTime}, reservationRowMapper());
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return (rs, rowNum) -> {
            Reservation reservation = new Reservation();
            reservation.setReservationId(rs.getString("RESERVATION_ID"));
            reservation.setStudentId(rs.getLong("STUDENT_ID"));
            reservation.setFacilityId(rs.getLong("FACILITY_ID"));
            reservation.setReservationDate(rs.getDate("RESERVATION_DATE").toLocalDate());
            reservation.setStartTime(rs.getInt("START_TIME"));
            reservation.setEndTime(rs.getInt("END_TIME"));
            return reservation;
        };
    }
}
