package com.attendance.modules.lecture;

import com.attendance.modules.lecture.form.LectureFormValidator;
import com.attendance.modules.lecture.form.LectureListResponseDto;
import com.attendance.modules.lecture.form.LectureForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class LectureController {

    private final LectureService lectureService;

    private final LectureFormValidator lectureFormValidator;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(lectureFormValidator);
    }

    @GetMapping("/my-lecture")
    public String my_lecture(){
        return "student/my-lecture";
    }

    @GetMapping("/admin-page")
    public String adminPage(Model model){
        List<LectureListResponseDto> lectures = lectureService.showLectureList();
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
}
