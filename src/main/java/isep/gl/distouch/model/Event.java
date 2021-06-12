package isep.gl.distouch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    @NotEmpty(message = "*Please provide your password")
    private String title;

    @OneToOne(cascade = CascadeType.ALL)
    @NotNull(message = "*Please provide the organizer of the event")
    @JoinColumn(name = "organizer_id", referencedColumnName = "id")
    private User organizer;

    @Column(name = "public")
    @NotNull(message = "*Please provide the confidentiality of the event")
    private boolean isPublic;

    @Column(name = "date")
    @NotNull(message = "*Please provide the confidentiality of the event")
    private Date date;

    @Column(name = "address")
    @Length(min = 5, message = "*Your address must have at least 5 characters")
    @NotEmpty(message = "*Please provide your address")
    private String address;

    @Column(name = "city")
    @NotEmpty(message = "*Please provide your city name")
    private String city;

    @Column(name = "zip_code")
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotNull(message = "*Please provide your Zip Code")
    private int zipCode;

    @Column(name = "country")
    @NotEmpty(message = "*Please provide your country name")
    private String country;

    @ManyToMany
    @JoinTable(name = "user_event",
            joinColumns = @JoinColumn(name = "participant_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    Set<User> participants;
}
