package Facility.reservation.DBP.service;

import Facility.reservation.DBP.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class LoginService {

    @Autowired
    private StudentRepository studentRepository;

    private final String DEFAULT_PASSWORD = "1234"; // 비밀번호 고정

    public boolean authenticate(Long studentId, String password) {
        return studentRepository.findById(studentId).isPresent() && DEFAULT_PASSWORD.equals(password);
    }
}
