package com.api.fascinareventos.models;

import com.api.fascinareventos.models.views.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.net.URL;
import java.util.Objects;

@Entity
@Table(name = "tb_team")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Team implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonView(View.Full.class)
    private Long id;
    @JsonView(View.Base.class)
    private URL avatar;
    @Column(nullable = false)
    @JsonView(View.Base.class)
    private String name;
    @ManyToOne
    @JsonView(View.Base.class)
    @JoinColumn(name = "role_id", nullable = true)
    private TeamRoles role;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = true)
    @JsonView(View.Full.class)
    private User user;

    @JsonView(View.Full.class)
    private Boolean enable = true;

    public Team(URL avatar, String name, TeamRoles role, User user) {
        this.avatar = avatar;
        this.name = name;
        this.role = role;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Team team = (Team) o;
        return id != null && Objects.equals(id, team.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

