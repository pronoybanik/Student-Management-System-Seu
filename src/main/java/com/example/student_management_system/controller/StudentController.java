
package com.example.student_management_system.controller;

import com.example.student_management_system.model.Student;
import com.example.student_management_system.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody Student student) {
        try {
            validate(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(studentService.addStudent(student));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", exception.getMessage()));
        }
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable int id) {
        Optional<Student> student = studentService.getStudentById(id);
        return student.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Student with id " + id + " was not found")));
    }

    @GetMapping("/sort/id")
    public List<Student> sortById() {
        return studentService.getStudentsSortedById();
    }

    @GetMapping("/sort/cgpa")
    public List<Student> sortByCgpa() {
        return studentService.getStudentsSortedByCgpa();
    }

    @GetMapping("/departments")
    public Set<String> getDepartments() {
        return studentService.getDepartments();
    }

    @GetMapping("/recent")
    public List<Student> getRecentStudents() {
        return studentService.getRecentStudents();
    }

    @GetMapping("/recent/peek")
    public ResponseEntity<?> peekRecentStudent() {
        Student student = studentService.peekRecentStudent();
        return student == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(student);
    }

    @DeleteMapping("/recent/pop")
    public ResponseEntity<?> popRecentStudent() {
        Student student = studentService.popRecentStudent();
        return student == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(student);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveStudents() {
        try {
            studentService.save();
            return ResponseEntity.ok(Map.of("message", "Students serialized successfully"));
        } catch (IOException exception) {
            return ResponseEntity.internalServerError().body(Map.of("error", exception.getMessage()));
        }
    }

    @PostMapping("/load")
    public ResponseEntity<?> loadStudents() {
        try {
            int count = studentService.load();
            return ResponseEntity.ok(Map.of("message", "Students deserialized successfully", "count", count));
        } catch (IOException | ClassNotFoundException exception) {
            return ResponseEntity.internalServerError().body(Map.of("error", exception.getMessage()));
        }
    }

    private void validate(Student student) {
        if (student == null || student.getId() <= 0) {
            throw new IllegalArgumentException("id must be greater than zero");
        }
        if (isBlank(student.getName()) || isBlank(student.getDepartment())) {
            throw new IllegalArgumentException("name and department are required");
        }
        if (student.getCgpa() < 0 || student.getCgpa() > 4) {
            throw new IllegalArgumentException("cgpa must be between 0 and 4");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
