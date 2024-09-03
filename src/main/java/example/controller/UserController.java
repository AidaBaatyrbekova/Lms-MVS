package example.controller;

import example.model.Course;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import example.model.Group;
import example.model.User;
import example.service.impl.GroupService;
import example.service.impl.StudentService;
import example.service.impl.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    final UserService userService;
    final StudentService studentService;
    final GroupService groupService;

    @ModelAttribute("groups")
    public List<Group> groups(){
        return groupService.findAll();
    }

    @GetMapping("/add")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        return "user/save";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/user/find-all";
    }

    @GetMapping("/find-all")
    public String findAll(Model model) {
        List<User> userList = userService.findAll();
        model.addAttribute("userList", userList);
        return "user/get-all";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user/update";
    }

    @PostMapping("/update-save/{id}")
    public String saveUpdate(@PathVariable("id") Long id, @ModelAttribute("user") User user) {
        userService.update(id, user);
        return "redirect:/user/find-all";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model) {
        userService.delete(id);
        model.addAttribute("userList", userService.findAll());
        return "user/get-all";
    }

    @GetMapping("/profile")
    public String getProfile(Principal principal, Model model){
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/students")
    public String getStudents(Model model){
        model.addAttribute("students", studentService.findAllStudents());
        return "user/students";
    }
}
