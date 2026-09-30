package com.example.student_management_system.service;

import com.example.student_management_system.comparator.CgpaDescendingComparator;
import com.example.student_management_system.model.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;

@Service
public class StudentService {

    private final List<Student> students = new ArrayList<>();
    private final Stack<Student> recentStudents = new Stack<>();
    private final Path storageFile;

    public StudentService(@Value("${student.data.file:students.ser}") String storageFile) {
        this.storageFile = Paths.get(storageFile);
    }

    public synchronized Student addStudent(Student student) {
        if (students.stream().anyMatch(existing -> existing.getId() == student.getId())) {
            throw new IllegalArgumentException("A student with id " + student.getId() + " already exists");
        }
        students.add(student);
        recentStudents.push(student);
        while (recentStudents.size() > 3) {
            recentStudents.remove(0);
        }
        return student;
    }

    /** Uses an Iterator explicitly, as required by the assignment. */
    public synchronized List<Student> getAllStudents() {
        List<Student> result = new ArrayList<>();
        Iterator<Student> iterator = students.iterator();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }

    public synchronized Optional<Student> getStudentById(int id) {
        return students.stream()
                .filter(student -> student.getId() == id)
                .findFirst();
    }

    /** Uses Student.compareTo(), which sorts by ID. */
    public synchronized List<Student> getStudentsSortedById() {
        List<Student> result = new ArrayList<>(students);
        result.sort(null);
        return result;
    }

    /** Uses a Comparator to sort by CGPA in descending order. */
    public synchronized List<Student> getStudentsSortedByCgpa() {
        List<Student> result = new ArrayList<>(students);
        result.sort(new CgpaDescendingComparator());
        return result;
    }

    /** Uses a HashSet so every department appears only once. */
    public synchronized Set<String> getDepartments() {
        Set<String> departments = new HashSet<>();
        for (Student student : students) {
            departments.add(student.getDepartment());
        }
        return departments;
    }

    public synchronized List<Student> getRecentStudents() {
        return new ArrayList<>(recentStudents);
    }

    public synchronized Student peekRecentStudent() {
        return recentStudents.empty() ? null : recentStudents.peek();
    }

    public synchronized Student popRecentStudent() {
        return recentStudents.empty() ? null : recentStudents.pop();
    }

    public synchronized void save() throws IOException {
        Path parent = storageFile.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (ObjectOutputStream output = new ObjectOutputStream(
                new BufferedOutputStream(Files.newOutputStream(storageFile)))) {
            output.writeObject(new ArrayList<>(students));
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized int load() throws IOException, ClassNotFoundException {
        if (!Files.exists(storageFile)) {
            return 0;
        }
        try (ObjectInputStream input = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(storageFile)))) {
            List<Student> restored = (List<Student>) input.readObject();
            students.clear();
            students.addAll(restored);
            recentStudents.clear();
            int start = Math.max(0, students.size() - 3);
            for (int index = start; index < students.size(); index++) {
                recentStudents.push(students.get(index));
            }
            return students.size();
        }
    }
}
