package com.attendance.modules.lecture;

import com.attendance.modules.lecture.form.LectureListResponseDto;
import com.attendance.modules.lecture.form.LectureForm;
import com.attendance.modules.student.Student;
import com.attendance.modules.student.StudentRepository;
import com.attendance.modules.studentlecture.StudentLecture;
import com.attendance.modules.studentlecture.StudentLectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Transactional
@RequiredArgsConstructor
@Service
public class LectureService {

    private final LectureRepository lectureRepository;

    private final StudentRepository studentRepository;

    private final StudentLectureRepository studentLectureRepository;

    public String addLecture(LectureForm lectureForm) {

        return lectureRepository.save(lectureForm.toEntity()).getLectureName();
    }

    public List<LectureListResponseDto> getLectureList() {
        return lectureRepository.findAll().stream()
                .map(LectureListResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<Student> getStudentFromLectureCode(String lectureCode) {
        List<StudentLecture> studentLectures = studentLectureRepository.findAllByLectureCode(lectureCode);
       return  studentLectures.stream()
                .map(studentLecture ->
                        studentRepository.findByStudentName(studentLecture.getStudentName()))
                .collect(Collectors.toList());
    }

    public List<Lecture> getStudentLectures(String studentName) {
        List<StudentLecture> studentLectures =  studentLectureRepository.findByStudentName(studentName);

        return studentLectures.stream()
                .map(studentLecture ->
                    lectureRepository.findByLectureCode(studentLecture.getLectureCode()))
                .collect(Collectors.toList());

    }
}
