package com.maddy.studentapi.repository;

import com.maddy.studentapi.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data JPA writes the implementation of these methods for us. */
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
