package Facility.reservation.DBP.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "REPORT")
@Getter
@Setter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPORT_ID")
    private Long reportId;

    @ManyToOne
    @JoinColumn(name = "RESERVATION_ID", referencedColumnName = "RESERVATION_ID", insertable = false, updatable = false)
    private Reservation reservation;  // Reservation 객체와 연결

    @Column(name = "RESERVATION_ID", length = 25)
    private String reservationId;  // reservationId는 String 타입으로 유지

    @Lob
    @Column(name = "REPORT_CONTENT")
    private String reportContent;

    @Column(name = "PROCESSING_STATUS", length = 100)
    private String processingStatus;

    // reservationId를 String으로 반환하는 메서드
    public String getReservationIdAsString() {
        return this.reservationId;
    }

    // reservation 객체로부터 reservationId를 설정하는 메서드
    public void setReservationIdFromReservation(Reservation reservation) {
        this.reservation = reservation;
        this.reservationId = reservation.getReservationId();  // reservationId는 String으로 설정
    }
}
