package com.example.repository.itf;

import com.example.model.Student;

import java.util.List;

public interface IStudentRepository extends IGenerateRepository<Student> {
    List<Student> findByName(String q);
}
