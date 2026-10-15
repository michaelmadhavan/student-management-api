package com.maddy.studentapi.service;

import com.maddy.studentapi.dto.PageResponse;
import com.maddy.studentapi.dto.StudentRequest;
import com.maddy.studentapi.dto.StudentResponse;
import com.maddy.studentapi.entity.Student;
import com.maddy.studentapi.exception.DuplicateEmailException;
import com.maddy.studentapi.exception.ResourceNotFoundException;
import com.maddy.studentapi.repository.StudentRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Business logic. Redis caching is applied on single-student reads. */
@Service
public class StudentService {

    private static final String CACHE_NAME = "students";

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public StudentResponse create(StudentRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new DuplicateEmailException("Email already exists: " + request.email());
        }
        Student student = new Student(request.name(), request.email(), request.course(), request.age());
        return StudentResponse.from(repository.save(student));
    }

    /** First call reads MySQL and stores the result in Redis. Next calls come from Redis. */
    @Cacheable(value = CACHE_NAME, key = "#id")
    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {
        return StudentResponse.from(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<StudentResponse> getAll(String name, Pageable pageable) {
        Page<Student> page = (name == null || name.isBlank())
                ? repository.findAll(pageable)
                : repository.findByNameContainingIgnoreCase(name.trim(), pageable);
        return PageResponse.from(page.map(StudentResponse::from));
    }

    /** Updates MySQL and refreshes the cached copy in Redis. */
    @CachePut(value = CACHE_NAME, key = "#id")
    @Transactional
    public StudentResponse update(Long id, StudentRequest request) {
        Student student = findOrThrow(id);
        if (repository.existsByEmailAndIdNot(request.email(), id)) {
            throw new DuplicateEmailException("Email already exists: " + request.email());
        }
        student.setName(request.name());
        student.setEmail(request.email());
        student.setCourse(request.course());
        student.setAge(request.age());
        return StudentResponse.from(repository.save(student));
    }

    /** Deletes from MySQL and removes the cached copy from Redis. */
    @CacheEvict(value = CACHE_NAME, key = "#id")
    @Transactional
    public void delete(Long id) {
        repository.delete(findOrThrow(id));
    }

    private Student findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
    }
}
