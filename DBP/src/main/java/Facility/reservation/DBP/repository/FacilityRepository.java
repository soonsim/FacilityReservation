package Facility.reservation.DBP.repository;

import Facility.reservation.DBP.entity.Facility;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FacilityRepository {

    private final JdbcTemplate jdbcTemplate;

    public FacilityRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 모든 시설 조회
    public List<Facility> findAll() {
        String sql = "SELECT * FROM FACILITY";
        return jdbcTemplate.query(sql, facilityRowMapper());
    }

    // Facility ID로 시설 조회 (findById로 이름 변경)
    public Optional<Facility> findById(Long facilityId) {
        String sql = "SELECT * FROM FACILITY WHERE FACILITY_ID = ?";
        List<Facility> facilities = jdbcTemplate.query(sql, new Object[]{facilityId}, facilityRowMapper());
        return facilities.stream().findFirst(); // 결과가 없으면 Optional.empty() 반환
    }

    private RowMapper<Facility> facilityRowMapper() {
        return (rs, rowNum) -> {
            Facility facility = new Facility();
            facility.setFacilityId(rs.getLong("FACILITY_ID"));
            facility.setFacilityName(rs.getString("FACILITY_NAME"));
            facility.setCapacity(rs.getInt("CAPACITY"));
            facility.setLocation(rs.getString("LOCATION"));
            facility.setUsageGuidelines(rs.getString("USAGE_GUIDELINES"));
            facility.setAvailableStartTime(rs.getInt("AVAILABLE_START_TIME"));
            facility.setAvailableEndTime(rs.getInt("AVAILABLE_END_TIME"));
            return facility;
        };
    }

    // Facility 이름으로 검색
    public List<Facility> getFacilitiesByName(String facilityName) {
        String sql = "SELECT * FROM FACILITY WHERE FACILITY_NAME LIKE ?";
        return jdbcTemplate.query(sql, new Object[]{"%" + facilityName + "%"}, facilityRowMapper());
    }

    // 시설 추가
    public void addFacility(Facility facility) {
        String sql = "INSERT INTO FACILITY (FACILITY_NAME, CAPACITY, LOCATION, USAGE_GUIDELINES, AVAILABLE_START_TIME, AVAILABLE_END_TIME) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, facility.getFacilityName(), facility.getCapacity(), facility.getLocation(),
                facility.getUsageGuidelines(), facility.getAvailableStartTime(), facility.getAvailableEndTime());
    }

    // 시설 수정
    public void updateFacility(Facility facility) {
        String sql = "UPDATE FACILITY SET FACILITY_NAME = ?, CAPACITY = ?, LOCATION = ?, USAGE_GUIDELINES = ?, AVAILABLE_START_TIME = ?, AVAILABLE_END_TIME = ? WHERE FACILITY_ID = ?";
        jdbcTemplate.update(sql, facility.getFacilityName(), facility.getCapacity(), facility.getLocation(),
                facility.getUsageGuidelines(), facility.getAvailableStartTime(), facility.getAvailableEndTime(),
                facility.getFacilityId());
    }

    // 시설 삭제
    public void deleteFacility(Long facilityId) {
        String sql = "DELETE FROM FACILITY WHERE FACILITY_ID = ?";
        jdbcTemplate.update(sql, facilityId);
    }
}
