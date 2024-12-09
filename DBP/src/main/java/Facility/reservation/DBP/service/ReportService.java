package Facility.reservation.DBP.service;

import Facility.reservation.DBP.entity.Report;
import Facility.reservation.DBP.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    // 신고 내용 추가
    public Report createReport(String reservationId, String reportContent, String processingStatus) {
        Report report = new Report();
        report.setReservationId(reservationId); // String 타입으로 저장
        report.setReportContent(reportContent);
        report.setProcessingStatus(processingStatus);

        // JdbcTemplate을 사용한 저장
        return reportRepository.save(report);
    }

    // 신고 목록 조회
    public List<Report> getAllReports() {
        return reportRepository.getAllReports();
    }

    // 예약번호로 신고 조회
    public List<Report> getReportsByReservationId(String reservationId) {
        return reportRepository.getReportsByReservationId(reservationId);
    }

    // 학생 ID로 신고 조회
    public List<Report> getReportsByStudentId(Long studentId) {
        return reportRepository.getReportsByStudentId(studentId);
    }
}
