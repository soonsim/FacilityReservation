package Facility.reservation.DBP.repository;

import Facility.reservation.DBP.entity.Student;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepository extends BaseRepository<Student, Long> {

    // 생성자
    public StudentRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate); // 부모 클래스 생성자 호출하여 JdbcTemplate 주입
    }

    // 학생 추가
    @Override
    public int add(Student student) {
        String sql = "INSERT INTO STUDENT (STUDENT_ID, NAME, GENDER, CONTACT_NUMBER, DEPARTMENT_ID) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, student.getStudentId(), student.getName(), student.getGender(),
                student.getContactNumber(), student.getDepartmentId());
    }

    // 학생 조회 by ID
    @Override
    public Optional<Student> getById(Long studentId) {
        String sql = "SELECT * FROM STUDENT WHERE STUDENT_ID = ?";
        try {
            Student student = jdbcTemplate.queryForObject(sql, new Object[]{studentId}, getRowMapper());
            return Optional.ofNullable(student);
        } catch (Exception e) {
            return Optional.empty(); // 해당 ID의 학생이 없을 경우
        }
    }

    // 모든 학생 조회
    @Override
    public List<Student> getAll() {
        String sql = "SELECT * FROM STUDENT";
        return jdbcTemplate.query(sql, getRowMapper());
    }

    // 학생 업데이트
    @Override
    public int update(Student student) {
        String sql = "UPDATE STUDENT SET NAME = ?, GENDER = ?, CONTACT_NUMBER = ?, DEPARTMENT_ID = ? WHERE STUDENT_ID = ?";
        return jdbcTemplate.update(sql, student.getName(), student.getGender(), student.getContactNumber(),
                student.getDepartmentId(), student.getStudentId());
    }

    // 학생 삭제
    @Override
    public int delete(Long studentId) {
        String sql = "DELETE FROM STUDENT WHERE STUDENT_ID = ?";
        return jdbcTemplate.update(sql, studentId);
    }

    // RowMapper 구현
    @Override
    protected RowMapper<Student> getRowMapper() {
        return new RowMapper<Student>() {
            @Override
            public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
                Student student = new Student();
                student.setStudentId(rs.getLong("STUDENT_ID"));
                student.setName(rs.getString("NAME"));
                student.setGender(rs.getString("GENDER"));
                student.setContactNumber(rs.getString("CONTACT_NUMBER"));
                student.setDepartmentId(rs.getLong("DEPARTMENT_ID"));
                return student;
            }
        };
    }

    // findById 추가 (기존 getById 메서드 호출)
    public Optional<Student> findById(Long studentId) {
        return getById(studentId);
    }
}
