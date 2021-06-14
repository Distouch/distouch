package isep.gl.distouch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.time.Period;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "email")
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;

    @Column(name = "password")
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;

    @Column(name = "first_name")
    @NotEmpty(message = "*Please provide your first name")
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty(message = "*Please provide your last name")
    private String lastName;

    public String getName() {
        return firstName + " " + lastName;
    }

    @Column(name = "birth_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "*Please provide your birth date")
    @Past(message = "*Please provide a valid birth date")
    private LocalDate birthDate;

    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    @Column(name = "city")
    @NotEmpty(message = "*Please provide your city name")
    private String city;

    @Column(name = "country")
    @NotEmpty(message = "*Please provide your country name")
    private String country;

    @ManyToMany
    @JoinTable(name = "user_event",
            joinColumns = @JoinColumn(name = "participant_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    Set<Event> events;
}
