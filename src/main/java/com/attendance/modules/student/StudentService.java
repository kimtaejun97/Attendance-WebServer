package com.attendance.modules.student;

import com.attendance.modules.studentlecture.StudentLecture;
import com.attendance.modules.studentlecture.StudentLectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    private final StudentLectureRepository studentLectureRepository;

    public void addStudent(StudentForm studentForm, String lectureCode) {
        Student student = Student.builder()
                .studentName(studentForm.getUsername())
                .build();

        Student newStudent =  studentRepository.save(student);

        studentLectureRepository.save(
                StudentLecture.builder()
                        .studentName(newStudent.getStudentName())
                        .lectureCode(lectureCode)
                        .build()
        );



    }
}
