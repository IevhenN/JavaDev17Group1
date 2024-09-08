package grp1.user;

import grp1.note.Note;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(exclude = "notes")
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @Size(min = 5, max = 50, message = "{error.username.empty}")
    @NotEmpty(message = "{error.username.empty}")
    private String username;

    @Column
    @Size(min = 8, max = 100, message = "{error.password.empty}")
    @NotEmpty(message = "{error.username.empty}")
    private String password;
    @Column
    private boolean enabled = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Note> notes = new ArrayList<>();
}
