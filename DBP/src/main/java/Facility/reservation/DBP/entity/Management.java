package Facility.reservation.DBP.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "MANAGEMENT")
@Getter
@Setter
public class Management {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FACILITY_ID")
    private Long facilityId;

    @Column(name = "DATE")
    private java.time.LocalDate date;

    @Column(name = "MANAGER", length = 100)
    private String manager;
}
