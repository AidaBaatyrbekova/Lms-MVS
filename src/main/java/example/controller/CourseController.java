package example.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import example.model.Company;
import example.model.Course;
import example.service.impl.CompanyService;
import example.service.impl.CourseService;

import java.util.List;

@Controller
@RequestMapping("/course")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseController {
    CourseService courseService;
    CompanyService companyService;

    @ModelAttribute("companies")
    public List<Company> companyList() {
        return companyService.findAll();
    }

    @GetMapping("/add")
    public String addCourse( Model model) {
        model.addAttribute("course", new Course());
        
        return "course/save";
    }

    @PostMapping("/save")
    public String saveCourse(@ModelAttribute("company") Course course) {
        courseService.save(course);
        return "redirect:find-all";
    }

    @GetMapping("/find-all")
    public String findAll(Model model) {
        List<Course> courses = courseService.findAll();
        model.addAttribute("courses", courses);
        return "course/get-all";
    }
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        Course course = courseService.findById(id);
        model.addAttribute("course", course);
        return "course/update";
    }

    @PostMapping("/save-update/{id}")
    public String saveUpdate(@PathVariable("id") Long id, @ModelAttribute("course") Course course) {
        courseService.update(id, course);
        return "redirect:/course/find-all";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model) {
        courseService.delete(id);
        model.addAttribute("courses", courseService.findAll());
        return "course/get-all";
    }
}
