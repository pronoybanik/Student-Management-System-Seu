package com.example.student_management_system.model;

import java.io.Serializable;
import java.util.Objects;

/** A serializable student record used by the REST API. */
public class Student implements Comparable<Student>, Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String department;
    private double cgpa;

    public Student() {
    }

    public Student(int id, String name, String department, double cgpa) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.cgpa = cgpa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    @Override
    public int compareTo(Student other) {
        return Integer.compare(this.id, other.id);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Student student)) return false;
        return id == student.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', department='" + department + "', cgpa=" + cgpa + "}";
    }
}
