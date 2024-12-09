package Facility.reservation.DBP.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<T, ID> {

    protected final JdbcTemplate jdbcTemplate;

    // 생성자
    public BaseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 데이터 추가
    public abstract int add(T entity);

    // 데이터 조회 by ID
    public abstract Optional<T> getById(ID id);

    // 모든 데이터 조회
    public abstract List<T> getAll();

    // 데이터 업데이트
    public abstract int update(T entity);

    // 데이터 삭제
    public abstract int delete(ID id);

    // RowMapper 반환 (구현 클래스에서 정의)
    protected abstract RowMapper<T> getRowMapper();
}
