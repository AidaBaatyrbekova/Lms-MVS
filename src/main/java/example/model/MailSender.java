package example.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "mailigh")
@Getter
@Setter
@NoArgsConstructor
public class MailSender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String subject;
    String  text;
    LocalDate creationDate;


}
