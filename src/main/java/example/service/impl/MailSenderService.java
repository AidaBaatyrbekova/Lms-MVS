package example.service.impl;
import example.model.MailSender;
import example.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional

public class MailSenderService {
    @PersistenceContext
    private EntityManager manager;

    private final JavaMailSender javaMailSender;
    private final UserService userService;

    @Autowired
    public MailSenderService(JavaMailSender javaMailSender, UserService userService) {
        this.javaMailSender = javaMailSender;
        this.userService = userService;
    }


    public void save(MailSender mailSender) {
        List<User> users = userService.findAll();
        for (User user : users) {
            if (user.isSubscribe()) {
                sendMassage(mailSender, user.getEmail());
            }
        }
        manager.persist(mailSender);
    }

    public void sendMassage(MailSender mailSender, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("admin@gmail.com");
        message.setSubject(mailSender.getSubject());
        message.setText(mailSender.getText());
        javaMailSender.send(message);
    }
}


