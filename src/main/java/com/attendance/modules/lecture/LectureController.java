package com.attendance.modules.lecture;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.CurrentUser;
import com.attendance.modules.lecture.form.LectureFormValidator;
import com.attendance.modules.lecture.form.LectureListResponseDto;
import com.attendance.modules.lecture.form.LectureForm;
import com.attendance.modules.student.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class LectureController {

    private final LectureService lectureService;
    private final LectureRepository lectureRepository;

    private final LectureFormValidator lectureFormValidator;

    @InitBinder("lectureForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(lectureFormValidator);
    }

    @GetMapping("/admin-page")
    public String adminPage(Model model){
        List<LectureListResponseDto> lectures = lectureService.getLectureList();
        model.addAttribute("lectures", lectures);

        return "admin/admin-page";
    }
    @GetMapping("/create-lecture")
    public String addLecture(Model model){
        model.addAttribute(new LectureForm());
        return "admin/create-lecture";
    }

    @PostMapping("/create-lecture")
    public String addLecture(@Valid LectureForm lectureForm, Errors errors){
        if(errors.hasErrors()){
            return "admin/create-lecture";
        }

        lectureService.addLecture(lectureForm);
        return "redirect:/admin-page";
    }

    @GetMapping("/lecture/{lectureCode}")
    public String lectureInfo (@PathVariable String lectureCode, Model model){
        Lecture lecture = lectureRepository.findByLectureCode(lectureCode);

        List<String> students = lectureService.getStudentFromLectureCode(lectureCode);

        model.addAttribute(lecture);
        model.addAttribute("students",students);


        return "admin/lecture";
    }

    @GetMapping("/my-lecture")
    public String my_lecture(@CurrentUser Account account, Model model){
        List<Lecture> lectures = lectureService.getStudentLectures(account.getNickname());
        model.addAttribute("lectures", lectures);
        return "student/my-lecture";

    }
}
