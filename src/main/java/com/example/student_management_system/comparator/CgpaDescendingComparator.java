package com.example.student_management_system.comparator;

import com.example.student_management_system.model.Student;

import java.util.Comparator;

/** Sorts students from the highest CGPA to the lowest CGPA. */
public class CgpaDescendingComparator implements Comparator<Student> {

    @Override
    public int compare(Student first, Student second) {
        return Double.compare(second.getCgpa(), first.getCgpa());
    }
}
