package example.controller;

import example.service.impl.CourseService;
import example.service.impl.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import example.model.Company;
import example.model.Course;
import example.service.impl.CompanyService;

import java.util.List;

@Controller
@RequestMapping("/company")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyController {
    CompanyService companyService;
    CourseService courseService;

    @GetMapping("/add")
    public String addCompany(Model model) {
        model.addAttribute("company", new Company());
        return "company/save";
    }

    @PostMapping("/save")
    public String saveCompany(@ModelAttribute("company") Company company) {
        companyService.save(company);
        return "redirect:add";
    }

    @GetMapping("/find-all")
    public String findAll(Model model) {
        List<Company> companyList = companyService.findAll();
        model.addAttribute("companyList", companyList);
        return "company/get-all";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        Company company = companyService.findById(id);
        model.addAttribute("company", company);
        return "company/update";
    }

    @PostMapping("/update-save/{id}")
    public String saveUpdate(@PathVariable("id") Long id, @ModelAttribute("company") Company company) {
        companyService.update(id, company);
        return "redirect:/company/find-all";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model) {
        companyService.delete(id);
        model.addAttribute("companies", companyService.findAll());
        return "company/get-all";
    }

    @GetMapping("/courses/{companyId}")
    public String getCourseByCompany(@PathVariable("companyId") Long companyId, Model model) {
        List<Course> courses = companyService.getCoursesByCompany(companyId);
        model.addAttribute("company1", companyService.findById(companyId));
        model.addAttribute("courses", courses);
        model.addAttribute("size", courses.size());
        return "company/get-courses";
    }
}