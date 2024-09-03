package example.controller;

import example.model.MailSender;
import example.service.impl.MailSenderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mailing")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MailSenderController {
    @Autowired
    MailSenderService mailSenderService;

    @GetMapping
    public String mailSender(Model model) {
        model.addAttribute("mailing", new MailSender());
        return "mailing/save";
    }

    @PostMapping("/save")
    public String saveMailing(@ModelAttribute("mailing") MailSender mailSender) {
        mailSenderService.save(mailSender);
        return "redirect:/mailing";
    }
}

