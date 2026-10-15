package com.maddy.studentapi.dto;

import com.maddy.studentapi.entity.Student;

import java.io.Serializable;

/** Data sent back to the client. Serializable so Redis can cache it. */
public record StudentResponse(Long id, String name, String email, String course, Integer age)
        implements Serializable {

    public static StudentResponse from(Student student) {
        return new StudentResponse(student.getId(), student.getName(), student.getEmail(),
                student.getCourse(), student.getAge());
    }
}
