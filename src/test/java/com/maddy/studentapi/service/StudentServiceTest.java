package com.maddy.studentapi.service;

import com.maddy.studentapi.dto.StudentRequest;
import com.maddy.studentapi.entity.Student;
import com.maddy.studentapi.exception.DuplicateEmailException;
import com.maddy.studentapi.exception.ResourceNotFoundException;
import com.maddy.studentapi.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository repository;

    @InjectMocks
    private StudentService service;

    private final StudentRequest request = new StudentRequest("Maddy", "maddy@example.com", "CSE", 22);

    @Test
    void create_savesStudent_whenEmailIsNew() {
        when(repository.existsByEmail("maddy@example.com")).thenReturn(false);
        when(repository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertEquals("Maddy", service.create(request).name());
    }

    @Test
    void create_throws_whenEmailAlreadyExists() {
        when(repository.existsByEmail("maddy@example.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> service.create(request));
        verify(repository, never()).save(any(Student.class));
    }

    @Test
    void getById_throws_whenStudentMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }
}
