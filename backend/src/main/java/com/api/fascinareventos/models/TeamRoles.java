package com.api.fascinareventos.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tb_team_roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TeamRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(nullable = false, unique = true)
    private String role;

    public TeamRoles(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TeamRoles teamRoles = (TeamRoles) o;
        return id != null && Objects.equals(id, teamRoles.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
