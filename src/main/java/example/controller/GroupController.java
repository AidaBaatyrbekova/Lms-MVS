package example.controller;

import example.model.User;
import example.service.impl.StudentService;
import example.service.impl.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import example.model.Course;
import example.model.Group;
import example.service.impl.CourseService;
import example.service.impl.GroupService;
import example.service.impl.CompanyService;

import java.util.List;

@Controller
@RequestMapping("/group")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GroupController {

    final GroupService groupService;
    final CourseService courseService;
    final StudentService studentService;
    final CompanyService companyService;

    @ModelAttribute("courses")
    public List<Course> courseList() {
        return courseService.findAll();
    }

    @GetMapping("/add")
    public String addGroup(Model model) {
        model.addAttribute("group", new Group());
        model.addAttribute("courses", courseService.findAll());
        return "group/save";
    }

    @PostMapping("/save")
    public String saveGroup(@ModelAttribute("group") Group group) {
        groupService.save(group);
        return "redirect:/group/list";
    }

    @GetMapping("/find-all")
    public String findAll(Model model) {
        List<Group> groups = groupService.findAll();
        model.addAttribute("groups", groups);
        return "group/get-all";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        Group group = groupService.findById(id);
        model.addAttribute("group", group);
        return "group/update";
    }

    @PostMapping("/update-save/{id}")
    public String saveUpdate(@PathVariable("id") Long id, @ModelAttribute("group") Group group) {
        groupService.update(id, group);
        return "redirect:/group/find-all";
    }

    @GetMapping("/delete/{id}")
    public String deleteGroup(@PathVariable("id") Long id, Model model) {
        groupService.delete(id);
        model.addAttribute("groups", groupService.findAll());
        return "group/get-all";
    }

    @GetMapping("/search")
    public String getStudentByName(Model model, @RequestParam("name") String name) {
        List<User> studentList = studentService.getStudentByName(name);
        model.addAttribute("students", studentList);
        model.addAttribute("count", studentList.size());
        return "group/get-group";
    }

    @GetMapping("/students-per-teacher")
    public String calculateStudentsPerTeacher(Model model) {
        double ratio = companyService.calculateStudentsPerTeacher();
        model.addAttribute("ratio", ratio);
        return "group/students-per-teacher";
    }
}
