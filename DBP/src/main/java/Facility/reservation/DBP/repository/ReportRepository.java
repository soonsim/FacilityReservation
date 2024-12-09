package Facility.reservation.DBP.repository;

import Facility.reservation.DBP.entity.Report;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReportRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReportRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 신고 내용 추가
    public Report save(Report report) {
        // 1. 현재 테이블에서 가장 큰 REPORT_ID를 구하기
        String sqlMaxId = "SELECT NVL(MAX(REPORT_ID), 0) FROM REPORT";
        Integer maxReportId = jdbcTemplate.queryForObject(sqlMaxId, Integer.class);

        // 2. 그 값에 1을 더해서 새로운 REPORT_ID 설정 (Long 타입으로 설정)
        Long newReportId = (long) (maxReportId + 1);

        // 3. 처리 상태가 없으면 "처리 대기"로 설정
        if (report.getProcessingStatus() == null || report.getProcessingStatus().isEmpty()) {
            report.setProcessingStatus("처리 대기");
        }

        // 4. 새로운 REPORT_ID로 INSERT 쿼리 실행
        String sqlInsert = "INSERT INTO REPORT (REPORT_ID, RESERVATION_ID, REPORT_CONTENT, PROCESSING_STATUS) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sqlInsert, newReportId, report.getReservationId(), report.getReportContent(), report.getProcessingStatus());

        // 5. 저장된 report 객체 반환
        report.setReportId(newReportId);  // Long 타입의 newReportId 세팅
        return report;
    }








    // 신고 목록 조회
    public List<Report> getAllReports() {
        String sql = "SELECT * FROM REPORT";
        return jdbcTemplate.query(sql, reportRowMapper());
    }

    // 예약번호로 신고 조회
    public List<Report> getReportsByReservationId(String reservationId) {
        String sql = "SELECT * FROM REPORT WHERE RESERVATION_ID = ?";
        return jdbcTemplate.query(sql, new Object[]{reservationId}, reportRowMapper());
    }

    // 학생 ID로 신고 조회
    public List<Report> getReportsByStudentId(Long studentId) {
        String sql = "SELECT * FROM REPORT R " +
                "JOIN RESERVATION RES ON R.RESERVATION_ID = RES.RESERVATION_ID " +
                "WHERE RES.STUDENT_ID = ?";
        return jdbcTemplate.query(sql, new Object[]{studentId}, reportRowMapper());
    }


    private RowMapper<Report> reportRowMapper() {
        return (rs, rowNum) -> {
            Report report = new Report();
            report.setReportId(rs.getLong("REPORT_ID"));
            report.setReservationId(rs.getString("RESERVATION_ID"));
            report.setReportContent(rs.getString("REPORT_CONTENT"));
            report.setProcessingStatus(rs.getString("PROCESSING_STATUS"));
            return report;
        };
    }
}
