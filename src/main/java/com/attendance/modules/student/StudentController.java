package com.attendance.modules.student;

import com.attendance.modules.studentlecture.StudentLectureRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class StudentController {

    private final StudentService studentService;

    private final StudentFormValidator studentFormValidator;



    @InitBinder("/studentForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(studentFormValidator);
    }

    @GetMapping("/add-student/{lectureCode}")
    public String addStudent(@PathVariable String lectureCode,Model model){
        model.addAttribute("lectureCode",lectureCode);
        model.addAttribute(new StudentForm());
        return "admin/add-student";
    }

    @PostMapping("/add-student/{lectureCode}")
    public String addStudentForm(@Valid StudentForm studentForm,@PathVariable String lectureCode, Errors errors){
        //TODO Validator 왜 안먹지...
        if(errors.hasErrors()){
            return "redirect:admin/add-student"+lectureCode;
        }
        studentService.addStudent(studentForm,lectureCode);

        return "redirect:/lecture/"+lectureCode;
    }

}
