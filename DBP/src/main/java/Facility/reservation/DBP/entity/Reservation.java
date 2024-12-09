package Facility.reservation.DBP.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "RESERVATION")
@Getter
@Setter
public class Reservation {



    @Id
    @Column(name = "RESERVATION_ID", nullable = false)
    private String reservationId; // 예약 ID (Primary Key)

    @Column(name = "STUDENT_ID", nullable = false)
    private Long studentId; // 학생 ID

    @ManyToOne(fetch = FetchType.EAGER)
   // @JoinColumn(name = "FACILITY_ID", referencedColumnName = "FACILITY_ID", nullable = false)
    private Facility facility; // 시설과의 Many-to-One 관계

    @Column(name = "FACILITY_ID" , nullable = false)
    private  Long facilityId;


    @Column(name = "RESERVATION_DATE", nullable = false)
    private java.time.LocalDate reservationDate; // 예약 날짜

    @Column(name = "START_TIME", nullable = false)
    private Integer startTime; // 시작 시간

    @Column(name = "END_TIME", nullable = false)
    private Integer endTime; // 종료 시간

    @Transient
    private String formattedDate; // 포맷된 날짜 (DB에 저장하지 않음)


    @Override
    public String toString() {
        return "Reservation{" +

                ", reservationId='" + reservationId + '\'' +
                ", studentId=" + studentId +
                ", facility=" + facility +
                ", facilityId=" + facilityId +
                ", reservationDate=" + reservationDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", formattedDate='" + formattedDate + '\'' +
                '}';
    }
}
