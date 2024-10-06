package team1.TJFHabitTrackerBE.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({
        "password", "enabled", "authorities",
        "accountNonLocked", "credentialsNonExpired",
        "accountNonExpired", "habits", "habitCompletions"
})

public class User  implements UserDetails{
    @Id
    private String id;
    private String email;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;
    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    private Set<Habits> habits = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HabitCompletion> habitCompletions = new ArrayList<>();


    public User(String id, String email, Date createdAt, Date updatedAt) {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
