package example.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import example.model.Course;
import example.model.User;
import example.service.impl.CourseService;
import example.service.impl.InstructorService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/instructor")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InstructorController {
    InstructorService instructorService;
    CourseService courseService;

    @ModelAttribute("courses")
    public List<Course> companyList() {
        ArrayList<Course> courses = new ArrayList<>();
        for (Course course : courseService.findAll()) {
            if (course.getUser() == null){
                courses.add(course);
            }
        }
        return courses;
    }

    @GetMapping("/add")
    public String addInstructor(Model model) {
        model.addAttribute("instructor", new User());
        return "instructor/save";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        instructorService.save(user);
        return "redirect:/instructor/find-all";
    }
    @GetMapping("/find-all")
    public String findAll(Model model){
        model.addAttribute("instructors",instructorService.findAllInstructors());
        return "instructor/get-all";
    }
}
